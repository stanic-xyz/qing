package chenyunlong.zhangli.model.dto.bilibili;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.bilibili.BiliAnimeInfoEntity;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class AnimeInfoDTO implements OutputConverter<AnimeInfoDTO, BiliAnimeInfoEntity> {

    private Long id;
    private Long mediaId;
    private String title;
    private Long seasonId;
    private String cover;
    private Integer isFinished;
    private String indexShow;
    private String order;
    private String link;

    @NonNull
    @Override
    public <T extends AnimeInfoDTO> T convertFrom(@NonNull BiliAnimeInfoEntity animeInfo) {
        this.setOrder(animeInfo.getScore() + "分");
        return OutputConverter.super.convertFrom(animeInfo);
    }
}
