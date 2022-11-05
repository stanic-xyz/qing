/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.qing.controller.content;

import cn.chenyunlong.qing.controller.content.model.AnimeInfoModel;
import cn.chenyunlong.qing.core.ApiResult;
import cn.chenyunlong.qing.model.dto.anime.AnimeInfoMinimalDTO;
import cn.chenyunlong.qing.model.dto.anime.AnimeInfoRankDTO;
import cn.chenyunlong.qing.model.entities.AnimeComment;
import cn.chenyunlong.qing.model.params.AnimeInfoQuery;
import cn.chenyunlong.qing.model.vo.page.*;
import cn.chenyunlong.qing.service.AnimeInfoService;
import cn.chenyunlong.qing.service.ReportService;
import cn.chenyunlong.qing.utils.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 前端控制器
 *
 * @author Stan
 * @date 2022/11/05
 */
@Hidden
@Tag(name = "前端界面")
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MovieController {

    private final AnimeInfoService animeInfoService;
    private final AnimeInfoModel animeInfoModel;
    private final ReportService reportService;

    @GetMapping(path = {"", "/index.html"})
    public ModelAndView index(ModelAndView modelAndView) throws JsonProcessingException, InterruptedException {
        IndexModel indexModel = animeInfoModel.getIndex();
        modelAndView.setViewName("home");
        modelAndView.addObject("data", indexModel);
        return modelAndView;
    }

    @GetMapping({"detail/{movieId}", "detail/{movieId}/index.html"})
    public ModelAndView movie(@PathVariable(value = "movieId") Long animeId) {
        DetailModel detailModel = animeInfoModel.detail(animeId);
        ModelAndView modelAndView = new ModelAndView("detail");
        modelAndView.addObject("data", detailModel);
        return modelAndView;
    }

    @GetMapping({"catalog", "catalog.html"})
    public ModelAndView catalog(AnimeInfoQuery animeInfoQuery,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "24") Integer pageSize) {

        ModelAndView modelAndView = new ModelAndView();
        CatalogModel catalogModel = animeInfoModel.listCatalog(new Page<>(page, pageSize), animeInfoQuery);
        modelAndView.setViewName("catalog");
        modelAndView.addObject("data", catalogModel);
        return modelAndView;
    }

    @GetMapping({"play/{movieId}", "play/{movieId}/index.html"})
    public ModelAndView play(
            @PathVariable(value = "movieId") Long animeId,
            @RequestParam(value = "playid", required = false) Long playId) {

        PlayModel playModel = animeInfoModel.play(animeId, playId);
        ModelAndView modelAndView = new ModelAndView("play");
        modelAndView.addObject("data", playModel);
        return modelAndView;
    }

    @GetMapping({"rank", "rank.html"})
    public ModelAndView rank(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "75") Integer size,
            @RequestParam(value = "year", required = false) String catYear,
            AnimeInfoQuery animeInfoQuery) {
        IPage<AnimeInfoRankDTO> rankPage = animeInfoService.getRankPage(new Page<>(page, size), animeInfoQuery);
        ModelAndView modelAndView = new ModelAndView("rank");
        modelAndView.addObject("animeList", rankPage.getRecords());
        modelAndView.addObject("total", rankPage.getTotal());
        modelAndView.addObject("number", rankPage.getCurrent());
        return modelAndView;
    }


    @GetMapping({"update", "update.html"})
    public ModelAndView update(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "24") Integer pageSize) {
        ModelAndView modelAndView = new ModelAndView("update");
        UpdateModel updateModel = animeInfoModel.listUpdate(page, pageSize);
        modelAndView.addObject("data", updateModel);
        return modelAndView;
    }


    @GetMapping({"recommend", "recommend.html"})
    public ModelAndView recommend(ModelAndView modelAndView) {
        List<AnimeInfoMinimalDTO> animeInfoList = animeInfoService.getRecommendAnimeInfoList();
        modelAndView.setViewName("recommend");
        modelAndView.addObject("animeInfos", animeInfoList);
        return modelAndView;
    }

    @GetMapping({"search", "search.html"})
    public ModelAndView search(
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            AnimeInfoQuery searchParam) {

        SearchModel searchModel = animeInfoModel.searchModel(new Page<>(page, pageSize), searchParam);
        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("data", searchModel);
        return modelAndView;
    }

    @ResponseBody
    @GetMapping("get_comments")
    public Object getComments(@RequestParam("cid") Long cid,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex) {
        IPage<AnimeComment> commentPage = animeInfoService.getComment(cid, pageIndex, pageSize);
        String jsonStr = "{\"AllCnt\":121,\"comments\":[{\"sid\":320522,\"content\":\"刀剑 任何一季 都没有 刀剑外传GGO好看 人设造型缺乏特点 魅力 剧本乏善可陈 反观GGO仅仅12集 却看得荡气回肠 人设立体造型鲜明 剧本有张力 大呼过瘾 而刀剑全三季是流水帐一样的动画\",\"time\":1598710393,\"username\":\"游客1140880176018\",\"cid\":\"20170140\"},{\"sid\":320335,\"content\":\"秒杀桐人，我笑了\",\"time\":1598702633,\"username\":\"游客11701520156059\",\"cid\":\"20170140\"},{\"sid\":317906,\"content\":\"1\",\"time\":1598583544,\"username\":\"游客17104201570183\",\"cid\":\"20170140\"},{\"sid\":314485,\"content\":\"最后成了人生淫家……\",\"time\":1598347924,\"username\":\"游客110019090050\",\"cid\":\"20170140\"},{\"sid\":314418,\"content\":\"前期很惊艳，后期很迷惑……\",\"time\":1598343672,\"username\":\"游客110019090050\",\"cid\":\"20170140\"},{\"sid\":311914,\"content\":\"藏在侧躺的尸体背后……这主角身高怕是没50厘米\",\"time\":1598234765,\"username\":\"游客580180280134\",\"cid\":\"20170140\"},{\"sid\":311905,\"content\":\"为啥没世界清静模式\",\"time\":1598234161,\"username\":\"游客580180280134\",\"cid\":\"20170140\"}],\"PageCtrl\":[{\"Title\":\"首页\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=-1\",\"Index\":-1},{\"Title\":\"1\",\"Url\":\"\",\"Index\":-1},{\"Title\":\"2\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=1\",\"Index\":1},{\"Title\":\"3\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=2\",\"Index\":2},{\"Title\":\"4\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=3\",\"Index\":3},{\"Title\":\"5\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=4\",\"Index\":4},{\"Title\":\"6\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=5\",\"Index\":5},{\"Title\":\"下一页\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=1\",\"Index\":1},{\"Title\":\"尾页\",\"Url\":\"/get_comments?cid=20170140\\u0026pageindex=17\",\"Index\":17}],\"html_pageurls\":\"\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton pbutton_current asciifont\\\" href=\\\"javascript:void(0)\\\"\\u003e首页\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton pbutton_current asciifont\\\" href=\\\"javascript:void(0)\\\"\\u003e1\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(1)\\\"\\u003e2\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(2)\\\"\\u003e3\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(3)\\\"\\u003e4\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(4)\\\"\\u003e5\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(5)\\\"\\u003e6\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(1)\\\"\\u003e下一页\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\\n\\t\\t\\t\\u003cli\\u003e\\u003ca class=\\\"pbutton  asciifont\\\" href=\\\"javascript:void(0)\\\" onclick=\\\"__age_show_comments_page(17)\\\"\\u003e尾页\\u003c/a\\u003e\\u003c/li\\u003e\\n\\t\\t\\t\"}";
        return ApiResult.success(commentPage);
    }

    @ResponseBody
    @GetMapping("send_comment")
    public ApiResult<Void> sendComments(@RequestParam("cid") Long cid,
                                        @RequestParam(value = "comment_content", defaultValue = "10") String content,
                                        @RequestParam(value = "comment_user", defaultValue = "1") String user) {
        animeInfoService.addComment(cid, content, user);
        return ApiResult.success();
    }

    @ResponseBody
    @GetMapping("report")
    public ApiResult<Void> report(@RequestParam("cid") Long cid,
                                  @RequestParam(value = "link_invalid", defaultValue = "0", required = false) Boolean linkInvalid,
                                  @RequestParam(value = "bad_quality", defaultValue = "0", required = false) Boolean badQuality,
                                  @RequestParam(value = "some_missing", defaultValue = "0", required = false) Boolean someMissing,
                                  @RequestParam(value = "detail", defaultValue = "", required = false) String detail,
                                  @RequestParam(value = "username", defaultValue = "admin", required = false) String username) {
        if (!linkInvalid && !badQuality & !someMissing && !StringUtils.isEmpty(detail)) {
            return ApiResult.fail("参数不能为空");
        }
        reportService.addReport(username, cid, linkInvalid, someMissing, badQuality, detail);
        return ApiResult.success();
    }

    @GetMapping({"login", "login.html"})
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping({"profile", "profile.html"})
    public ModelAndView profile() {
        return new ModelAndView("profile");
    }

    @GetMapping({"404", "404.html"})
    public ModelAndView nul() {
        return new ModelAndView("404");
    }

    @GetMapping({"link/{animeId}/{epsodeId}", "link/{animeId}/{epsodeId}/index.html"})
    public ModelAndView link(@PathVariable(value = "animeId") Long animeId,
                             @PathVariable(value = "epsodeId") Long epsodeId) {
        return new ModelAndView("redirect:https://pan.baidu.com/share/init?surl=Vjn9aJ2IY3bJlpO4X8H9kg");
    }
}
