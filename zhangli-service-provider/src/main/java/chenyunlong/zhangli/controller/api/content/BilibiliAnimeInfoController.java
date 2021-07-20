package chenyunlong.zhangli.controller.api.content;

import chenyunlong.zhangli.model.entities.bilibili.BilibiliAnimeScoreEntity;
import chenyunlong.zhangli.common.service.BilibiliAnimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/anime/bilibili")
public class BilibiliAnimeInfoController {

    private final BilibiliAnimeService animeInfoService;

    public BilibiliAnimeInfoController(BilibiliAnimeService animeInfoService) {
        this.animeInfoService = animeInfoService;
    }

    @GetMapping("scores")
    public List<BilibiliAnimeScoreEntity> getScoreList(Long animeId, LocalDateTime startTime, LocalDateTime endTime) {
        return animeInfoService.getScoreInfoList(animeId, startTime, endTime);
    }

    @GetMapping("update")
    public void updateAnimeInfo() {
        animeInfoService.updateAnimeInfo();
    }
}
