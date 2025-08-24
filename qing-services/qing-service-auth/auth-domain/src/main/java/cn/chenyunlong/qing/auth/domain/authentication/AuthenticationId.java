package cn.chenyunlong.qing.auth.domain.authentication;

import cn.chenyunlong.qing.domain.common.EntityId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;

public class AuthenticationId extends EntityId<Long> {

    /**
     * 分类的唯一标识符
     */
    @Getter
    @NotNull
    Long value;

    /**
     * 构造函数
     *
     * @param value 分类的唯一标识符
     * @throws IllegalArgumentException 当ID无效时
     */
    private AuthenticationId(@NonNull Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("分类ID必须大于0");
        }
        this.value = value;
    }

    /**
     * 创建分类ID的工厂方法
     *
     * @param value 分类的唯一标识符
     * @return 分类ID实例
     * @throws IllegalArgumentException 当ID无效时
     */
    public static AuthenticationId of(Long value) {
        return new AuthenticationId(value);
    }

    /**
     * 创建分类ID的工厂方法（字符串形式）
     *
     * @param id 分类的唯一标识符字符串
     * @return 分类ID实例
     * @throws IllegalArgumentException 当ID无效时
     * @throws NumberFormatException    当字符串无法转换为数字时
     */
    public static AuthenticationId of(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("分类ID字符串不能为空");
        }
        try {
            return new AuthenticationId(Long.parseLong(id.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的分类ID格式: " + id, e);
        }
    }
}
