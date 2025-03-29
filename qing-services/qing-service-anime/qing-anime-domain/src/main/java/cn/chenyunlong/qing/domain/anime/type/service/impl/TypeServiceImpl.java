package cn.chenyunlong.qing.domain.anime.type.service.impl;

import cn.chenyunlong.qing.domain.anime.type.Type;
import cn.chenyunlong.qing.domain.anime.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.domain.anime.type.mapper.TypeMapper;
import cn.chenyunlong.qing.domain.anime.type.repository.TypeRepository;
import cn.chenyunlong.qing.domain.anime.type.service.ITypeService;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements ITypeService {

    private final TypeRepository typeRepository;

    @Override
    public Long createType(TypeCreator creator) {
        Optional<Type> type = EntityOperations.doCreate(typeRepository)
            .create(() -> TypeMapper.INSTANCE.dtoToEntity(creator))
            .update(Type::init)
            .execute();
        return type.isPresent() ? type.get().getAggregateId().getId() : 0;
    }

    @Override
    public void validType(Long id) {
        EntityOperations.doUpdate(typeRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidType(Long id) {
        EntityOperations.doUpdate(typeRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }
}
