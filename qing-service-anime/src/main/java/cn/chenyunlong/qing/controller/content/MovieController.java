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

import cn.chenyunlong.qing.infrastructure.model.params.AnimeInfoQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

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


    @GetMapping(path = {"", "/index.html"})
    public ModelAndView index(ModelAndView modelAndView) throws JsonProcessingException, InterruptedException {
        modelAndView.setViewName("home");
        modelAndView.addObject("data", null);
        return modelAndView;
    }

    @GetMapping("anime/{movieId}/index.html")
    public ModelAndView movie(@PathVariable(value = "movieId") Long animeId) {
        ModelAndView modelAndView = new ModelAndView("detail");
        modelAndView.addObject("data", null);
        return modelAndView;
    }

    @GetMapping("anime/{movieId}/play/{listId}/{playId}/index.html")
    public ModelAndView play(@PathVariable(value = "movieId") Long animeId,
                             @PathVariable(value = "listId") Long listId,
                             @PathVariable(value = "playId") Long playId) {
        ModelAndView modelAndView = new ModelAndView("play");
        modelAndView.addObject("data", null);
        return modelAndView;
    }

    @GetMapping("catalog/index.html")
    public ModelAndView catalog(AnimeInfoQuery animeInfoQuery,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "24") Integer pageSize) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("catalog");
        modelAndView.addObject("data", null);
        return modelAndView;
    }

    @GetMapping("rank/index.html")
    public ModelAndView rank(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "75") Integer size,
            @RequestParam(value = "year", required = false) String catYear,
            AnimeInfoQuery animeInfoQuery) {
        ModelAndView modelAndView = new ModelAndView("rank");
        modelAndView.addObject("animeList", Collections.emptyList());
        modelAndView.addObject("total", Collections.emptyList());
        modelAndView.addObject("number", Collections.emptyList());
        return modelAndView;
    }

    @GetMapping("update/index.html")
    public ModelAndView update(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "24") Integer pageSize) {
        ModelAndView modelAndView = new ModelAndView("update");
        modelAndView.addObject("data", null);
        return modelAndView;
    }


    @GetMapping("recommend/index.html")
    public ModelAndView recommend(ModelAndView modelAndView) {
        modelAndView.setViewName("recommend");
        modelAndView.addObject("animeInfos", null);
        return modelAndView;
    }

    @GetMapping("search/index.html")
    public ModelAndView search(
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            AnimeInfoQuery searchParam) {

        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("data", null);
        return modelAndView;
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
