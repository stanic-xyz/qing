package cn.chenyunlong.qing.anime.domain.product;

import cn.chenyunlong.qing.domain.common.EntityId;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ProductId extends EntityId<Long> {

    /**
     * 动漫的唯一标识符
     */
    @NonNull
    Long value;

    /**
     * 构造函数
     *
     * @param value 动漫的唯一标识符
     * @throws IllegalArgumentException 当ID无效时
     */
    private ProductId(@NonNull Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("动漫ID必须大于0");
        }
        this.value = value;
    }

    /**
     * 创建动漫ID的工厂方法
     *
     * @param value 动漫的唯一标识符
     * @return 动漫ID实例
     * @throws IllegalArgumentException 当ID无效时
     */
    public static ProductId of(Long value) {
        return new ProductId(value);
    }

    /**
     * 创建动漫ID的工厂方法（字符串形式）
     *
     * @param id 产品的唯一标识符字符串
     * @return 动漫ID实例
     * @throws IllegalArgumentException 当ID无效时
     * @throws NumberFormatException    当字符串无法转换为数字时
     */
    public static ProductId of(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("动漫ID字符串不能为空");
        }
        try {
            return new ProductId(Long.parseLong(id.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的动漫ID格式: " + id, e);
        }
    }

    /**
     * 获取ID值（兼容旧API）
     *
     * @return ID值
     * @deprecated 使用 {@link #getValue()} 替代
     */
    @Deprecated
    public Long getValue() {
        return value;
    }
}
