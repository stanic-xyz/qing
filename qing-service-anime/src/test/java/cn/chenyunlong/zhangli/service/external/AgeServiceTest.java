package cn.chenyunlong.zhangli.service.external;

import cn.chenyunlong.zhangli.model.agefans.AgeAnimeInfo;
import cn.chenyunlong.zhangli.model.agefans.AgePlayInfoModel;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

class AgeServiceTest {

    private final AgeService ageService;

    AgeServiceTest() {
        ageService = new AgeService(new RestTemplate(), new ObjectMapper());
    }

    @Test
    void testGetPlayDetail() throws IOException {
        int count = 0;
        String read = IoUtil.read(Files.newInputStream(Paths.get("test.json")), StandardCharsets.UTF_8);
        final List<AgeAnimeInfo> finalAnimeInfoList = new LinkedList<>();
        List<AgeAnimeInfo> animeInfoList = JSONObject.parseArray(read).toJavaList(AgeAnimeInfo.class);
        for (AgeAnimeInfo ageAnimeInfo : animeInfoList) {
            AgeAnimeInfo detail = ageService.getDetail(ageAnimeInfo.getAnimeId());
            if (detail == null) {
                continue;
            }
            detail.setDetailCoverUrl(ageAnimeInfo.getCoverUrl());
            detail.setAnimeId(ageAnimeInfo.getAnimeId());
            finalAnimeInfoList.add(detail);
            if (count > 2) {
                break;
            }
            count++;
        }
        System.out.println(JSONObject.toJSONString(finalAnimeInfoList, true));
    }

    @Test
    void testGetDetail() {
        AgePlayInfoModel servicePlayDetail = ageService.getPlayDetail(20220028, 3, 1);
        System.out.println(servicePlayDetail);
    }

    @Test
    void getAnimeListByIndex() {
        List<AgeAnimeInfo> elements = ageService.getAnimeListByIndex(1);
        System.out.println(elements);
    }

    @Test
    void getAnimeList() throws IOException {
        List<AgeAnimeInfo> animeList = ageService.getAnimeList();
        String jsonString = JSONObject.toJSONString(animeList);
        try (FileWriter fileWriter = new FileWriter("test.json")) {
            fileWriter.append(jsonString);
            fileWriter.flush();
        }
    }
}