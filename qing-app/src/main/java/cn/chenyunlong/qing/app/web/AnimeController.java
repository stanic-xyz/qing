package cn.chenyunlong.qing.app.web;

import cn.chenyunlong.qing.domain.third.bilibili.response.AnimeInfoDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "发布信息")
@RestController
@RequestMapping("api/v1/anime")
public class AnimeController {

    @GetMapping("{animeId}")
    public AnimeInfoDTO findAnimeById(@PathVariable("animeId") Long animeId) {
        AnimeInfoDTO animeInfoDTO = new AnimeInfoDTO();
        animeInfoDTO.setId(animeId);
        return animeInfoDTO;
    }
}
