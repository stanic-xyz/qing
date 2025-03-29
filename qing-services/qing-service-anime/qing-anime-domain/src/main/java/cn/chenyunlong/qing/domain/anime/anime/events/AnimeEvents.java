package cn.chenyunlong.qing.domain.anime.anime.events;


import cn.chenyunlong.qing.domain.anime.anime.models.AnimeId;

public class AnimeEvents {


    public record AnimeCreated(AnimeId animeId) {
    }


    public record AnimePutOn(AnimeId animeId) {
    }


    public record AnimeTakenOff(AnimeId animeId) {
    }


    public record AnimeDeleted(AnimeId animeId) {
    }

}
