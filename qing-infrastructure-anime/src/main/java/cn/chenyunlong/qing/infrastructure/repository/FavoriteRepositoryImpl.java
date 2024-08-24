package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.faverate.Favorite;
import cn.chenyunlong.qing.domain.anime.faverate.repository.FavoriteRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.FavoriteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteRepositoryImpl extends JpaServiceImpl<FavoriteJpaRepository, Favorite, Long> implements FavoriteRepository {


    private final FavoriteJpaRepository animeJpaRepository;

}
