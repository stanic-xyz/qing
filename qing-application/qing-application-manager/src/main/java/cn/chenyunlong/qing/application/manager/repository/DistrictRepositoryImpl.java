package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.DistrictJpaRepository;
import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
import org.springframework.stereotype.Service;

@Service
public class DistrictRepositoryImpl extends JpaServiceImpl<DistrictJpaRepository, District, Long> implements DistrictRepository {

}
