package cn.chenyunlong.qing.auth.domain.rbac.permission;

import cn.chenyunlong.qing.auth.domain.rbac.Operator;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionStatus;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionType;
import cn.chenyunlong.qing.auth.domain.rbac.permission.event.PermissionStatusChangedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PermissionTest {

    @Test
    void updateStatus() {
        Permission permission = Permission.create("test", "test", PermissionType.DATA, "resource", "get", "123123");
        Assertions.assertEquals(PermissionType.DATA, permission.getType());
        Assertions.assertEquals("test", permission.getCode());
        Assertions.assertEquals(PermissionStatus.ENABLED, permission.getStatus());

        // 测试禁用权限
        permission.updateStatus(PermissionStatus.DISABLED, "测试禁用", Operator.system());
        Assertions.assertEquals(PermissionStatus.DISABLED, permission.getStatus());

        Collection<Object> objects = permission.domainEvents();

        PermissionStatusChangedEvent event = (PermissionStatusChangedEvent) objects.stream().findFirst().orElseThrow();

        Assertions.assertEquals(event.getId(), permission.getId());
        Assertions.assertEquals(PermissionStatus.ENABLED, event.getOldStatus());
        Assertions.assertEquals(PermissionStatus.DISABLED, event.getNewStatus());
    }
}
