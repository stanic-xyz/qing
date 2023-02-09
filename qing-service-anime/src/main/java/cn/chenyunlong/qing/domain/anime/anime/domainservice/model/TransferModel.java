package cn.chenyunlong.qing.domain.anime.anime.domainservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TransferModel {

    @Schema(name = "skuId")
    private Long skuId;

    @Schema(name = "操作用户")
    private String operateUser;

    @Schema(name = "转入仓库id")
    private Long transferInHouseId;

    @Schema(name = "转出仓库id")
    private Long transferOutHouseId;

    @Schema(name = "唯一编码")
    private List<Long> uniqueCodes;

    @Schema(name = "批次号")
    private String batchNo;

}
