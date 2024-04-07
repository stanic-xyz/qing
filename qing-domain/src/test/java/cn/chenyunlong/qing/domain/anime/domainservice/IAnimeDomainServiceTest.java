package cn.chenyunlong.qing.domain.anime.domainservice;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.IAnimeDomainService;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeCategoryService;
import cn.chenyunlong.qing.domain.anime.anime.service.ITagService;
import cn.chenyunlong.qing.domain.anime.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.anime.attachement.service.IAttachmentService;
import cn.chenyunlong.qing.domain.anime.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.anime.district.service.IDistrictService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;

class IAnimeDomainServiceTest extends AbstractDomainTests {

    @Autowired
    private IAnimeCategoryService categoryService;

    @Autowired
    private ITagService tagService;

    @Autowired
    private IDistrictService districtService;

    @Autowired
    private IAnimeDomainService animeDomainService;

    @Autowired
    private IAttachmentService attachmentService;


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
        AnimeCreateRequest createRequest = new AnimeCreateRequest();
        createRequest.setName("凡人修仙传");
        createRequest.setCompanyName("起点中文网");
        createRequest.setAuthor("忘语");
        createRequest.setInstruction(
            "看机智的凡人小子韩立如何稳健发展、步步为营，战魔道、夺至宝、驰骋星海、快意恩仇，成为纵横三界的强者。他日仙界重相逢，一声道友尽沧桑。");
        createRequest.setPlayStatus(PlayStatus.SERIALIZING);
        createRequest.setPlotType("TV动画");
        createRequest.setOrderNo(1);
        createRequest.setOfficialWebsite("https://www.bilibili.com/bangumi/media/md28223043");
        createRequest.setDistrictId(1L);
        createRequest.setDistrictName("中国");
        createRequest.setTypeName("喜剧");
        createRequest.setPremiereDate(LocalDate.now());
        createRequest.setCoverUrl(
            "https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9" +
                ".jpg@450w_600h.webp");
        createRequest.setPremiereDate(LocalDate.of(2022, 7, 5));
        createRequest.setPlayHeat(String.valueOf(1430000000));

        // 创建一个标签
        TagCreator tagCreator = TagCreator.builder().name("玄幻").instruction("玄幻").build();
        Long tagId = tagService.createTag(tagCreator);
        createRequest.setTagIds(CollUtil.toList(tagId));

        // 创建一个区域
        DistrictCreator districtCreator = new DistrictCreator();
        districtCreator.setName("中国");
        districtCreator.setCode("CN");
        Long districtId = districtService.createDistrict(districtCreator);
        createRequest.setDistrictId(districtId);

        // 创建一个动画类别
        AnimeCategoryCreator categoryCreator = AnimeCategoryCreator.builder()
                                                   .pid(null)
                                                   .name("言情")
                                                   .orderNo(1)
                                                   .build();
        Long categoryId = categoryService.createAnimeCategory(categoryCreator);
        createRequest.setTypeId(categoryId);

        Long attachmentId = attachmentService.createAttachment(AttachmentCreator.builder()
                                                                   .path("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9.jpg@450w_600h.webp")
                                                                   .fileId(IdUtil.getSnowflakeNextId())
                                                                   .fileName("95d2881427fd43431f6a696a05623675ecdce9d9.jpg")
                                                                   .fileSize(100000000000L)
                                                                   .mimeType(MimeTypeUtils.APPLICATION_XML_VALUE)
                                                                   .build());
        createRequest.setCoverUrlAttachmentId(attachmentId);
        createRequest.setCoverUrl("");

        Long animeId = animeDomainService.createAnime(createRequest);
        Assertions.assertNotNull(animeId);
    }
}
