package cn.chenyunlong.qing.auth.domain.rbac.rolepermission;

import cn.chenyunlong.qing.domain.common.Identifiable;

public record RolePermissionId(Long id) implements Identifiable<Long> {

    public static RolePermissionId generate() {
        return RolePermissionId.of(Identifiable.simpleGenerate());
    }

    private static RolePermissionId of(Long id) {
        return new RolePermissionId(id);
    }
}
