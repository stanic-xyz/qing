package chenyunlong.zhangli.controller.content;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoRankModel;
import chenyunlong.zhangli.model.vo.page.PlayInfoModel;
import chenyunlong.zhangli.service.AnimeInfoService;
import cn.hutool.json.JSONUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
        AnimeInfoVo animeInfo = animeInfoService.getMovieDetail(movieId);
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
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize
    ) {

        long totalCount = animeInfoService.getTotalCount(query);
        int totalPage = (int) Math.ceil(((double) totalCount) / ((double) pageSize));
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        List<AnimeInfo> animeInfos = animeInfoService.query(query, page, pageSize);
        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("animeInfos", animeInfos);
        modelAndView.addObject("query", query);
        modelAndView.addObject("totalCount", totalCount);
        modelAndView.addObject("currentIndex", page);
        modelAndView.addObject("totalPage", totalPage);
        return modelAndView;
    }

    @GetMapping("play/{movieId}")
    public ModelAndView play(
            @PathVariable(value = "movieId", required = true) String animeId,
            @RequestParam(value = "playid") String playid
    ) {
        String[] strings = playid.split("_");
        int type = 0;
        int ep = 0;
        if (strings.length == 2) {
            type = Integer.parseInt(strings[0]);
            ep = Integer.parseInt(strings[1]);
        }

        AnimeInfoVo animeInfoVo = animeInfoService.getPlayDetail(animeId);
        PlayInfoModel playInfoModel = new PlayInfoModel();
        playInfoModel.setAnimeInfo(animeInfoVo);
        ModelAndView modelAndView = new ModelAndView("play");
        modelAndView.addObject(playInfoModel);
        return modelAndView;
    }

    @ResponseBody
    @GetMapping("get_comments")
    public Object getComments() {
        String jsonStr = "{\"AllCnt\":121,\"comments\":[{\"sid\":320522,\"content\":\"刀剑 任何一季 都没有 刀剑外传GGO好看 人设造型缺乏特点 魅力 剧本乏善可陈 反观GGO仅仅12集 却看得荡气回肠 人设立体造型鲜明 剧本有张力 大呼过瘾 而刀剑全三季是流水帐一样的动画\",\"time\":1598710393,\"username\":\"游客1140880176018\",\"cid\":\"20170140\"},{\"sid\":320335,\"content\":\"秒杀桐人，我笑了\",\"time\":1598702633,\"username\":\"游客11701520156059\",\"cid\":\"20170140\"},{\"sid\":317906,\"content\":\"1\",\"time\":1598583544,\"username\":\"游客17104201570183\",\"cid\":\"20170140\"},{\"sid\":314485,\"content\":\"最后成了人生淫家……\",\"time\":1598347924,\"username\":\"游客110019090050\",\"cid\":\"20170140\"},{\"sid\":314418,\"content\":\"前期很惊艳，后期很迷惑……\",\"time\":1598343672,\"username\":\"游客110019090050\",\"cid\":\"20170140\"},{\"sid\":311914,\"content\":\"藏在侧躺的尸体背后……这主角身高怕是没50厘米\",\"time\":1598234765,\"username\":\"游客580180280134\",\"cid\":\"20170140\"},{\"sid\":311905,\"content\":\"为啥没世界清静模式\",\"time\":1598234161,\"username\":\"游客580180280134\",\"cid\":\"20170140\"}],\"PageCtrl\":[{\"Title\":\"首页\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=-1\",\"Index\":-1},{\"Title\":\"1\",\"Url\":\"\",\"Index\":-1},{\"Title\":\"2\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=1\",\"Index\":1},{\"Title\":\"3\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=2\",\"Index\":2},{\"Title\":\"4\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=3\",\"Index\":3},{\"Title\":\"5\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=4\",\"Index\":4},{\"Title\":\"6\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=5\",\"Index\":5},{\"Title\":\"下一页\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=1\",\"Index\":1},{\"Title\":\"尾页\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=17\",\"Index\":17}],\"html_pageurls\":\"\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton pbutton_current asciifont\\\" href=\\\"javascript:void(0)\\\"\\u003e首页\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton pbutton_current asciifont\\\" href=\\\"javascript:void(0)\\\"\\u003e1\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(1)\\\"\\u003e2\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(2)\\\"\\u003e3\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(3)\\\"\\u003e4\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(4)\\\"\\u003e5\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(5)\\\"\\u003e6\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(1)\\\"\\u003e下一页\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(17)\\\"\\u003e尾页\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\"}";
        return JSONUtil.parse(jsonStr).toStringPretty();
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