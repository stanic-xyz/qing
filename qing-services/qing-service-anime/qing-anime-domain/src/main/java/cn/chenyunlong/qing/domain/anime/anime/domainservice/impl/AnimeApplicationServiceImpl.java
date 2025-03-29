package cn.chenyunlong.qing.domain.anime.anime.domainservice.impl;

import cn.chenyunlong.qing.domain.anime.anime.domainservice.AnimeApplicationService;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.TagRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import cn.chenyunlong.qing.domain.anime.attachement.AttachmentRepository;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeApplicationServiceImpl implements AnimeApplicationService {

    private final IAnimeService animeService;

    @Resource
    private final AnimeCategoryRepository categoryRepository;

    @Resource
    private final TagRepository tagRepository;

    @Resource
    private final DistrictRepository districtRepository;

    @Resource
    private final AttachmentRepository attachmentRepository;



}
