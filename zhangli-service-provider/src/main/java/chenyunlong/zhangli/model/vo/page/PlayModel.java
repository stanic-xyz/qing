package chenyunlong.zhangli.model.vo.page;

import chenyunlong.zhangli.model.dto.AnimeCommentDTO;
import chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoPlayVo;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlayModel extends BaseModel {

    private AnimeInfoPlayVo animeInfo;
    private List<AnimeInfoMinimalDTO> relevant;
    private List<AnimeInfoMinimalDTO> recommendation;
    private IPage<AnimeCommentDTO> comments;
    private AnimeEpisodeDTO episodeInfo;
}
