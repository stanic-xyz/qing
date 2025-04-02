package cn.chenyunlong.qing.anime.domain.anime.dto.updater;

import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema
public class AnimeUpdateCommand {

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "instruction"
    )
    private String instruction;


    @Schema(title = "封面的附件Id")
    private String cover;

    @Schema(
        title = "originalName"
    )
    private String originalName;

    @Schema(
        title = "otherName"
    )
    private String otherName;

    @Schema(
        title = "author"
    )
    private String author;

    @Schema(
        title = "companyId"
    )
    private Long companyId;

    @Schema(
        title = "companyName"
    )
    private String companyName;

    @Schema(
        title = "premiereDate"
    )
    private LocalDate premiereDate;

    @Schema(
        title = "officialWebsite"
    )
    private String officialWebsite;

    private AnimeId id;
}
