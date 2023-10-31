package cn.chenyunlong.qing.domain.user.service;

import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.user.dto.creator.UserCreator;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class IUserServiceTest extends AbstractDomainTests {

    @Autowired
    private IUserService userService;

    @Rollback
    @Test
    @DisplayName("添加用户（注册）")
    public void createUser() {
        UserCreator userCreator = new UserCreator();
        userCreator.setAvatar(RandomUtil.randomString(100));
        userCreator.setDescription(RandomUtil.randomString(100));
        userCreator.setEmail(RandomUtil.randomString(50));
        userCreator.setNickname(RandomUtil.randomString(15));
        userCreator.setPassword(RandomUtil.randomString(16));
        userCreator.setMfaKey(MFAType.TFA_TOTP.getName());
        Long userId = userService.createUser(userCreator);
        Assertions.assertNotNull(userId);
    }
}
