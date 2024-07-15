package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.DistrictJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class DistrictRepositoryImpl extends JpaServiceImpl<DistrictJpaRepository, District, Long> implements DistrictRepository {

}
