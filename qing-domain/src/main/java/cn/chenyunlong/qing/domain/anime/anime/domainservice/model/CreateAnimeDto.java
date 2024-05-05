package cn.chenyunlong.qing.domain.anime.anime.domainservice.model;

import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAnimeDto extends AnimeCreator {

    private Long typeId;

}
