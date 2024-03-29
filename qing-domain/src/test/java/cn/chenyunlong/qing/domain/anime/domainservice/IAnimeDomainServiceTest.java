package cn.chenyunlong.qing.domain.anime.domainservice;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeCategoryService;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import cn.chenyunlong.qing.domain.anime.tag.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.tag.service.ITagService;
import cn.hutool.core.collection.CollUtil;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class IAnimeDomainServiceTest extends AbstractDomainTests {

    @Autowired
    private IAnimeService animeService;

    @Autowired
    private IAnimeCategoryService categoryService;

    @Autowired
    private ITagService tagService;

    @Test
    void handleAnimeInfoRecommend() {
        // TODO 测试处理动画信息推荐
    }

    @Test
    void handleAnimeInfoOut() {
        // TODO 测试处理动画信息退出
    }

    @Test
    void handleAnimeInfoTransfer() {
        // TODO 测试处理动画信息转移
    }

    @Test
    void create() {
        // 创建一个动画类别
        Long category = categoryService.createAnimeCategory(AnimeCategoryCreator.builder()
                                                                .pid(null)
                                                                .name("言情")
                                                                .orderNo(1)
                                                                .build());

        Long tagId = tagService.createTag(TagCreator.builder().name("玄幻").instruction("玄幻").build());

        AnimeCreator animeInfoCreator = new AnimeCreator();
        animeInfoCreator.setName("凡人修仙传");
        animeInfoCreator.setCompanyName("起点中文网");
        animeInfoCreator.setAuthor("忘语");
        animeInfoCreator.setInstruction(
            "看机智的凡人小子韩立如何稳健发展、步步为营，战魔道、夺至宝、驰骋星海、快意恩仇，成为纵横三界的强者。他日仙界重相逢，一声道友尽沧桑。");
        animeInfoCreator.setTagIds(CollUtil.toList(tagId));
        animeInfoCreator.setPlayStatus(PlayStatus.SERIALIZING);
        animeInfoCreator.setPlotType("TV动画");
        animeInfoCreator.setOrderNo(1);
        animeInfoCreator.setOfficialWebsite("https://www.bilibili.com/bangumi/media/md28223043");
        animeInfoCreator.setDistrictId(1L);
        animeInfoCreator.setDistrictName("中国");
        animeInfoCreator.setTypeId(category);
        animeInfoCreator.setTypeName("喜剧");
        animeInfoCreator.setPremiereDate(LocalDate.now());
        animeInfoCreator.setCoverUrl(
            "https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9" +
                ".jpg@450w_600h.webp");
        animeInfoCreator.setPremiereDate(LocalDate.of(2022, 7, 5));
        animeInfoCreator.setPlayHeat(String.valueOf(1430000000));
        Long aLong = animeService.createAnime(animeInfoCreator);
        Assertions.assertNotNull(aLong);
    }
}
