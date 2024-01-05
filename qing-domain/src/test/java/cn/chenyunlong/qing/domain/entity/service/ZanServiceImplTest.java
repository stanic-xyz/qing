package cn.chenyunlong.qing.domain.entity.service;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.entity.repository.EntityRepository;
import cn.chenyunlong.qing.domain.zan.Zan;
import cn.chenyunlong.qing.domain.zan.dto.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.repository.ZanRepository;
import cn.chenyunlong.qing.domain.zan.service.impl.ZanServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
class ZanServiceImplTest {

    private ZanServiceImpl zanService;
    private EntityRepository entityRepository;
    private UserRepository userRepository;
    private ZanRepository zanRepository;

    @BeforeEach
    void setUp() {
        entityRepository = mock(EntityRepository.class);
        userRepository = mock(UserRepository.class);
        zanRepository = mock(ZanRepository.class);
        zanService =
            new ZanServiceImpl(zanRepository, entityRepository, userRepository, null, null, null);
    }

    @Test
    void testCreateZan_ThrowsBusinessExceptionWhenEntityNotExists() {
        ZanCreator creator = new ZanCreator();
        creator.setEntityId(1L);
        creator.setUserId(2L);

        doReturn(Optional.empty()).when(entityRepository).findById(creator.getEntityId());
        assertThrows(BusinessException.class, () -> zanService.createZan(creator));
    }

    @Test
    void testCreateZan_ThrowsBusinessExceptionWhenUserNotExists() {
        ZanCreator creator = new ZanCreator();
        creator.setEntityId(1L);
        creator.setUserId(2L);

        doReturn(Optional.empty()).when(userRepository).findById(creator.getUserId());
        assertThrows(BusinessException.class, () -> zanService.createZan(creator));
    }

    @Test
    void testCreateZan_ReturnsZanIdWhenSuccessful() {
        ZanCreator creator = new ZanCreator();
        creator.setEntityId(1L);
        creator.setUserId(2L);

        QingUser qingUser = new QingUser();
        qingUser.setId(2L);

        doReturn(Optional.of(new Entity())).when(entityRepository).findById(creator.getEntityId());
        doReturn(Optional.of(new QingUser())).when(userRepository).findById(creator.getUserId());
        doReturn(Optional.of(new Zan())).when(zanRepository).findById(anyLong());
        doAnswer(answer -> {
            Object argument = answer.getArgument(0);
            ((Zan) argument).setValidStatus(ValidStatus.VALID);
            Zan zan = new Zan();
            zan.setId(1L);
            return zan;
        }).when(zanRepository).save(any());
        long zanId = zanService.createZan(creator);
        assertEquals(1, zanId);
    }

    @Test
    void testCreateZan_ReturnsZeroWhenZanNotCreated() {
        ZanCreator creator = new ZanCreator();
        creator.setEntityId(1L);
        creator.setUserId(2L);

        doReturn(Optional.of(new Entity())).when(entityRepository).findById(creator.getEntityId());
        doReturn(Optional.of(new QingUser())).when(userRepository).findById(creator.getUserId());
        doReturn(Optional.empty()).when(zanRepository).findById(anyLong());
        doAnswer(answer -> {
            Object argument = answer.getArgument(0);
            ((Zan) argument).setValidStatus(ValidStatus.VALID);
            Zan zan = new Zan();
            zan.setId(1L);
            return zan;
        }).when(zanRepository).save(any());
        long zanId = zanService.createZan(creator);
        assertEquals(1, zanId);
    }
}
