package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 创建角色请求类，用于封装创建角色所需的参数信息
 * 使用 @Data 注解来自动生成 getter、setter、toString 等方法
 */
@Data
public class CreateRoleRequest {

    /**
     * 权限分组（权限空间）内角色的唯一标识符
     * 用于在系统中唯一标识一个角色
     */
    @JsonProperty("code")
    private String code;
    /**
     * 权限分组（权限空间）内角色名称
     * 用于显示和识别角色的名称
     */
    @JsonProperty("name")
    private String name;
    /**
     * 所属权限分组(权限空间)的 code，不传获取默认权限分组。
     */
    @JsonProperty("namespace")
    private String namespace;
    /**
     * 角色描述
     */
    @JsonProperty("description")
    private String description;
    /**
     * 角色自动禁止时间，单位毫秒, 如果传null表示永久有效
     */
    @JsonProperty("disableTime")
    private String disableTime;
}
