/*
 * Copyright (c) 2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.anime.anime.domainservice;

import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import cn.chenyunlong.qing.domain.anime.anime.creator.AnimeInfoCreator;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.biz.BatchRecommendModel;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.meta.InOutBizType;
import cn.chenyunlong.qing.domain.anime.attachment.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.anime.attachment.service.IAttachmentService;
import cn.chenyunlong.qing.domain.anime.episode.service.IEpisodeService;
import cn.chenyunlong.qing.domain.anime.playlist.service.IPlaylistService;
import cn.chenyunlong.qing.domain.anime.tag.creator.AnimeTagCreator;
import cn.chenyunlong.qing.domain.anime.tag.service.IAnimeTagService;
import cn.chenyunlong.qing.domain.anime.type.creator.AnimeTypeCreator;
import cn.chenyunlong.qing.domain.anime.type.service.IAnimeTypeService;
import cn.chenyunlong.qing.domain.district.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.district.service.IDistrictService;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest
@ActiveProfiles("local")
@Rollback
class AnimeDomainServiceImplTest {

    @Autowired
    private IAnimeDomainService animeDomainService;

    @Autowired
    private IAnimeTagService animeTagService;

    @Autowired
    private IAnimeTypeService animeTypeService;

    @Autowired
    private IEpisodeService episodeService;

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IDistrictService districtService;

    @Test
    void testAddTypeInfo() {
        AnimeTypeCreator typeCreator = new AnimeTypeCreator();
        typeCreator.setName("玄幻");
        typeCreator.setDescription("玄幻 ");
        animeTypeService.createAnimeType(typeCreator);
    }

    @Test
    void testAddAttachment() {
        AttachmentCreator attachmentCreator = new AttachmentCreator();
        attachmentCreator.setAttachmentUrl("https://i0.hdslb" +
                ".com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9\n" +
                ".jpg@450w_600h.webp");
        attachmentCreator.setAttachmentName("凡人修仙传封面");
        attachmentService.createAttachment(attachmentCreator);
    }

    @Test
    void testAddTag() {
        AnimeTagCreator creator = new AnimeTagCreator();
        creator.setName("玄幻");
        creator.setDescription("大家最爱的玄幻电影");
        animeTagService.createAnimeTag(creator);
    }

    @Test
    void testAddAuthor() {
        AnimeTagCreator creator = new AnimeTagCreator();
        creator.setName("玄幻");
        creator.setDescription("大家最爱的玄幻电影");
        animeTagService.createAnimeTag(creator);
    }


    @Test
    void testAddAnimeInfo() {

        AnimeTypeCreator typeCreator = new AnimeTypeCreator();
        typeCreator.setName("玄幻");
        typeCreator.setDescription("玄幻 ");
        Long typeId = animeTypeService.createAnimeType(typeCreator);

        DistrictCreator districtCreator = new DistrictCreator();
        districtCreator.setCode("CN");
        districtCreator.setName("中国");
        districtCreator.setDescription("中国");
        Long districtId = districtService.createDistrict(districtCreator);

        AnimeInfoCreator animeInfoCreator = new AnimeInfoCreator();
        animeInfoCreator.setName("凡人修仙传");
        animeInfoCreator.setCompany("起点中文网");
        animeInfoCreator.setAuthor("忘语");
        animeInfoCreator.setInstruction("看机智的凡人小子韩立如何稳健发展、步步为营，战魔道、夺至宝、驰骋星海、快意恩仇，成为纵横三界的强者。他日仙界重相逢，一声道友尽沧桑。");
        animeInfoCreator.setTags("玄幻");
        animeInfoCreator.setPlayStatus(PlayStatus.SERIALIZING);
        animeInfoCreator.setPlotType("TV动画");
        animeInfoCreator.setOrderNo(1);
        animeInfoCreator.setOfficialWebsite("https://www.bilibili.com/bangumi/media/md28223043");
        animeInfoCreator.setDistrictId(districtId);
        animeInfoCreator.setDistrictName(districtCreator.getName());
        animeInfoCreator.setTypeId(typeId);
        animeInfoCreator.setTypeName(typeCreator.getName());
        animeInfoCreator.setPremiereDate(LocalDate.now());
        animeInfoCreator.setCoverUrl("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9" +
                ".jpg@450w_600h.webp");
        animeInfoCreator.setPremiereDate(LocalDate.of(2022, 7, 5));
        animeInfoCreator.setPlayHeat(String.valueOf(1430000000));
        animeDomainService.create(animeInfoCreator);


    }

    @Test
    void handleAnimeInfoRecommend() {

        AnimeTypeCreator typeCreator = new AnimeTypeCreator();
        typeCreator.setName("玄幻");
        typeCreator.setDescription("玄幻 ");
        Long typeId = animeTypeService.createAnimeType(typeCreator);

        DistrictCreator districtCreator = new DistrictCreator();
        districtCreator.setCode("CN");
        districtCreator.setName("中国");
        districtCreator.setDescription("中国");
        Long districtId = districtService.createDistrict(districtCreator);

        AnimeInfoCreator animeInfoCreator = new AnimeInfoCreator();
        animeInfoCreator.setName("凡人修仙传");
        animeInfoCreator.setCompany("起点中文网");
        animeInfoCreator.setAuthor("忘语");
        animeInfoCreator.setInstruction("看机智的凡人小子韩立如何稳健发展、步步为营，战魔道、夺至宝、驰骋星海、快意恩仇，成为纵横三界的强者。他日仙界重相逢，一声道友尽沧桑。");
        animeInfoCreator.setTags("玄幻");
        animeInfoCreator.setPlayStatus(PlayStatus.SERIALIZING);
        animeInfoCreator.setPlotType("TV动画");
        animeInfoCreator.setOrderNo(1);
        animeInfoCreator.setOfficialWebsite("https://www.bilibili.com/bangumi/media/md28223043");
        animeInfoCreator.setDistrictId(districtId);
        animeInfoCreator.setDistrictName(districtCreator.getName());
        animeInfoCreator.setTypeId(typeId);
        animeInfoCreator.setTypeName(typeCreator.getName());
        animeInfoCreator.setPremiereDate(LocalDate.now());
        animeInfoCreator.setCoverUrl("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9" +
                ".jpg@450w_600h.webp");
        animeInfoCreator.setPremiereDate(LocalDate.of(2022, 7, 5));
        animeInfoCreator.setPlayHeat(String.valueOf(1430000000));
        Long aLong = animeDomainService.create(animeInfoCreator);

        BatchRecommendModel batchRecommendModel = new BatchRecommendModel();
        batchRecommendModel.setOperateUser("Stan");
        batchRecommendModel.setName("推荐名称");
        batchRecommendModel.setReason("推荐理由");
        batchRecommendModel.setSkuId(new SnowflakeGenerator().next());
        batchRecommendModel.setAnimeIds(Collections.singletonList(aLong));
        batchRecommendModel.setInOutBizType(InOutBizType.IN_BUY);
        batchRecommendModel.setHouseId(1L);
        batchRecommendModel.setBatchNo(new Snowflake().nextIdStr());
        animeDomainService.handleAnimeInfoRecommend(batchRecommendModel);

    }
}
