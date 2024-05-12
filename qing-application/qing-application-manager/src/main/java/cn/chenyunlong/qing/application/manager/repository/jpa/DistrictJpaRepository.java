package cn.chenyunlong.qing.application.manager.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.district.District;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictJpaRepository extends BaseJpaRepository<District, Long> {

}
