// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.feedback.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class FeedbackResponse extends AbstractJpaResponse {
    @Schema(
            title = "mid"
    )
    private Long mid;

    @Schema(
            title = "type"
    )
    private Integer type;

    @Schema(
            title = "detail"
    )
    private String detail;

    @Schema(
            title = "uid"
    )
    private String uid;

    @Schema(
            title = "processingStatus"
    )
    private Integer processingStatus;
}