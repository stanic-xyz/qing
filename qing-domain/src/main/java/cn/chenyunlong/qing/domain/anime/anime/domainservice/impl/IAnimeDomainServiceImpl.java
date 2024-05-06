package cn.chenyunlong.qing.domain.anime.anime.domainservice.impl;

import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.IAnimeDomainService;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.AnimeCreateContext;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.TagRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import cn.chenyunlong.qing.domain.anime.attachement.repository.AttachmentRepository;
import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IAnimeDomainServiceImpl implements IAnimeDomainService {

    private final IAnimeService animeService;
    private final AnimeCategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final DistrictRepository districtRepository;
    private final AttachmentRepository attachmentRepository;

    /**
     * 创建动漫信息
     */
    @Override
    public Long createAnime(AnimeCreateRequest request) {
        // 创建动漫信息，给你看了，你就是不会
        List<Tag> tagList = tagRepository.findAllById(request.getTagIds());
        District district = districtRepository.findById(request.getDistrictId()).orElseThrow();
        AnimeCategory animeCategory = categoryRepository.findById(request.getTypeId()).orElseThrow();
        Attachment attachment = attachmentRepository.findById(request.getCoverUrlAttachmentId()).orElseThrow();
        AnimeCreateContext context = AnimeCreateContext.createContext(request, tagList, district, animeCategory, attachment);
        return animeService.createAnime(context);
    }

}
