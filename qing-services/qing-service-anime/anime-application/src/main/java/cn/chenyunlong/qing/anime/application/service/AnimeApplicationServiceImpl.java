package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.anime.domain.attachement.AttachmentRepository;
import cn.chenyunlong.qing.anime.domain.district.repository.DistrictRepository;
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
