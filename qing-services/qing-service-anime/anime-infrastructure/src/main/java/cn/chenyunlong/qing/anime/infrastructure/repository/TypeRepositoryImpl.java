package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.type.Type;
import cn.chenyunlong.qing.anime.domain.type.repository.TypeRepository;
import cn.chenyunlong.qing.anime.infrastructure.converter.TypeMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.TypeJpaRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeRepositoryImpl implements TypeRepository {

    private final TypeJpaRepository typeJpaRepository;

    @Override
    public Type save(Type entity) {
        return null;
    }

    @Override
    public Optional<Type> findById(AggregateId id) {
        if (id == null) {
            return Optional.empty();
        }
        return typeJpaRepository.findById(id.getId()).map(TypeMapper.INSTANCE::entityToDomain);
    }
}
