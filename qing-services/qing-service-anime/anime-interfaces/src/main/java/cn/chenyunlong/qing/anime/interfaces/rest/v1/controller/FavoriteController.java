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

package cn.chenyunlong.qing.anime.interfaces.rest.v1.controller;

import cn.chenyunlong.qing.anime.application.service.FavoriteService;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.FavoriteAddRequest;
import cn.chenyunlong.qing.anime.domain.favorite.Favorite;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 收藏夹控制器
 *
 * @author 陈云龙
 */
@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "收藏夹管理", description = "收藏夹相关接口")
@Validated
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/add")
    @Operation(summary = "添加收藏", description = "将动漫添加到收藏夹")
    public ResponseEntity<Favorite> addToFavorite(
            @Valid @RequestBody FavoriteAddRequest request) {

        try {
            Favorite favorite = favoriteService.addToFavorite(
                    request.getUserId(), request.getAnimeId(), request.getFavoriteGroup(),
                    request.getRemark(), request.getIsPublic());
            return ResponseEntity.ok(favorite);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/remove")
    @Operation(summary = "取消收藏", description = "从收藏夹中移除动漫")
    public ResponseEntity<Void> removeFromFavorite(
            @Parameter(description = "用户ID", name = "userId") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId) {

        favoriteService.removeFromFavorite(userId, animeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-group")
    @Operation(summary = "更新收藏分组", description = "更新动漫的收藏分组")
    public ResponseEntity<Void> updateFavoriteGroup(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId,
            @Parameter(description = "收藏分组") @RequestParam("favoriteGroup") @NotNull String favoriteGroup) {

        try {
            favoriteService.updateFavoriteGroup(userId, animeId, favoriteGroup);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-remark")
    @Operation(summary = "更新收藏备注", description = "更新动漫的收藏备注")
    public ResponseEntity<Void> updateFavoriteRemark(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId,
            @Parameter(description = "备注") @RequestParam("remark") @NotNull String remark) {

        try {
            favoriteService.updateFavoriteRemark(userId, animeId, remark);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-public-status")
    @Operation(summary = "设置公开状态", description = "设置收藏的公开状态")
    public ResponseEntity<Void> setFavoritePublicStatus(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId,
            @Parameter(description = "是否公开") @RequestParam("isPublic") @NotNull Boolean isPublic) {

        try {
            favoriteService.setFavoritePublicStatus(userId, animeId, isPublic);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户收藏列表", description = "获取用户的所有收藏")
    public ResponseEntity<List<Favorite>> getUserFavorites(
            @Parameter(description = "用户ID") @PathVariable @NotNull @Positive Long userId) {

        List<Favorite> favorites = favoriteService.getUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/user/{userId}/group/{favoriteGroup}")
    @Operation(summary = "按分组获取收藏", description = "根据分组获取用户的收藏列表")
    public ResponseEntity<List<Favorite>> getUserFavoritesByGroup(
            @Parameter(description = "用户ID") @PathVariable @NotNull @Positive Long userId,
            @Parameter(description = "收藏分组") @PathVariable @NotNull String favoriteGroup) {

        List<Favorite> favorites = favoriteService.getUserFavoritesByGroup(userId, favoriteGroup);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/user/{userId}/groups")
    @Operation(summary = "获取收藏分组列表", description = "获取用户的收藏分组列表")
    public ResponseEntity<List<String>> getUserFavoriteGroups(
            @Parameter(description = "用户ID") @PathVariable @NotNull @Positive Long userId) {

        List<String> groups = favoriteService.getUserFavoriteGroups(userId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/check")
    @Operation(summary = "检查收藏状态", description = "检查用户是否已收藏某个动漫")
    public ResponseEntity<Boolean> isFavorited(
            @Parameter(description = "用户ID", name = "userId") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId) {

        Boolean isFavorited = favoriteService.isFavorited(userId, animeId);
        return ResponseEntity.ok(isFavorited);
    }

    @GetMapping("/anime/{animeId}/count")
    @Operation(summary = "获取动漫收藏次数", description = "获取动漫的收藏次数统计")
    public ResponseEntity<Long> getAnimeFavoriteCount(
            @Parameter(description = "动漫ID") @PathVariable @NotNull @Positive Long animeId) {

        Long count = favoriteService.getAnimeFavoriteCount(animeId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/detail")
    @Operation(summary = "获取收藏详情", description = "获取用户对某个动漫的收藏详情")
    public ResponseEntity<Favorite> getFavoriteDetail(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId) {

        Optional<Favorite> favorite = favoriteService.getFavoriteDetail(userId, animeId);
        return favorite.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
