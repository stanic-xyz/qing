package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
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
