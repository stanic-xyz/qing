package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.zan.Zan;
import cn.chenyunlong.qing.domain.zan.repository.ZanRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ZanRepositoryImpl implements ZanRepository {

    public Zan save(Zan entity) {
        return null;
    }

    @Override
    public Optional<Zan> findById(AggregateId id) {
        return Optional.empty();
    }

    public Optional<Zan> findById(Long id) {
        return Optional.empty();
    }
}
