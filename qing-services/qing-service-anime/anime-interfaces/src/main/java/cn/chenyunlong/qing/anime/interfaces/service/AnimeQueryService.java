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
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public AnimeVO getById(Long id) {
        Optional<AnimeEntity> animeEntity = animeJpaRepository.findById(id);
        if (animeEntity.isEmpty()) {
            throw new RuntimeException("动漫信息不存在");
        }
        return AnimeMapper.INSTANCE.entityToVo(AnimeMapper.INSTANCE.entityToDomain(animeEntity.get()));
    }

    public List<AnimeVO> page(Integer page, Integer size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<AnimeEntity> animeEntityPage = animeJpaRepository.findAll(pageable);
        List<AnimeEntity> animeEntityList = animeEntityPage.getContent();

        // 如果有关键词，在内存中过滤（临时解决方案）
        if (StrUtil.isNotBlank(keyword)) {
            animeEntityList = animeEntityList.stream()
                .filter(entity -> entity.getName() != null
                    && entity.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        }

        return animeEntityList.stream()
            .map(AnimeMapper.INSTANCE::entityToDomain)
            .map(AnimeMapper.INSTANCE::entityToVo)
            .toList();
    }

    public List<AnimeVO> listByCategory(Long categoryId) {
        // 使用 findAll 然后在内存中过滤（临时解决方案）
        List<AnimeEntity> allEntities = animeJpaRepository.findAll();
        List<AnimeEntity> animeEntityList = allEntities.stream()
            .filter(entity -> entity.getTypeId() != null
                && entity.getTypeId().equals(categoryId))
            .toList();

        return animeEntityList.stream()
            .map(AnimeMapper.INSTANCE::entityToDomain)
            .map(AnimeMapper.INSTANCE::entityToVo)
            .toList();
    }
}
