package cn.chenyunlong.qing.domain.anime.anime.dto.request;

import cn.chenyunlong.common.model.Request;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Schema
@Data
public class AnimeCreateCommand implements Request {

    @NotBlank
    @Schema(title = "name")
    private String name;

    @NotBlank
    @Schema(title = "instruction")
    private String instruction;

    @NotNull
    @Schema(title = "districtId")
    private Long districtId;

    @Schema(title = "封面的附件Id")
    private Long coverAttachmentId;

    @NotNull
    @Schema(title = "typeId")
    private Long typeId;

    @Schema(title = "originalName")
    private String originalName;

    @Schema(title = "otherName")
    private String otherName;

    @Schema(title = "author")
    private String author;

    @Schema(title = "companyId")
    private Long companyId;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Schema(title = "premiereDate")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate premiereDate;

    @Schema(title = "plotType")
    private String plotType;

    @NotNull
    @Schema(title = "tags")
    private List<Long> tagIds;

    @Schema(title = "officialWebsite")
    private String officialWebsite;
}