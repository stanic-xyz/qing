package cn.chenyunlong.qing.domain.anime.recommend.dto.vo;

import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeMapper;
import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
public class RecommendDetailVO extends RecommendVO {

    @Schema(title = "动画信息")
    private AnimeVO animeVO;

    public RecommendDetailVO(Recommend source, Anime anime) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setVersion(source.getVersion());
        this.setName(source.getName());
        this.setInstruction(source.getInstruction());
        this.animeVO = AnimeMapper.INSTANCE.entityToVo(anime);
    }
}
