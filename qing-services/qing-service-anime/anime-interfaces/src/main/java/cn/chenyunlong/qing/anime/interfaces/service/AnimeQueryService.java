package cn.chenyunlong.qing.anime.interfaces.service;

import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeCategoryTreeVO;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeCategoryMapper;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AnimeEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.CategoryEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.AnimeCategoryJpaRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.AnimeJpaRepository;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimeQueryService {

    private final AnimeCategoryJpaRepository animeCategoryJpaRepository;
    private final AnimeJpaRepository animeJpaRepository;

    public List<AnimeCategoryTreeVO> tree() {
        List<CategoryEntity> categoryEntityList = animeCategoryJpaRepository.findAll();

        List<CategoryEntity> entityList = categoryEntityList.stream().filter(categoryEntity -> categoryEntity.getPid() == Category.ROOT_PID)
            .toList();
        return buildCategoryTree(entityList, categoryEntityList);
    }

    private List<AnimeCategoryTreeVO> buildCategoryTree(List<CategoryEntity> parentList, List<CategoryEntity> categoryEntityList) {
        if (CollUtil.isEmpty(parentList)) {
            return CollUtil.toList();
        }
        List<Long> parentIds = parentList.stream().map(CategoryEntity::getId).toList();

        Map<Long, List<CategoryEntity>> parentGroup = categoryEntityList.stream().filter(categoryEntity -> CollUtil.contains(parentIds, categoryEntity.getPid()))
            .collect(Collectors.groupingBy(CategoryEntity::getPid));

        return parentList.stream().map(categoryEntity -> {
            List<CategoryEntity> childCategories = parentGroup.getOrDefault(categoryEntity.getId(), CollUtil.toList());
            AnimeCategoryTreeVO animeCategoryTreeVO = AnimeCategoryMapper.INSTANCE.entityToTreeVo(categoryEntity);
            animeCategoryTreeVO.setChild(buildCategoryTree(childCategories, categoryEntityList));
            return animeCategoryTreeVO;
        }).toList();
    }

    public List<AnimeVO> listAll() {
        List<AnimeEntity> animeEntityList = animeJpaRepository.findAll();
        return animeEntityList.stream().map(AnimeMapper.INSTANCE::entityToDomain).map(AnimeMapper.INSTANCE::entityToVo).toList();
    }
}
