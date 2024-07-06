package cn.chenyunlong.qing.infrastructure.repository;


import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.AnimeCategoryJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimeCategoryRepositoryImpl extends JpaServiceImpl<AnimeCategoryJpaRepository, AnimeCategory, Long> implements AnimeCategoryRepository {

}
