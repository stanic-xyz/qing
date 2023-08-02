package cn.chenyunlong.qing.domain.anime.domainservice;

import cn.chenyunlong.qing.domain.anime.PlayStatus;
import cn.chenyunlong.qing.domain.anime.creator.AnimeInfoCreator;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class IAnimeDomainServiceTest {

    @Autowired
    private IAnimeDomainService animeDomainService;

    @Test
    void handleAnimeInfoRecommend() {
    }

    @Test
    void handleAnimeInfoOut() {
    }

    @Test
    void handleAnimeInfoTransfer() {
    }

    @Test
    void create() {
        AnimeInfoCreator animeInfoCreator = new AnimeInfoCreator();
        animeInfoCreator.setName("凡人修仙传");
        animeInfoCreator.setCompany("起点中文网");
        animeInfoCreator.setAuthor("忘语");
        animeInfoCreator.setInstruction(
            "看机智的凡人小子韩立如何稳健发展、步步为营，战魔道、夺至宝、驰骋星海、快意恩仇，成为纵横三界的强者。他日仙界重相逢，一声道友尽沧桑。");
        animeInfoCreator.setTags("玄幻");
        animeInfoCreator.setPlayStatus(PlayStatus.SERIALIZING);
        animeInfoCreator.setPlotType("TV动画");
        animeInfoCreator.setOrderNo(1);
        animeInfoCreator.setOfficialWebsite("https://www.bilibili.com/bangumi/media/md28223043");
        animeInfoCreator.setDistrictId(1L);
        animeInfoCreator.setDistrictName("中国");
        animeInfoCreator.setTypeId(1L);
        animeInfoCreator.setTypeName("喜剧");
        animeInfoCreator.setPremiereDate(LocalDate.now());
        animeInfoCreator.setCoverUrl(
            "https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9" +
                ".jpg@450w_600h.webp");
        animeInfoCreator.setPremiereDate(LocalDate.of(2022, 7, 5));
        animeInfoCreator.setPlayHeat(String.valueOf(1430000000));
        Long aLong = animeDomainService.create(animeInfoCreator);
        Assertions.assertNotNull(aLong);
    }
}
