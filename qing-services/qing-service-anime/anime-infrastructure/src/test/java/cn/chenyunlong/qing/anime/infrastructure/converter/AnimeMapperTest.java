package cn.chenyunlong.qing.anime.infrastructure.converter;


import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeUpdateCommand;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AnimeMapperTest {

    @Test
    void request2Updater() {
        AnimeUpdateRequest request = new AnimeUpdateRequest();
        request.setTypeId(1L);
        AnimeUpdateCommand animeUpdateCommand = AnimeMapper.INSTANCE.request2Updater(request);
        animeUpdateCommand.setId(new AnimeId(1L));
        Assertions.assertEquals(1, animeUpdateCommand.getId().getId());
    }
}
