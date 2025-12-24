package cn.chenyunlong.qing.anime.domain.anime.events;


import cn.chenyunlong.qing.anime.domain.anime.models.AnimeData;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;

public class AnimeEvents {


    public record AnimeCreated(AnimeId animeId) {}

    public record AnimePutOn(AnimeId animeId) {}

    public record AnimeTakenOff(AnimeId animeId) {}

    public record AnimeDeleted(AnimeId animeId) {}

    public record AnimeUpdated(AnimeId animeId) {}

    public record AnimeInfoUpdated(AnimeId animeId, AnimeData animeData) {}
}
