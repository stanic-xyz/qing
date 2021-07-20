package chenyunlong.zhangli.common.core.domain;

import chenyunlong.zhangli.common.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Base entity.
 *
 * @author Stan
 * @date 2021/05/22
 */
@Data
@ToString
@EqualsAndHashCode
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

    /**
     * 排序号
     */
    private Integer orderNo;

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
        if (orderNo == null) {
            //不进行排序
            orderNo = 0;
        }
    }
}
