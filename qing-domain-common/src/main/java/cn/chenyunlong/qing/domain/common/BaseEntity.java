package cn.chenyunlong.qing.domain.common;

// 这是一个纯技术的基础类，不包含任何业务逻辑
// 主要用于提供ID管理和相等性判断的基础实现
public abstract class BaseEntity<ID> implements Entity<ID> {
    protected ID id;

    protected BaseEntity() {}

    protected BaseEntity(ID id) {
        this.id = id;
    }

    @Override
    public ID getId() {
        return id;
    }

    // 保护性的setter，主要用于框架反序列化
    protected void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BaseEntity<?> that = (BaseEntity<?>) obj;

        // 如果是瞬态对象（没有ID），比较对象引用
        if (id == null || that.id == null) {
            return super.equals(obj);
        }

        return id.equals(that.id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + "]";
    }
}
