package chenyunlong.zhangli.controller.content;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.request.ImportAnimeModel;
import chenyunlong.zhangli.service.AnimeInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("api")
public class AdminController {

    private final AnimeInfoService animeInfoService;

    public AdminController(AnimeInfoService animeInfoService) {
        this.animeInfoService = animeInfoService;
    }

    @PostMapping("animes")
    public List<ImportAnimeModel> importAnime(@RequestBody List<ImportAnimeModel> requestModel) {

        List<ImportAnimeModel> result = new LinkedList<>();

        List<AnimeInfo> animeInfos = new LinkedList<>();
        requestModel.forEach(model -> {
            animeInfos.clear();
            AnimeInfo animeInfo = new AnimeInfo();
            BeanUtils.copyProperties(model, animeInfo);
            animeInfo.setId(model.getUrl());
            animeInfo.setCoverUrl(model.getCover());
            animeInfo.setTags(model.getPlotType());
            animeInfos.add(animeInfo);
            try {
                animeInfoService.add(animeInfos);
            } catch (Exception exception) {
                result.add(model);
            }
        });

        return result;
    }
}
