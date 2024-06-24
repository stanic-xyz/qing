package cn.chenyunlong.qing.domain.anime.domainservice;

import static org.mockito.Mockito.doAnswer;

import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.IAnimeDomainService;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.impl.IAnimeDomainServiceImpl;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.AnimeCreateContext;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.TagRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import cn.chenyunlong.qing.domain.anime.anime.service.impl.AnimeServiceImpl;
import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import cn.chenyunlong.qing.domain.anime.attachement.repository.AttachmentRepository;
import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.MimeTypeUtils;


class IAnimeDomainServiceTest {

    @Test
    void create() {
        AnimeRepository animeRepository = new AnimeRepository() {

            @Override
            public Optional<Anime> findById(Long animeId) {
                return Optional.empty();
            }

            @Override
            public Anime save(Anime entity) {
                entity.setId(IdUtil.getSnowflakeNextId());
                return entity;
            }

            @Override
            public void deleteById(Long id) {

            }

            @Override
            public Page<Anime> findAll(PageRequest pageRequest) {
                return null;
            }

            @Override
            public List<Anime> findByIds(List<Long> ids) {
                return List.of();
            }

            @Override
            public void deleteAllByIds(List<Long> ids) {

            }

            @Override
            public void saveAll(List<Anime> domainList) {
            }
        };

        AnimeCategoryRepository categoryRepository = Mockito.mock(AnimeCategoryRepository.class);
        TagRepository tagRepository = Mockito.mock(TagRepository.class);
        DistrictRepository districtRepository = Mockito.mock(DistrictRepository.class);
        AttachmentRepository attachmentRepository = Mockito.mock(AttachmentRepository.class);
        IAnimeDomainService animeDomainService;
        Validator validator = Mockito.mock(Validator.class);
        IAnimeService animeService = new AnimeServiceImpl(animeRepository, categoryRepository, districtRepository, validator);

        animeDomainService = new IAnimeDomainServiceImpl(animeService, categoryRepository, tagRepository, districtRepository, attachmentRepository);

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
        createRequest.setTagIds(CollUtil.toList(1L));

        // 创建一个区域
        District district = new District();
        district.setName("中国");
        district.setCode("CN");
        district.setId(1L);
        createRequest.setDistrictId(1L);

        doAnswer(invocation -> Optional.of(district)).when(districtRepository).findById(district.getId());

        // 创建一个动画类别
        AnimeCategory animeCategory = new AnimeCategory();
        animeCategory.setPid(null);
        animeCategory.setName("言情");
        animeCategory.setOrderNo(1);
        animeCategory.setId(1L);

        doAnswer(invocation -> Optional.of(animeCategory)).when(categoryRepository).findById(animeCategory.getId());
        createRequest.setTypeId(1L);

        Attachment attachment = new Attachment();
        attachment.setId(1L);
        attachment.setPath("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9.jpg@450w_600h.webp");
        attachment.setFileName("95d2881427fd43431f6a696a05623675ecdce9d9.jpg");
        attachment.setFileSize(100000000000L);
        attachment.setMimeType(MimeTypeUtils.APPLICATION_XML_VALUE);

        doAnswer(invocation -> Optional.of(attachment)).when(attachmentRepository).findById(attachment.getId());

        createRequest.setCoverUrlAttachmentId(attachment.getId());
        createRequest.setCoverUrl("");

        AnimeCreateContext createContext = animeDomainService.createAnime(createRequest);
        Assertions.assertNotNull(createContext);
    }
}
