package cn.chenyunlong.qing.domain.anime.anime.domainservice.impl;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.IAnimeDomainService;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.AnimeCreateContext;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.TagRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import cn.chenyunlong.qing.domain.anime.attachement.repository.AttachmentRepository;
import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IAnimeDomainServiceImpl implements IAnimeDomainService {

    private final IAnimeService animeService;

    @Resource
    private final AnimeCategoryRepository categoryRepository;

    @Resource
    private final TagRepository tagRepository;

    @Resource
    private final DistrictRepository districtRepository;

    @Resource
    private final AttachmentRepository attachmentRepository;

    /**
     * 创建动漫信息
     */
    @Override
    public AnimeCreateContext createAnime(AnimeCreateRequest request) {
        // 创建动漫信息，给你看了，你就是不会
        List<Tag> tagList = tagRepository.findByIds(request.getTagIds());
        District district = districtRepository.findById(request.getDistrictId()).orElseThrow(() -> new NotFoundException("地区不存在"));
        AnimeCategory animeCategory = categoryRepository.findById(request.getTypeId()).orElseThrow(() -> new NotFoundException("类型不存在"));
        // Attachment attachment = attachmentRepository.findById(request.getCoverUrlAttachmentId()).orElseThrow(() -> new NotFoundException("封面附件不存在"));
        AnimeCreateContext context = AnimeCreateContext.createContext(request, tagList, district, animeCategory, null);
        animeService.createAnime(context);
        return context;
    }

}
