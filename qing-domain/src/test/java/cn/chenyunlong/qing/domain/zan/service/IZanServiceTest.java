package cn.chenyunlong.qing.domain.zan.service;

import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.service.IUserService;
import cn.chenyunlong.qing.domain.entity.EntityType;
import cn.chenyunlong.qing.domain.entity.dto.creator.EntityCreator;
import cn.chenyunlong.qing.domain.entity.service.IEntityService;
import cn.chenyunlong.qing.domain.zan.dto.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.dto.vo.ZanVO;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

class IZanServiceTest extends AbstractDomainTests {

    @Autowired
    private IZanService zanService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IEntityService entityService;

    @Rollback
    @Test
    @DisplayName("点赞！")
    void createZan_NoUser() {
        ZanCreator creator = new ZanCreator();
        creator.setUserId(1L);
        creator.setEntityId(2L);
        Assertions.assertThrows(BusinessException.class, () ->
        {
            // 用户不存在，调用点赞接口会报错！
            Long userId = zanService.createZan(creator);
            Assertions.assertNotNull(userId);
        });
    }

    @Rollback
    @Test
    @DisplayName("点赞！")
    void createZan() {
        // 创建一个用户
        EntityCreator entityCreator = new EntityCreator();
        entityCreator.setEntityType(EntityType.USER);
        entityCreator.setName("华为");
        Long entityId = entityService.createEntity(entityCreator);
        Assertions.assertNotNull(entityId);

        UserCreator userCreator = new UserCreator();
        userCreator.setAvatar(RandomUtil.randomString(100));
        userCreator.setDescription(RandomUtil.randomString(100));
        userCreator.setEmail(RandomUtil.randomString(50));
        userCreator.setNickname(RandomUtil.randomString(15));
        userCreator.setPassword(RandomUtil.randomString(16));
        userCreator.setMfaKey(MFAType.TFA_TOTP.getName());
        Long userId = userService.register(userCreator);
        Assertions.assertNotNull(userId);


        ZanCreator creator = new ZanCreator();
        creator.setUserId(userId);
        creator.setEntityId(entityId);
        Long zanId = zanService.createZan(creator);
        Assertions.assertNotNull(zanId);

        ZanVO byId = zanService.findById(zanId);

        Assertions.assertEquals(userId, byId.getUserId());
        Assertions.assertEquals(userId, byId.getUserId());

    }

    @DisplayName("测试 invalid 方法")
    @Rollback
    @Test
    void invalidZan() {
        // 创建一个用户
        EntityCreator entityCreator = new EntityCreator();
        entityCreator.setEntityType(EntityType.USER);
        entityCreator.setName("华为");
        Long entityId = entityService.createEntity(entityCreator);
        Assertions.assertNotNull(entityId);

        UserCreator userCreator = new UserCreator();
        userCreator.setAvatar(RandomUtil.randomString(100));
        userCreator.setDescription(RandomUtil.randomString(100));
        userCreator.setEmail(RandomUtil.randomString(50));
        userCreator.setNickname(RandomUtil.randomString(15));
        userCreator.setPassword(RandomUtil.randomString(16));
        userCreator.setMfaKey(MFAType.TFA_TOTP.getName());
        Long userId = userService.register(userCreator);
        Assertions.assertNotNull(userId);

        ZanCreator creator = new ZanCreator();
        creator.setUserId(userId);
        creator.setEntityId(entityId);
        Long zanId = zanService.createZan(creator);
        Assertions.assertNotNull(zanId);
        ZanVO byId = zanService.findById(zanId);

        Assertions.assertEquals(userId, byId.getUserId());
        Assertions.assertEquals(userId, byId.getUserId());

        zanService.invalidZan(zanId);
        ZanVO zanVO = zanService.findById(zanId);
        Assertions.assertNotNull(zanVO);
    }

    @Rollback
    @Test
    @DisplayName("测试 Pipeline 和 Spring Plugin")
    void like() {
        // 创建一个用户
        EntityCreator entityCreator = new EntityCreator();
        entityCreator.setEntityType(EntityType.USER);
        entityCreator.setName("华为");
        Long entityId = entityService.createEntity(entityCreator);
        Assertions.assertNotNull(entityId);

        UserCreator userCreator = new UserCreator();
        userCreator.setAvatar(RandomUtil.randomString(100));
        userCreator.setDescription(RandomUtil.randomString(100));
        userCreator.setEmail(RandomUtil.randomString(50));
        userCreator.setNickname(RandomUtil.randomString(15));
        userCreator.setPassword(RandomUtil.randomString(16));
        userCreator.setMfaKey(MFAType.TFA_TOTP.getName());
        Long userId = userService.register(userCreator);
        Assertions.assertNotNull(userId);

        ZanCreateRequest creator = new ZanCreateRequest();
        creator.setUserId(userId);
        creator.setEntityId(entityId);
        Long like = zanService.like(creator);
        Assertions.assertNotNull(like);
    }
}
