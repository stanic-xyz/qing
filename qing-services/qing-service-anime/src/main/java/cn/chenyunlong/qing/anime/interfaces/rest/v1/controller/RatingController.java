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

import cn.chenyunlong.qing.anime.application.service.RatingService;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.RatingAddRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.RatingLikeRequest;
import cn.chenyunlong.qing.anime.domain.rating.Rating;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
 * 评分评论控制器
 *
 * @author 陈云龙
 */
@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "评分评论管理", description = "评分评论相关接口")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/add")
    @Operation(summary = "添加评分评论", description = "用户对动漫进行评分和评论")
    public ResponseEntity<Rating> addRating(
            @Valid @RequestBody RatingAddRequest request) {

        try {
            Rating rating = ratingService.addRating(
                    request.getUserId(), request.getAnimeId(), request.getScore(),
                    request.getComment(), request.getIsAnonymous(), request.getIsPublic());
            return ResponseEntity.ok(rating);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-score")
    @Operation(summary = "更新评分", description = "更新用户对动漫的评分")
    public ResponseEntity<Void> updateScore(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId,
            @Parameter(description = "评分(1-10)") @RequestParam("score") @NotNull @Min(1) @Max(10) Integer score) {

        try {
            ratingService.updateScore(userId, animeId, score);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-comment")
    @Operation(summary = "更新评论", description = "更新用户对动漫的评论")
    public ResponseEntity<Void> updateComment(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId,
            @Parameter(description = "评论内容") @RequestParam("comment") @NotNull String comment) {

        try {
            ratingService.updateComment(userId, animeId, comment);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/like")
    @Operation(summary = "点赞评论", description = "为评论点赞")
    public ResponseEntity<Void> likeRating(
            @Valid @RequestBody RatingLikeRequest request) {

        try {
            ratingService.likeRating(request.getUserId(), request.getRatingId());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/unlike")
    @Operation(summary = "取消点赞", description = "取消对评论的点赞")
    public ResponseEntity<Void> unlikeRating(
            @Valid @RequestBody RatingLikeRequest request) {

        try {
            ratingService.unlikeRating(request.getUserId(), request.getRatingId());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/set-anonymous")
    @Operation(summary = "设置匿名状态", description = "设置评论的匿名状态")
    public ResponseEntity<Void> setAnonymousStatus(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId,
            @Parameter(description = "是否匿名") @RequestParam("isAnonymous") @NotNull Boolean isAnonymous) {

        try {
            ratingService.setAnonymousStatus(userId, animeId, isAnonymous);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/set-public")
    @Operation(summary = "设置公开状态", description = "设置评论的公开状态")
    public ResponseEntity<Void> setPublicStatus(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId,
            @Parameter(description = "是否公开") @RequestParam("isPublic") @NotNull Boolean isPublic) {

        try {
            ratingService.setPublicStatus(userId, animeId, isPublic);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除评分评论", description = "删除用户的评分评论")
    public ResponseEntity<Void> deleteRating(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId) {

        ratingService.deleteRating(userId, animeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/anime/{animeId}")
    @Operation(summary = "获取动漫评分列表", description = "获取动漫的所有公开评分")
    public ResponseEntity<List<Rating>> getAnimeRatings(
            @Parameter(description = "动漫ID") @PathVariable @NotNull @Positive Long animeId) {

        List<Rating> ratings = ratingService.getAnimeRatings(animeId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户评分列表", description = "获取用户的所有评分记录")
    public ResponseEntity<List<Rating>> getUserRatings(
            @Parameter(description = "用户ID") @PathVariable @NotNull @Positive Long userId) {

        List<Rating> ratings = ratingService.getUserRatings(userId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/user-anime")
    @Operation(summary = "获取用户对动漫的评分", description = "获取用户对某个动漫的评分")
    public ResponseEntity<Rating> getUserAnimeRating(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId) {

        Optional<Rating> rating = ratingService.getUserAnimeRating(userId, animeId);
        return rating.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/anime/{animeId}/average-score")
    @Operation(summary = "获取动漫平均评分", description = "计算动漫的平均评分")
    public ResponseEntity<Double> getAnimeAverageScore(
            @Parameter(description = "动漫ID") @PathVariable @NotNull @Positive Long animeId) {

        Double averageScore = ratingService.getAnimeAverageScore(animeId);
        return ResponseEntity.ok(averageScore != null ? averageScore : 0.0);
    }

    @GetMapping("/anime/{animeId}/rating-count")
    @Operation(summary = "获取动漫评分次数", description = "获取动漫的评分次数统计")
    public ResponseEntity<Long> getAnimeRatingCount(
            @Parameter(description = "动漫ID") @PathVariable @NotNull @Positive Long animeId) {

        Long count = ratingService.getAnimeRatingCount(animeId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/anime/{animeId}/popular-comments")
    @Operation(summary = "获取热门评论", description = "获取动漫的热门评论（按点赞数排序）")
    public ResponseEntity<List<Rating>> getPopularComments(
            @Parameter(description = "动漫ID") @PathVariable @NotNull @Positive Long animeId,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") @Positive int limit) {

        List<Rating> comments = ratingService.getPopularComments(animeId, limit);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/anime/{animeId}/latest-comments")
    @Operation(summary = "获取最新评论", description = "获取动漫的最新评论")
    public ResponseEntity<List<Rating>> getLatestComments(
            @Parameter(description = "动漫ID") @PathVariable @NotNull @Positive Long animeId,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") @Positive int limit) {

        List<Rating> comments = ratingService.getLatestComments(animeId, limit);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/check-rated")
    @Operation(summary = "检查评分状态", description = "检查用户是否已对动漫评分")
    public ResponseEntity<Boolean> hasUserRated(
            @Parameter(description = "用户ID") @RequestParam("userId") @NotNull @Positive Long userId,
            @Parameter(description = "动漫ID") @RequestParam("animeId") @NotNull @Positive Long animeId) {

        Boolean hasRated = ratingService.hasUserRated(userId, animeId);
        return ResponseEntity.ok(hasRated);
    }
}
