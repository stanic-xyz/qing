package cn.chenyunlong.qing.anime.domain.anime.factory;

import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import cn.chenyunlong.qing.anime.domain.anime.enums.District;
import cn.chenyunlong.qing.anime.domain.anime.models.*;

public class AnimeFactory {

    public static Anime createAnime(long id,
                                    String name,
                                    String instruction,
                                    District district,
                                    AnimeType type,
                                    Company company,
                                    PremiereDate premiereDate,
                                    Tags tags,
                                    AnimeCategory animeCategory
    ) {
        Anime anime = new Anime();
        anime.setAnimeId(new AnimeId(id));
        anime.setName(name);
        anime.setInstruction(instruction);
        anime.setDistrict(district);
        anime.setType(type);
        anime.setCompany(company);
        anime.setPremiereDate(premiereDate);
        anime.setPlayStatus(PlayStatus.SERIALIZING);
        anime.setPlayHeat("0");
        anime.setTags(tags);
        anime.setAnimeCategory(animeCategory);
        anime.init(); // 调用基类的初始化方法
        return anime;
    }
}
