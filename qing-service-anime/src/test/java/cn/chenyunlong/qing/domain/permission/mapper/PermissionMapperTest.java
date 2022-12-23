package cn.chenyunlong.qing.domain.permission.mapper;

import cn.chenyunlong.qing.domain.permission.Permission;
import cn.chenyunlong.qing.domain.permission.creator.PermissionCreator;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class PermissionMapperTest {

    @Test
    void dtoToEntity() {

        PermissionCreator permissionCreator = new PermissionCreator();
        permissionCreator.setDescription("写入");
        permissionCreator.setName("write");

        Permission permission = PermissionMapper.INSTANCE.dtoToEntity(permissionCreator);

        Assert.isTrue("write".equals(permission.getName()), "名称错误了");
        Assert.isTrue("写入".equals(permission.getDescription()), "描述信息错误");
    }
}
