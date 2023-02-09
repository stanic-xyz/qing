package cn.chenyunlong.qing.domain.anime.anime.domainservice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AnimeInfoBizInfo {

    private InOutBizType inOutBizType;

    @Schema(name = "唯一编码")
    private List<Long> uniqueCodes;

    @Schema(name = "批次号")
    private String batchNo;

    @Schema(name = "自动生成批次号")
    private String genBatchNo;

    private String operateUser;
}
