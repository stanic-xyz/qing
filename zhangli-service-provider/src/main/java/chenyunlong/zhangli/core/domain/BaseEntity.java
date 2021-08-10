package chenyunlong.zhangli.core.domain;

import chenyunlong.zhangli.utils.StringUtils;

import java.time.LocalDateTime;

/**
 * Base entity.
 *
 * @author Stan
 * @date 2021/05/22
 */
public class BaseEntity {

    /**
     * Create time.
     */
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    private LocalDateTime updateTime;

    /**
     * 搜索值
     */
    private String searchValue;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 备注
     */
    private String remark;

    public BaseEntity() {
    }


    /**
     * 数据检查
     */
    public void preCheck() {
        if (createBy == null) {
            createTime = LocalDateTime.now();
        }
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
        if (StringUtils.isEmpty(searchValue)) {
            searchValue = "";
        }
        if (StringUtils.isEmpty(createBy)) {
            createBy = "system";
        }
        if (StringUtils.isEmpty(updateBy)) {
            updateBy = "";
        }
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    public String getSearchValue() {
        return this.searchValue;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BaseEntity)) return false;
        final BaseEntity other = (BaseEntity) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$createTime = this.getCreateTime();
        final Object other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime))
            return false;
        final Object this$updateTime = this.getUpdateTime();
        final Object other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !this$updateTime.equals(other$updateTime))
            return false;
        final Object this$searchValue = this.getSearchValue();
        final Object other$searchValue = other.getSearchValue();
        if (this$searchValue == null ? other$searchValue != null : !this$searchValue.equals(other$searchValue))
            return false;
        final Object this$createBy = this.getCreateBy();
        final Object other$createBy = other.getCreateBy();
        if (this$createBy == null ? other$createBy != null : !this$createBy.equals(other$createBy)) return false;
        final Object this$updateBy = this.getUpdateBy();
        final Object other$updateBy = other.getUpdateBy();
        if (this$updateBy == null ? other$updateBy != null : !this$updateBy.equals(other$updateBy)) return false;
        final Object this$remark = this.getRemark();
        final Object other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BaseEntity;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $createTime = this.getCreateTime();
        result = result * PRIME + ($createTime == null ? 43 : $createTime.hashCode());
        final Object $updateTime = this.getUpdateTime();
        result = result * PRIME + ($updateTime == null ? 43 : $updateTime.hashCode());
        final Object $searchValue = this.getSearchValue();
        result = result * PRIME + ($searchValue == null ? 43 : $searchValue.hashCode());
        final Object $createBy = this.getCreateBy();
        result = result * PRIME + ($createBy == null ? 43 : $createBy.hashCode());
        final Object $updateBy = this.getUpdateBy();
        result = result * PRIME + ($updateBy == null ? 43 : $updateBy.hashCode());
        final Object $remark = this.getRemark();
        result = result * PRIME + ($remark == null ? 43 : $remark.hashCode());
        return result;
    }

    public String toString() {
        return "BaseEntity(createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", searchValue=" + this.getSearchValue() + ", createBy=" + this.getCreateBy() + ", updateBy=" + this.getUpdateBy() + ", remark=" + this.getRemark() + ")";
    }
}
