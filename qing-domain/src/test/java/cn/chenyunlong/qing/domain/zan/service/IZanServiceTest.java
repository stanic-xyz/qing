package cn.chenyunlong.qing.domain.zan.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.user.User;
import cn.chenyunlong.qing.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.zan.creator.ZanCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class IZanServiceTest extends AbstractDomainTests {

    @Autowired
    private IZanService zanService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User entity = new User();
        userRepository.save(entity);
    }

    @Test
    @DisplayName("点赞！")
    void createZan_NoUser() {
        ZanCreator creator = new ZanCreator();
        creator.setUserId(1L);
        creator.setEntityId(2L);
        Assertions.assertThrows(BusinessException.class, () ->
        {
            Long userId = zanService.createZan(creator);
            Assertions.assertNotNull(userId);
        });
    }

    @Test
    @DisplayName("点赞！")
    void createZan() {
        ZanCreator creator = new ZanCreator();
        creator.setUserId(1L);
        creator.setEntityId(2L);
        Assertions.assertThrows(BusinessException.class, () ->
        {
            Long userId = zanService.createZan(creator);
            Assertions.assertNotNull(userId);
        });
    }
}
