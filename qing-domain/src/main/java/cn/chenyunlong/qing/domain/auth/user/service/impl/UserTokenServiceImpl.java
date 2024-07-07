package cn.chenyunlong.qing.domain.auth.user.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.user.UserToken;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserTokenQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserTokenUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserTokenVO;
import cn.chenyunlong.qing.domain.auth.user.mapper.UserTokenMapper;
import cn.chenyunlong.qing.domain.auth.user.repository.UserTokenRepository;
import cn.chenyunlong.qing.domain.auth.user.service.IUserTokenService;
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
public class UserTokenServiceImpl implements IUserTokenService {

    private final UserTokenRepository userTokenRepository;

    /**
     * createImpl
     */
    @Override
    public Long createUserToken(UserTokenCreator creator) {
        Optional<UserToken> userToken = EntityOperations.doCreate(userTokenRepository)
            .create(() -> UserTokenMapper.INSTANCE.dtoToEntity(creator))
            .update(UserToken::init)
            .execute();
        return userToken.isPresent() ? userToken.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateUserToken(UserTokenUpdater updater) {
        EntityOperations.doUpdate(userTokenRepository)
            .loadById(updater.getId())
            .update(updater::updateUserToken)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validUserToken(Long id) {
        EntityOperations.doUpdate(userTokenRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidUserToken(Long id) {
        EntityOperations.doUpdate(userTokenRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public UserTokenVO findById(Long id) {
        Optional<UserToken> userToken = userTokenRepository.findById(id);
        return new UserTokenVO(
            userToken.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<UserTokenVO> findByPage(PageRequestWrapper<UserTokenQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return userTokenRepository.findAll(pageRequest).map(UserTokenVO::new);
    }
}
