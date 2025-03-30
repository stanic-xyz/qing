package impl;

import cn.chenyunlong.qing.anime.application.service.IAnimeService;
import cn.chenyunlong.qing.anime.application.service.impl.AnimeServiceImpl;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCreateCommand;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.anime.domain.attachement.Attachment;
import cn.chenyunlong.qing.anime.domain.attachement.AttachmentRepository;
import cn.chenyunlong.qing.anime.domain.district.District;
import cn.chenyunlong.qing.anime.domain.district.repository.DistrictRepository;
import cn.chenyunlong.qing.anime.domain.type.Type;
import cn.chenyunlong.qing.anime.domain.type.repository.TypeRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.hutool.core.collection.CollUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeTypeUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.doAnswer;


class AnimeApplicationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AnimeApplicationServiceTest.class);

    @Test
    void create() {
        AnimeRepository animeRepository = Mockito.mock(AnimeRepository.class);
        Mockito.when(animeRepository.save(Mockito.any(Anime.class))).thenAnswer(invocation -> {
            Anime anime = invocation.getArgument(0, Anime.class);
            anime.setAggregateId(AggregateId.generate());
            return anime;
        });

        AnimeCategoryRepository categoryRepository = Mockito.mock(AnimeCategoryRepository.class);
        TagRepository tagRepository = Mockito.mock(TagRepository.class);
        DistrictRepository districtRepository = Mockito.mock(DistrictRepository.class);
        AttachmentRepository attachmentRepository = Mockito.mock(AttachmentRepository.class);
        TypeRepository typeRepository = Mockito.mock(TypeRepository.class);

        IAnimeService animeService = new AnimeServiceImpl(animeRepository, categoryRepository, districtRepository, tagRepository, typeRepository);

        AnimeCreateCommand createRequest = new AnimeCreateCommand();
        createRequest.setName("凡人修仙传");
        createRequest.setAuthor("忘语");
        createRequest.setInstruction(
            "看机智的凡人小子韩立如何稳健发展、步步为营，战魔道、夺至宝、驰骋星海、快意恩仇，成为纵横三界的强者。他日仙界重相逢，一声道友尽沧桑。");
        createRequest.setPlotType("TV动画");
        createRequest.setOfficialWebsite("https://www.bilibili.com/bangumi/media/md28223043");
        createRequest.setDistrictId(1L);
        createRequest.setPremiereDate(LocalDate.now());
        createRequest.setPremiereDate(LocalDate.of(2022, 7, 5));

        // 创建一个标签
        createRequest.setTagIds(CollUtil.toList(1L));

        // 创建一个区域
        District district = new District();
        district.setName("中国");
        district.setCode("CN");
        district.setAggregateId(AggregateId.generate());
        createRequest.setDistrictId(1L);

        doAnswer(invocation -> Optional.of(district)).when(districtRepository).findById(Mockito.any());

        // 创建一个动画类别
        Category category = new Category();
        category.setPid(null);
        category.setName("言情");
        category.setOrderNo(1);
        category.setAggregateId(AggregateId.generate());

        doAnswer(invocation -> Optional.of(category)).when(categoryRepository).findById(Mockito.any());
        createRequest.setTypeId(1L);

        Attachment attachment = new Attachment();
        attachment.setAggregateId(AggregateId.generate());
        attachment.setPath("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9.jpg@450w_600h.webp");
        attachment.setFileName("95d2881427fd43431f6a696a05623675ecdce9d9.jpg");
        attachment.setFileSize(100000000000L);
        attachment.setMimeType(MimeTypeUtils.APPLICATION_XML_VALUE);

        doAnswer(invocation -> Optional.of(attachment)).when(attachmentRepository).findById(Mockito.any());
        createRequest.setCoverAttachmentId(attachment.getAggregateId().getId());

        Type type = new Type();
        type.setAggregateId(AggregateId.generate());
        type.setName("TV动画");

        doAnswer(invocation -> {
            log.info("查询：{}", invocation);
            return Optional.of(type);
        }).when(typeRepository).findById(Mockito.any());

        createRequest.setCompanyId(1L);

        //        AnimeCreator creator = AnimeMapper.INSTANCE.requestToCreator(createRequest);
        //        Anime anime = animeService.createAnime(creator);
        //        Assertions.assertNotNull(anime);
        //        Collection<Object> domainEvents = anime.domainEvents();
        //        Assertions.assertEquals(1, domainEvents.size());
    }
}
