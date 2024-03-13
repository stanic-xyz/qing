package cn.chenyunlong.qing.domain.anime.anime.events;

import cn.chenyunlong.qing.domain.anime.anime.Anime;
import lombok.Value;

public class AnimeEvents {

    @Value
    public static class AnimeCreated {

        Anime anime;

    }
}
