package chenyunlong.zhangli.controller.content;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.response.anime.AnimeInfoRankModel;
import chenyunlong.zhangli.service.AnimeInfoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Stan
 */
@Controller
@RequestMapping("movie")
public class MovieController {

    private final AnimeInfoService animeInfoService;

    public MovieController(AnimeInfoService animeInfoService) {
        this.animeInfoService = animeInfoService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("detail/{movieId}")
    public ModelAndView movie(@PathVariable(value = "movieId", required = false) String movieId) {
        AnimeInfo animeInfo = animeInfoService.getMovieDetail(movieId);
        ModelAndView modelAndView = new ModelAndView("detail");
        modelAndView.addObject(animeInfo);
        return modelAndView;
    }

    @GetMapping("catalog")
    public ModelAndView catalog(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        if (page < 1) {
            page = 1;
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 24);
        AnimeInfoRankModel rankModel = animeInfoService.getRankPage(pageRequest);
        ModelAndView modelAndView = new ModelAndView("catalog");
        modelAndView.addObject("animeList", rankModel.getAnimeInfoList());
        modelAndView.addObject("total", rankModel.getTotalCount());
        modelAndView.addObject("number", pageRequest.getPageNumber() + 1);
        return modelAndView;
    }

    @GetMapping("rank")
    public ModelAndView rank(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "year", required = false) String catyear
    ) {
        if (page < 1) {
            page = 1;
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 75);
        AnimeInfoRankModel rankModel = animeInfoService.getRankPage(pageRequest);
        ModelAndView modelAndView = new ModelAndView("rank");
        modelAndView.addObject("animeList", rankModel.getAnimeInfoList());
        modelAndView.addObject("total", rankModel.getTotalCount());
        modelAndView.addObject("number", pageRequest.getPageNumber() + 1);
        return modelAndView;
    }


    @GetMapping("update")
    public ModelAndView update() {
        return new ModelAndView("update");
    }


    @GetMapping("recommend")
    public ModelAndView recommend() {
        return new ModelAndView("recommend");
    }

    @GetMapping("search")
    public ModelAndView search(
            @RequestParam("query") String query,
            @RequestParam("page") Integer page
    ) {
        return new ModelAndView("search");
    }

    @GetMapping("play/{movieId}")
    public ModelAndView play(
            @PathVariable(value = "movieId", required = false) String movieId,
            @RequestParam(value = "playid", required = false) Integer playid
    ) {
        return new ModelAndView("play");
    }

    @GetMapping("profile")
    public ModelAndView profile() {
        return new ModelAndView("profile");
    }

    @GetMapping("404")
    public ModelAndView err() {
        return new ModelAndView("404");
    }
}
