package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.anime.AnimeTagRel;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeTagRelRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimeTagRelRepositoryImpl extends JpaServiceImpl<BaseJpaRepository<AnimeTagRel, Long>, AnimeTagRel, Long> implements AnimeTagRelRepository {

}
