package cn.chenyunlong.qing.domain.type.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.type.Type;
import cn.chenyunlong.qing.domain.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.domain.type.dto.query.TypeQuery;
import cn.chenyunlong.qing.domain.type.dto.updater.TypeUpdater;
import cn.chenyunlong.qing.domain.type.dto.vo.TypeVO;
import cn.chenyunlong.qing.domain.type.mapper.TypeMapper;
import cn.chenyunlong.qing.domain.type.repository.TypeRepository;
import cn.chenyunlong.qing.domain.type.service.ITypeService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements ITypeService {
    private final TypeRepository typeRepository;

    /**
     * createImpl
     */
    @Override
    public Long createType(TypeCreator creator) {
        Optional<Type> type = EntityOperations.doCreate(typeRepository)
            .create(() -> TypeMapper.INSTANCE.dtoToEntity(creator))
            .update(Type::init)
            .execute();
        return type.isPresent() ? type.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateType(TypeUpdater updater) {
        EntityOperations.doUpdate(typeRepository)
            .loadById(updater.getId())
            .update(updater::updateType)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validType(Long id) {
        EntityOperations.doUpdate(typeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidType(Long id) {
        EntityOperations.doUpdate(typeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public TypeVO findById(Long id) {
        Optional<Type> type = typeRepository.findById(id);
        return new TypeVO(type.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<TypeVO> findByPage(PageRequestWrapper<TypeQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return typeRepository.findAll(pageRequest).map(TypeVO::new);
    }
}
