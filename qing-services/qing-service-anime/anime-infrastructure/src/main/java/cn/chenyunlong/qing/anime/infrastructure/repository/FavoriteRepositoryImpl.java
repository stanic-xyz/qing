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

package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.favorite.Favorite;
import cn.chenyunlong.qing.anime.domain.favorite.FavoriteId;
import cn.chenyunlong.qing.anime.domain.favorite.repository.FavoriteRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.FavoriteEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.FavoriteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 收藏夹仓储实现类
 * 使用JPA数据库实现
 *
 * @author 陈云龙
 */
@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepository {

    private final FavoriteJpaRepository favoriteJpaRepository;

    @Override
    public Favorite save(Favorite favorite) {
        FavoriteEntity entity = toEntity(favorite);
        FavoriteEntity savedEntity = favoriteJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Favorite> findByUserIdAndAnimeId(Long userId, Long animeId) {
        return favoriteJpaRepository.findByUserIdAndAnimeId(userId, animeId)
                .map(this::toDomain);
    }

    @Override
    public List<Favorite> findByUserId(Long userId) {
        return favoriteJpaRepository.findByUserIdOrderByFavoriteTimeDesc(userId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Favorite> findByUserIdAndFavoriteGroup(Long userId, String favoriteGroup) {
        return favoriteJpaRepository.findByUserIdAndFavoriteGroupOrderByFavoriteTimeDesc(userId, favoriteGroup)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findFavoriteGroupsByUserId(Long userId) {
        return favoriteJpaRepository.findDistinctFavoriteGroupsByUserId(userId);
    }

    @Override
    public Long countByAnimeId(Long animeId) {
        return favoriteJpaRepository.countByAnimeId(animeId);
    }

    @Override
    public Boolean existsByUserIdAndAnimeId(Long userId, Long animeId) {
        return favoriteJpaRepository.existsByUserIdAndAnimeId(userId, animeId);
    }

    @Override
    public void deleteById(Long id) {
        favoriteJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByUserIdAndAnimeId(Long userId, Long animeId) {
        favoriteJpaRepository.deleteByUserIdAndAnimeId(userId, animeId);
    }

    private FavoriteEntity toEntity(Favorite favorite) {
        FavoriteEntity entity = new FavoriteEntity();
        if (favorite.getId() != null) {
            entity.setId(favorite.getId().id());
        }
        entity.setUserId(favorite.getUserId());
        entity.setAnimeId(favorite.getAnimeId());
        entity.setFavoriteTime(favorite.getFavoriteTime());
        entity.setFavoriteGroup(favorite.getFavoriteGroup());
        entity.setRemark(favorite.getRemark());
        entity.setIsPublic(favorite.getIsPublic());
        return entity;
    }

    private Favorite toDomain(FavoriteEntity entity) {
        Favorite favorite = Favorite.create(
            entity.getUserId(),
            entity.getAnimeId(),
            entity.getFavoriteGroup(),
            entity.getRemark(),
            entity.getIsPublic()
        );
        favorite.setId(FavoriteId.of(entity.getId()));
        favorite.setFavoriteTime(entity.getFavoriteTime());
        return favorite;
    }
}
