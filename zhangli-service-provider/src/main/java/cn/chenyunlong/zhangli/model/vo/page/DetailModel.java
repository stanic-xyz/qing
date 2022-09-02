package cn.chenyunlong.zhangli.model.vo.page;

import cn.chenyunlong.zhangli.model.dto.AnimeCommentDTO;
import cn.chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import cn.chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetailModel extends BaseModel {

    private AnimeInfoVo animeInfo;
    private List<AnimeInfoMinimalDTO> relevant;
    private List<AnimeInfoMinimalDTO> recommendation;
    private IPage<AnimeCommentDTO> comments;
}
