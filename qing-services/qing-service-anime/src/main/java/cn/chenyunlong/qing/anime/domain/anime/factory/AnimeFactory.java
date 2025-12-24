package cn.chenyunlong.qing.anime.domain.anime.factory;

import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import cn.chenyunlong.qing.anime.domain.anime.enums.District;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeCategory;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeType;
import cn.chenyunlong.qing.anime.domain.anime.models.Company;
import cn.chenyunlong.qing.anime.domain.anime.models.PremiereDate;
import cn.chenyunlong.qing.anime.domain.anime.models.Tags;

public class AnimeFactory {

    public static Anime createAnime(long id,
            String name,
            String instruction,
            District district,
            AnimeType type,
            Company company,
            PremiereDate premiereDate,
            Tags tags,
            AnimeCategory animeCategory) {
        Anime anime = Anime.rebuild(
                AnimeId.of(id),
                name,
                animeCategory,
                instruction,
                district,
                null, // cover
                type,
                null, // originalName
                null, // otherName
                null, // author
                company,
                premiereDate,
                PlayStatus.SERIALIZING,
                null, // plotTypes
                tags,
                null, // officialWebsite
                null, // playHeat
                null, // lastUpdateTime
                null, // orderNo
                false, // isOnShelf
                false // isDeleted
        );
        anime.init(); // 调用基类的初始化方法
        return anime;
    }
}
