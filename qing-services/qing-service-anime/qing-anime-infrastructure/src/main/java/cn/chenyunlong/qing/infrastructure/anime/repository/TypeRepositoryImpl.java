package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.anime.type.Type;
import cn.chenyunlong.qing.domain.anime.type.repository.TypeRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TypeRepositoryImpl implements TypeRepository {

    @Override
    public Type save(Type entity) {
        return null;
    }

    @Override
    public Optional<Type> findById(AggregateId id) {
        return Optional.empty();
    }
}
