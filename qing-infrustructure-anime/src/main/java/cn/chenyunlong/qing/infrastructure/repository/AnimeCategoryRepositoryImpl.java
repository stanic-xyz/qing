package cn.chenyunlong.qing.infrastructure.repository;


import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.infrastructure.repository.jpa.AnimeCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeCategoryRepositoryImpl extends JpaServiceImpl<AnimeCategoryJpaRepository, AnimeCategory, Long> implements AnimeCategoryRepository {

}
