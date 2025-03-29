package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.DistrictEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictJpaRepository extends BaseJpaQueryRepository<DistrictEntity, Long> {

}
