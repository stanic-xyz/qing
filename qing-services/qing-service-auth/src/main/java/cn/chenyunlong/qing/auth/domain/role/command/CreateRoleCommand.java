package cn.chenyunlong.qing.auth.domain.role.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRoleCommand {

    /**
     * 权限分组（权限空间）内角色的唯一标识符
     * 用于在系统中唯一标识一个角色
     */
    private String code;
    /**
     * 权限分组（权限空间）内角色名称
     * 用于显示和识别角色的名称
     */
    private String name;
    /**
     * 所属权限分组(权限空间)的 code，不传获取默认权限分组。
     */
    private String namespace;
    /**
     * 角色描述
     */
    private String description;

    public static CreateRoleCommand create(String code, String name, String description) {
        return new CreateRoleCommand(code, name, "default", description);
    }
}
