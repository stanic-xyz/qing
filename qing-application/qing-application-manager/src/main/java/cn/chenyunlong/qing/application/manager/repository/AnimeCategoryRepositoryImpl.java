package cn.chenyunlong.qing.application.manager.repository;


import cn.chenyunlong.qing.application.manager.repository.jpa.AnimeCategoryJpaRepository;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeCategoryRepositoryImpl extends JpaServiceImpl<AnimeCategoryJpaRepository, AnimeCategory, Long> implements AnimeCategoryRepository {

}
