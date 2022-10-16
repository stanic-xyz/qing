package cn.chenyunlong.zhangli.service.external;

import cn.chenyunlong.zhangli.model.agefans.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgeService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AgeService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 获取播放详情
     *
     * @param animeId    动漫ID
     * @param playlistId 播放列表ID
     * @param episodeId  单集ID
     * @return 播放信息
     */
    public AgePlayInfoModel getPlayDetail(long animeId, int playlistId, int episodeId) {
        Random random = new Random();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //60s
        requestFactory.setConnectTimeout(60 * 1000);
        requestFactory.setReadTimeout(60 * 1000);

        String url = String.format("https://www.agemys.com/_getplay?aid=%d&playindex=%d&epindex=%d", animeId, playlistId, episodeId);
        String referUrl = "https://www.agemys.com";
        StringBuilder cookie = new StringBuilder();
        String playUrl = "https://www.agemys.com/_getplay2?kp=4zPeWaqNhi20IUkB6rXFO2pAJ1otsSb%2FIzpjyjuYSXKly9zoxWCaSFno%2FSoM8yFblxl8IVjeTJ5eIaCbwj%2BgpYDwjV02ZDL8dxaEsoxzUR9hzjeC4pd38w%3D%3D";

        String currentUrl = url + "&r=" + random.nextDouble();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        headers.add("Cookie", cookie.toString());
        headers.add("Host", "www.agefans.cc");
        headers.add("Referer", referUrl);

        HttpEntity<String> stringHttpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> firstResponse;
        firstResponse = restTemplate.exchange(url, HttpMethod.GET, stringHttpEntity, String.class);

        List<String> sessions = firstResponse.getHeaders().get("Set-Cookie");
        assert sessions != null;
        sessions.stream().map(session -> session.substring(0, session.indexOf(";")))
                .forEach(s -> cookie.append(";").append(s));

        headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        headers.add("Cookie", cookie.toString());
        headers.add("Host", "www.agemys.com");
        headers.add("Referer", referUrl);
        stringHttpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response;
        response = restTemplate.exchange(currentUrl, HttpMethod.GET, stringHttpEntity, String.class);
        System.out.println(response);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            System.out.println(responseBody);
            try {
                return objectMapper.readValue(responseBody, AgePlayInfoModel.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取动漫详情信息
     *
     * @param animeId 动漫ID
     * @return 动漫信息
     */
    public AgeAnimeInfo getDetail(long animeId) {
        String url = "https://www.agemys.com/detail/" + animeId;
        AgeAnimeInfo animeInfo = new AgeAnimeInfo();
        Document document = null;
        while (document == null) {
            try {
                document = Jsoup.parse(new URL(url), 5000);
            } catch (IOException e) {
                log.error("获取动漫详情信息失败了,错误原因：{}，即将发起重试:", e.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        Elements playLists = document.body().select(".movurl > ul");
        List<AgePlayList> agePlayList = new LinkedList<>();
        //获取所有播放列表
        int index = 1;
        for (Element playList : playLists) {
            Elements items = playList.select("a");
            if (!items.isEmpty()) {
                List<AgeAnimeEpisode> episodeList = new LinkedList<>();

                AgePlayList listItem = new AgePlayList();

                for (int i = 0; i < items.size(); i++) {
                    Element element = items.get(i);
                    AgeAnimeEpisode episode = new AgeAnimeEpisode();
                    episode.setIndex(i);
                    episode.setTitle(element.text());
                    episode.setHref(element.attr("href"));
                    episodeList.add(episode);
                }
                listItem.setEpisodeList(episodeList);
                agePlayList.add(listItem);
                listItem.setIndex(index);
                index++;
            }
        }
        //获取推荐列表
        Elements recommendList = document.select("#recommend_block>ul>li");

        List<AgeRecommendInfo> recommendInfos = recommendList.stream().map(element -> {
            AgeRecommendInfo recommend = new AgeRecommendInfo();
            recommend.setHref(Objects.requireNonNull(element.select("a").first()).attr("href"));
            recommend.setHeight(Integer.valueOf(Objects.requireNonNull(element.select(".anime_icon1_img").first()).attr("height")));
            recommend.setWidth(Integer.valueOf(Objects.requireNonNull(element.select(".anime_icon1_img").first()).attr("width")));
            recommend.setImgUrl(Objects.requireNonNull(element.select(".anime_icon1_img").first()).attr("data-src"));
            recommend.setAnimeName(Objects.requireNonNull(element.select(".anime_icon1_name").first()).text());
            return recommend;
        }).collect(Collectors.toList());

        AgeBaiduNet baiduNet = new AgeBaiduNet();
        Element first = document.select(".res_links>.res_links_a").first();
        String href = first != null ? first.attr("href") : "";
        baiduNet.setAgeLink(href);

        Element name = document.select(".res_links").first();
        baiduNet.setName(name != null ? name.text() : "");

        Element netDisk = document.select(".res_links>.res_links_pswd").first();
        baiduNet.setPassword(netDisk != null ? netDisk.text() : "");
        animeInfo.setBaiduNet(baiduNet);

        //获取班级名称和描述
        animeInfo.setName(document.select(".detail_imform_name").text());
        animeInfo.setInstruction(document.select(".detail_imform_desc_pre").text());

        //获取教师的详细信息
        Elements animeDetail = document.select(".blockcontent>.detail_imform_kv");


        Element district = animeDetail.get(0).select(".detail_imform_value").first();
        animeInfo.setDistrict(district != null ? district.text() : "");

        Element type = animeDetail.get(1).select(".detail_imform_value").first();
        animeInfo.setType(type != null ? type.text() : "");

        Element originalName = animeDetail.get(2).select(".detail_imform_value").first();
        animeInfo.setOriginalName(originalName != null ? originalName.text() : "");

        Element otherName = animeDetail.get(3).select(".detail_imform_value").first();
        animeInfo.setOtherName(otherName != null ? otherName.text() : "");

        Element author = animeDetail.get(4).select(".detail_imform_value").first();
        animeInfo.setAuthor(author != null ? author.text() : "");

        Element company = animeDetail.get(5).select(".detail_imform_value").first();
        animeInfo.setCompany(company != null ? company.text() : "");

        Element premiereDate = animeDetail.get(6).select(".detail_imform_value").first();
        animeInfo.setPremiereDate(premiereDate != null ? premiereDate.text() : "");

        Element playStatus = animeDetail.get(7).select(".detail_imform_value").first();
        animeInfo.setPlayStatus(playStatus != null ? playStatus.text() : "");

        Element plotType = animeDetail.get(8).select(".detail_imform_value").first();
        animeInfo.setPlotType(plotType != null ? plotType.text() : "");

        Element tags = animeDetail.get(9).select(".detail_imform_value").first();
        animeInfo.setTags(tags != null ? tags.text() : "");

        Element officalWebsite = animeDetail.get(10).select(".detail_imform_value").first();
        animeInfo.setOfficialWebsite(officalWebsite != null ? officalWebsite.text() : "");

        animeInfo.setRecommendList(recommendInfos);
        //添加播放信息
        animeInfo.setAgePlayListList(agePlayList);
        return animeInfo;
    }

    /**
     * 获取所有动漫列表
     *
     * @return 动漫信息
     */
    public List<AgeAnimeInfo> getAnimeList() {
        final List<AgeAnimeInfo> elements = new LinkedList<>();
        for (int i = 0; i < 149; i++) {
            elements.addAll(getAnimeListByIndex(i));
        }
        return elements;
    }

    public List<AgeAnimeInfo> getAnimeListByIndex(int page) {
        List<AgeAnimeInfo> animeList = new LinkedList<>();
        String url = "https://www.agemys.com/catalog/all-all-all-all-all-time-" + page;
        AgeAnimeInfo animeInfo = new AgeAnimeInfo();
        Document document;
        try {
            Thread.sleep(1000);
            document = Jsoup.parse(new URL(url), 5000);
            Element body = document.body();
            Elements elementsByClass = body.getElementsByClass("blockcontent1");
            Element first = elementsByClass.first();
            if (first != null) {
                Elements animeInfoList = Objects.requireNonNull(first).getElementsByClass("cell");
                for (Element element : animeInfoList) {
                    Element headElement = element.getElementsByTag("a").first();
                    assert headElement != null;
                    //获取链接地址
                    String href = headElement.attr("href");
                    String animeId = href.substring(href.lastIndexOf("/") + 1);
                    //获取图片地址
                    String img = Objects.requireNonNull(headElement.getElementsByTag("img").first()).attr("src");

                    System.out.println("animeId：" + animeId);
                    System.out.println(href);
                    System.out.println(img);
                    AgeAnimeInfo ageAnimeInfo = new AgeAnimeInfo();
                    ageAnimeInfo.setAnimeId(Long.parseLong(animeId));
                    ageAnimeInfo.setCoverUrl(img);
                    animeList.add(ageAnimeInfo);
                }
            }
        } catch (IOException e) {
            log.error("获取动漫详情信息失败了", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return animeList;
    }
}
