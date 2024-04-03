package cn.chenyunlong.qing.domain.anime.anime.domainservice.model;

import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAnimeDto extends AnimeCreator {

    private Long typeId;

}
