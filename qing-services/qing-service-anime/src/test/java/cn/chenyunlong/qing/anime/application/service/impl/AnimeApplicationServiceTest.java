package cn.chenyunlong.qing.anime.application.service.impl;

import cn.chenyunlong.qing.anime.application.service.AnimeDomainService;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.CreatorAnimeCommand;
import cn.chenyunlong.qing.anime.domain.anime.enums.District;
import cn.chenyunlong.qing.anime.domain.anime.event.AnimeCreatedEvent;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.domain.anime.models.CategoryId;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.anime.domain.attachement.Attachment;
import cn.chenyunlong.qing.anime.domain.attachement.AttachmentId;
import cn.chenyunlong.qing.anime.domain.type.Type;
import cn.chenyunlong.qing.anime.domain.type.TypeId;
import cn.chenyunlong.qing.anime.domain.type.repository.TypeRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class AnimeApplicationServiceTest {

    @InjectMocks
    private AnimeDomainService animeService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private AnimeRepository animeRepository;
    @Mock
    private AnimeCategoryRepository categoryRepository;
    @Mock
    private TypeRepository typeRepository;

    private static final Logger log = LoggerFactory.getLogger(AnimeApplicationServiceTest.class);

    @Test
    void create() {
        Mockito.when(animeRepository.save(Mockito.any(Anime.class))).thenAnswer(invocation -> {
            Anime anime = invocation.getArgument(0, Anime.class);
            anime.setId(AnimeId.of(IdUtil.getSnowflakeNextId()));
            return anime;
        });

        CreatorAnimeCommand command = new CreatorAnimeCommand();
        command.setName("凡人修仙传");
        command.setAuthor("忘语");
        command.setInstruction(
                "看机智的凡人小子韩立如何稳健发展、步步为营，战魔道、夺至宝、驰骋星海、快意恩仇，成为纵横三界的强者。他日仙界重相逢，一声道友尽沧桑。");
        command.setPlotType("TV动画");
        command.setOfficialWebsite("https://www.bilibili.com/bangumi/media/md28223043");
        command.setDistrict(District.CHINA);

        // 创建一个标签
        command.setTagIds(CollUtil.toList(1L));

        // 创建一个区域
        command.setDistrict(District.CHINA);

        // 创建一个动画类别
        Category category = new Category();
        category.setPid(null);
        category.setName("言情");
        category.setOrderNo(1);
        category.setId(CategoryId.of(IdUtil.getSnowflakeNextId()));

        doAnswer(invocation -> Optional.of(category)).when(categoryRepository).findById(Mockito.any());
        command.setTypeId(TypeId.of(1L));


        Attachment attachment = Attachment.createAttachment(AttachmentId.of(IdUtil.getSnowflakeNextId()),
                "95d2881427fd43431f6a696a05623675ecdce9d9.jpg",
                1L,
                "https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9.jpg@450w_600h.webp",
                1L,
                "contentHash", Instant.now()
        );
        command.setCover("cover.avatar");

        Type type = Type.createType(TypeId.of(IdUtil.getSnowflakeNextId()), "TV动画", "TV动画就是TV动画！");

        doAnswer(invocation -> {
            log.info("查询：{}", invocation);
            return Optional.of(type);
        }).when(typeRepository).findById(Mockito.any());
        command.setCompanyId(1L);

        Anime anime = animeService.createAnime(command);
        Assertions.assertNotNull(anime);
        Collection<Object> domainEvents = anime.domainEvents();
        Assertions.assertEquals(1, domainEvents.size());
        Assertions.assertInstanceOf(AnimeCreatedEvent.class, CollUtil.get(domainEvents, 0));
    }
}
