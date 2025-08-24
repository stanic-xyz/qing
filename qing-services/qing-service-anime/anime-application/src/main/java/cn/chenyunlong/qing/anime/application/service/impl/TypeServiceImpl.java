package cn.chenyunlong.qing.anime.application.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.chenyunlong.qing.anime.application.service.ITypeService;
import cn.chenyunlong.qing.anime.domain.type.Type;
import cn.chenyunlong.qing.anime.domain.type.TypeId;
import cn.chenyunlong.qing.anime.domain.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.anime.domain.type.repository.TypeRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements ITypeService {

    private final TypeRepository typeRepository;

    @Override
    public Long createType(TypeCreator creator) {
        Optional<Type> type = EntityOperations.doCreate(typeRepository)
                .create(() -> dtoToEntity(creator))
                .update(Type::init)
                .execute();
        return type.isPresent() ? type.get().getId().getValue() : 0;
    }

    private Type dtoToEntity(TypeCreator creator) {
        Type type = new Type();
        type.setName(creator.getName());
        type.setInstruction(creator.getInstruction());
        return type;
    }

    @Override
    public void validType(Long id) {
        EntityOperations.doUpdate(typeRepository)
                .loadById(TypeId.of(id))
                .update(BaseAggregate::valid)
                .execute();
    }

    @Override
    public void invalidType(Long id) {
        EntityOperations.doUpdate(typeRepository)
                .loadById(TypeId.of(id))
                .update(BaseAggregate::invalid)
                .execute();
    }
}
