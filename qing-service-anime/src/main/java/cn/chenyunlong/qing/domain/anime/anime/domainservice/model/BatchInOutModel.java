package cn.chenyunlong.qing.domain.anime.anime.domainservice.model;

import cn.chenyunlong.qing.domain.anime.anime.domainservice.InOutBizType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 批量出入库模型
 */
@Data
public class BatchInOutModel {

    private String name;

    @Schema(name = "出入库类型")
    private InOutBizType inOutBizType;

    @Schema(name = "操作用户")
    private String operateUser;

    @Schema(name = "仓库Id")
    private Long houseId;

    @Schema(name = "唯一编码")
    private List<Long> uniqueCodes;

    @Schema(name = "批次号")
    private String batchNo;

    @Schema(name = "skuId")
    private Long skuId;
}
