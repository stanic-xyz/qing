package cn.chenyunlong.qing.anime.infrastructure.adapter;

import cn.chenyunlong.qing.anime.application.dto.AnimeDTO;
import cn.chenyunlong.qing.anime.application.dto.PageResult;
import cn.chenyunlong.qing.anime.application.port.AnimeQueryPort;
import cn.chenyunlong.qing.anime.application.query.AnimeQuery;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeInfrastructureMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AnimeEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.AnimeJpaRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.hutool.core.util.StrUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 动漫查询端口适配器
 * 实现基于JPA的动漫查询功能
 *
 * @author chenyunlong
 * @since 2024-12-30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AnimeQueryAdapter extends EntityOperations implements AnimeQueryPort {

    private final AnimeJpaRepository animeJpaRepository;
    private final AnimeInfrastructureMapper mapper;

    @Override
    public Optional<AnimeDTO> findById(AnimeId animeId) {
        log.debug("查询动漫，ID: {}", animeId);

        return animeJpaRepository.findById(animeId.id())
                .filter(entity -> !entity.getIsDeleted())
                .map(this::convertToDTO);
    }

    @Override
    public List<AnimeDTO> findByIds(List<AnimeId> animeIds) {
        log.debug("批量查询动漫，ID列表: {}", animeIds);

        if (animeIds == null || animeIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> ids = animeIds.stream()
                .map(AnimeId::id)
                .collect(Collectors.toList());

        return animeJpaRepository.findAllById(ids)
                .stream()
                .filter(entity -> !entity.getIsDeleted())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<AnimeDTO> findPage(AnimeQuery query) {
        log.debug("分页查询动漫，查询条件: {}", query);

        Specification<AnimeEntity> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);

        Page<AnimeEntity> page = animeJpaRepository.findAll(spec, pageable);

        List<AnimeDTO> dtos = page.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(dtos, page.getTotalElements(), query.getPage(), query.getSize());
    }

    @Override
    public long countByQuery(AnimeQuery query) {
        log.debug("统计查询结果数量，查询条件: {}", query);

        Specification<AnimeEntity> spec = buildSpecification(query);
        return animeJpaRepository.count(spec);
    }

    @Override
    public PageResult<AnimeDTO> findOnShelf(int page, int size) {
        log.debug("查询上架动漫，页码: {}, 大小: {}", page, size);

        AnimeQuery query = AnimeQuery.builder()
                .onShelf(true)
                .deleted(false)
                .page(page)
                .size(size)
                .build();

        return findPage(query);
    }

    @Override
    public PageResult<AnimeDTO> findByCategory(Long categoryId, int page, int size) {
        log.debug("根据分类查询动漫，分类ID: {}, 页码: {}, 大小: {}", categoryId, page, size);

        AnimeQuery query = AnimeQuery.builder()
                .categoryId(categoryId)
                .deleted(false)
                .page(page)
                .size(size)
                .build();

        return findPage(query);
    }

    @Override
    public PageResult<AnimeDTO> findByTag(Long tagId, int page, int size) {
        log.debug("根据标签查询动漫，标签ID: {}, 页码: {}, 大小: {}", tagId, page, size);

        AnimeQuery query = AnimeQuery.builder()
                .tagIds(Collections.singletonList(tagId))
                .deleted(false)
                .page(page)
                .size(size)
                .build();

        return findPage(query);
    }

    @Override
    public PageResult<AnimeDTO> findByNameLike(String name, int page, int size) {
        log.debug("根据名称模糊查询动漫，名称: {}, 页码: {}, 大小: {}", name, page, size);

        if (!StringUtils.hasText(name)) {
            return PageResult.empty(page, size);
        }

        AnimeQuery query = AnimeQuery.builder()
                .name(name.trim())
                .deleted(false)
                .page(page)
                .size(size)
                .build();

        return findPage(query);
    }

    @Override
    public long countTotal() {
        log.debug("统计动漫总数");
        return animeJpaRepository.countByIsDeletedFalse();
    }

    @Override
    public long countOnShelf() {
        log.debug("统计上架动漫数量");
        return animeJpaRepository.countByIsOnShelfTrueAndIsDeletedFalse();
    }

    @Override
    public long countByCategory(Long categoryId) {
        log.debug("统计指定分类的动漫数量，分类ID: {}", categoryId);
        return animeJpaRepository.countByTypeIdAndIsDeletedFalse(categoryId);
    }

    @Override
    public boolean existsByName(String name) {
        log.debug("检查动漫名称是否存在，名称: {}", name);
        return animeJpaRepository.existsByNameAndIsDeletedFalse(name);
    }

    @Override
    public boolean existsByNameExcludeId(String name, AnimeId excludeId) {
        log.debug("检查动漫名称是否存在（排除指定ID），名称: {}, 排除ID: {}", name, excludeId);
        return animeJpaRepository.existsByNameAndIdNotAndIsDeletedFalse(name, excludeId.id());
    }

    @Override
    public List<AnimeDTO> findPopular(int limit) {
        log.debug("获取热门动漫，限制数量: {}", limit);

        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "playHeat"));

        return animeJpaRepository.findByIsOnShelfTrueAndIsDeletedFalse(pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnimeDTO> findLatest(int limit) {
        log.debug("获取最新动漫，限制数量: {}", limit);

        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createTime"));

        return animeJpaRepository.findByIsOnShelfTrueAndIsDeletedFalse(pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnimeDTO> findRecommended(int limit) {
        log.debug("获取推荐动漫，限制数量: {}", limit);

        // 简单的推荐算法：按播放热度和创建时间综合排序
        Pageable pageable = PageRequest.of(0, limit,
                Sort.by(Sort.Direction.DESC, "playHeat")
                        .and(Sort.by(Sort.Direction.DESC, "createTime")));

        return animeJpaRepository.findByIsOnShelfTrueAndIsDeletedFalse(pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 构建查询规范
     *
     * @param query 查询条件
     * @return 查询规范
     */
    private Specification<AnimeEntity> buildSpecification(AnimeQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 基础过滤条件
            if (query.getDeleted() != null) {
                predicates.add(criteriaBuilder.equal(root.get("deleted"), query.getDeleted()));
            } else {
                // 默认不查询已删除的记录
                predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
            }

            // 上架状态
            if (query.getOnShelf() != null) {
                predicates.add(criteriaBuilder.equal(root.get("onShelf"), query.getOnShelf()));
            }

            // 动漫ID列表
            if (query.getAnimeIds() != null && !query.getAnimeIds().isEmpty()) {
                predicates.add(root.get("id").in(query.getAnimeIds()));
            }

            // 名称模糊查询
            if (StrUtil.isNotBlank(query.getName())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + query.getName().toLowerCase() + "%"));
            }

            // 分类ID
            if (query.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("typeId"), query.getCategoryId()));
            }

            // 标签查询（简单实现，实际可能需要更复杂的逻辑）
            if (query.getTagIds() != null && !query.getTagIds().isEmpty()) {
                // 这里需要根据实际的标签存储方式来实现
                // 如果标签是以逗号分隔的字符串存储，可以使用like查询
                for (Long tagId : query.getTagIds()) {
                    predicates.add(criteriaBuilder.like(
                            root.get("tags"),
                            "%" + tagId + "%"));
                }
            }

            // 播放状态
            if (StrUtil.isNotBlank(query.getPlayStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("playStatus"), query.getPlayStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 构建分页和排序
     *
     * @param query 查询条件
     * @return 分页对象
     */
    private Pageable buildPageable(AnimeQuery query) {
        Sort sort = Sort.by(
                query.getSortDirection() == AnimeQuery.SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                query.getSortBy());

        return PageRequest.of(query.getPage() - 1, query.getSize(), sort);
    }

    /**
     * 转换为DTO
     *
     * @param entity JPA实体
     * @return 动漫DTO
     */
    private AnimeDTO convertToDTO(AnimeEntity entity) {
        Anime anime = mapper.toDomain(entity);
        return AnimeDTO.from(anime);
    }
}
