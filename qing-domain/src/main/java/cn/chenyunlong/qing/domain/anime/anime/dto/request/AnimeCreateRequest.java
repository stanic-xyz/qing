package cn.chenyunlong.qing.domain.anime.anime.dto.request;

import cn.chenyunlong.common.model.Request;
import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Schema
@Data
public class AnimeCreateRequest implements Request {

    @NotBlank
    @Schema(title = "name")
    private String name;

    @NotBlank
    @Schema(title = "instruction")
    private String instruction;

    @Schema(title = "districtId")
    private Long districtId;

    @Schema(title = "districtName")
    private String districtName;

    @Schema(title = "coverUrl")
    private String coverUrl;

    @Schema(title = "typeId")
    private Long typeId;

    @Schema(title = "typeName")
    private String typeName;

    @Schema(title = "originalName")
    private String originalName;

    @Schema(title = "otherName")
    private String otherName;

    @Schema(title = "author")
    private String author;

    @Schema(title = "companyId")
    private Long companyId;

    @Schema(title = "companyName")
    private String companyName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Schema(title = "premiereDate")
    private LocalDate premiereDate;

    @Schema(title = "playStatus")
    private PlayStatus playStatus;

    @Schema(title = "plotType")
    private String plotType;

    @Schema(title = "tags")
    private List<Long> tagIds;

    @Schema(title = "officialWebsite")
    private String officialWebsite;

    @Schema(title = "playHeat")
    private String playHeat;

    @Schema(title = "orderNo")
    private Integer orderNo;
}
