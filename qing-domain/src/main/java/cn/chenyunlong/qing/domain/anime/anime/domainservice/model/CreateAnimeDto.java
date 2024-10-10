package cn.chenyunlong.qing.domain.anime.anime.domainservice.model;

import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAnimeDto extends AnimeCreator {

    private Long typeId;

}
