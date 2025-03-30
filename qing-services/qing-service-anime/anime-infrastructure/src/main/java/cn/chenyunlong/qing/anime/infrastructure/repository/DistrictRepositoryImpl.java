package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.district.District;
import cn.chenyunlong.qing.anime.domain.district.repository.DistrictRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DistrictRepositoryImpl implements DistrictRepository {

    @Override
    public District save(District entity) {
        return null;
    }

    @Override
    public Optional<District> findById(AggregateId id) {
        return Optional.empty();
    }
}
