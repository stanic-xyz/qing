package cn.chenyunlong.qing.domain.anime.anime.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import cn.chenyunlong.qing.domain.anime.episode.dto.vo.EpisodeVO;
import cn.chenyunlong.qing.domain.anime.playlist.dto.vo.PlayListVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
public class AnimeDetailVO extends AbstractBaseJpaVo {

    @Schema(title = "name")
    private String name;

    @Schema(title = "instruction")
    private String instruction;

    @Schema(title = "districtId")
    private Long districtId;

    @Schema(title = "districtName")
    private String districtName;

    @Schema(
        title = "coverUrl"
    )
    private String cover;

    @Schema(
        title = "typeId"
    )
    private Long typeId;

    @Schema(
        title = "typeName"
    )
    private String typeName;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate premiereDate;

    @Schema(
        title = "playStatus"
    )
    private PlayStatus playStatus;

    @Schema(
        title = "plotType"
    )
    private String plotType;

    @Schema(
        title = "tags"
    )
    private String tags;

    @Schema(
        title = "officialWebsite"
    )
    private String officialWebsite;

    @Schema(
        title = "playHeat"
    )
    private String playHeat;

    @Schema(
        title = "orderNo"
    )
    private Integer orderNo;

    private List<TagVO> tagVOList;

    private List<EpisodeVO> episodeList;

    private List<PlayListVO> playLists;
}
