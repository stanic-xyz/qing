package cn.chenyunlong.qing.anime.domain.anime.exception;


import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;

public class AnimeNotFoundException extends RuntimeException {

    public AnimeNotFoundException(AnimeId animeId) {
        super("动漫未找到: " + animeId);
    }
}
