/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain;

import cn.chenyunlong.qing.domain.third.diygod.figure.FigureEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ImageCheckerTest {

    @Test
    @DisplayName("测试生成日期")
    void generateDate() {
        LocalDateTime localDateTime = LocalDateTime.of(2005, 12, 30, 0, 0, 0);
        for (int i = 0; i < 100; i++) {
            String idCard = "51130319961002" + "5142";
            localDateTime = localDateTime.plusDays(1);
            String str = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("511303" + str + "5142 \t " + format);
        }
    }

    @Disabled
    @Test
    @DisplayName("从diyGod获取手办信息")
    public void getGkFromDiyGod() {

        HttpComponentsClientHttpRequestFactory httpRequestFactory =
            new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(1000);
        httpRequestFactory.setConnectTimeout(1000);
        httpRequestFactory.setReadTimeout(10000);

        HttpHeaders httpHeaders = new HttpHeaders();
        String url = "https://diygod.me/gk/";
        httpHeaders.add("Referer", url);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory);
        ResponseEntity<String> responseEntity;
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String gksBody = responseEntity.getBody();
            if (StringUtils.hasText(gksBody)) {
                Document document = Jsoup.parse(gksBody);
                //获取手办列表
                Elements elements = document.getElementsByClass("gk-item");
                List<FigureEntity> figureEntities = new LinkedList<>();
                for (Element element : elements) {
                    FigureEntity figureEntity = new FigureEntity();
                    //像jquery选择器一样，获取文章标题元素
                    Objects.requireNonNull(Objects.requireNonNull(element.select(".gk-img")
                                .first()).getElementsByTag("picture")
                            .first()).getElementsByTag("img")
                        .stream().findFirst().ifPresent(img -> {
                            String coverUrl = img.attr("src");
                            System.out.println("https://diygod.me/" + coverUrl);
                            figureEntity.setCoverImg("https://diygod.me/" + coverUrl);
                        });

                    Objects.requireNonNull(element.select(".gk-desc")
                        .first()).children().forEach(p -> {
                        String key = ((TextNode) p.childNodes().get(0).childNode(0)).text();

                        String value;
                        if (key.startsWith("链接")) {
                            System.out.println(key);
                            value = p.childNodes().get(1).attr("href");
                            int index = 1;
                            p.childNodes().forEach(node
                                    -> {
                                    if (!"strong".equals(node.nodeName())) {
                                        System.out.println("链接" + index + "：" + node.attr("href"));
                                    }
                                }
                            );
                            figureEntity.setLinks(value);
                        } else if (key.startsWith("名称")) {
                            value = ((TextNode) p.childNodes().get(1)).text();
                            figureEntity.setName(value);
                        } else if (key.startsWith("角色")) {
                            value = ((TextNode) p.childNodes().get(1)).text();
                            figureEntity.setCharacterName(value);
                        } else if (key.startsWith("作品")) {
                            value = ((TextNode) p.childNodes().get(1)).text();
                            figureEntity.setRelatedWork(value);
                        } else if (key.startsWith("制作")) {
                            value = ((TextNode) p.childNodes().get(1)).text();
                            figureEntity.setProducer(value);
                        } else if (key.startsWith("出荷")) {
                            value = ((TextNode) p.childNodes().get(1)).text();
                            String releaseDate = value.replace("年", "-").replace("月", "");
                            String[] split = releaseDate.split("-");
                            if (split.length == 2) {
                                figureEntity.setReleaseDate(LocalDate.of(Integer.parseInt(split[0]),
                                    Integer.parseInt(split[1]), 1));
                            }
                        } else if (key.startsWith("价格")) {
                            value = ((TextNode) p.childNodes().get(1)).text();
                            figureEntity.setPrice(
                                BigDecimal.valueOf(Double.parseDouble(value.replaceAll("元", ""))));
                        }
                    });
                    figureEntities.add(figureEntity);
                }
                System.out.println(figureEntities);
            }
        }
    }
}
