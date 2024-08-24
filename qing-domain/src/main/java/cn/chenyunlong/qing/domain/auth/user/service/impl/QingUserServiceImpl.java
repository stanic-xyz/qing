package cn.chenyunlong.qing.domain.auth.user.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.converter.SysUserMapper;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.QingUserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.QingUserQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.QingUserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.QingUserVO;
import cn.chenyunlong.qing.domain.auth.user.mapper.QingUserMapper;
import cn.chenyunlong.qing.domain.auth.user.repository.QingUserRepository;
import cn.chenyunlong.qing.domain.auth.user.service.IQingUserService;
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
public class QingUserServiceImpl implements IQingUserService {

    private final QingUserRepository qingUserRepository;

    /**
     * createImpl
     */
    @Override
    public Long createQingUser(QingUserCreator creator) {
        Optional<QingUser> qingUser = EntityOperations.doCreate(qingUserRepository)
                                          .create(() -> QingUserMapper.INSTANCE.dtoToEntity(creator))
                                          .update(QingUser::init)
                                          .execute();
        return qingUser.isPresent() ? qingUser.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateQingUser(QingUserUpdater updater) {
        EntityOperations.doUpdate(qingUserRepository)
            .loadById(updater.getId())
            .update(updater::updateQingUser)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validQingUser(Long id) {
        EntityOperations.doUpdate(qingUserRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidQingUser(Long id) {
        EntityOperations.doUpdate(qingUserRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public QingUserVO findById(Long id) {
        Optional<QingUser> qingUser = qingUserRepository.findById(id);
        return qingUser.map(SysUserMapper.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    /**
     * findByPage
     */
    @Override
    public Page<QingUserVO> findByPage(PageRequestWrapper<QingUserQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return qingUserRepository.findAll(pageRequest).map(SysUserMapper.INSTANCE::entityToVo);
    }
}
