package cn.chenyunlong.qing.anime.domain.anime.exception;

import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;

// 自定义领域异常
public class AnimeNotApprovedException extends RuntimeException {
    public AnimeNotApprovedException(AnimeId id) {
        super("动漫未通过审核: " + id.id());
    }
}
