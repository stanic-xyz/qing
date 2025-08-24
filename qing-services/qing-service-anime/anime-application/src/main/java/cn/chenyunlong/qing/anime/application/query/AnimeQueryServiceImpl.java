package cn.chenyunlong.qing.anime.application.query;

import cn.chenyunlong.qing.anime.application.dto.AnimeDTO;
import cn.chenyunlong.qing.anime.application.dto.PageResult;
import cn.chenyunlong.qing.anime.application.mapper.AnimeApplicationMapper;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeCategory;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.domain.anime.models.CategoryId;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 动漫查询服务实现
 *
 * @author chenyunlong
 * @since 2024-12-30
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimeQueryServiceImpl implements AnimeQueryService {

    private static final Logger log = LoggerFactory.getLogger(AnimeQueryServiceImpl.class);

    private final AnimeRepository animeRepository;
    private final AnimeCategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final AnimeApplicationMapper animeApplicationMapper;

    @Override
    public Optional<AnimeDTO> findById(AnimeId animeId) {
        log.debug("查询动漫，ID: {}", animeId);

        return animeRepository.findById(animeId)
                .map(this::convertToDTO);
    }

    @Override
    public List<AnimeDTO> findByIds(List<AnimeId> animeIds) {
        log.debug("批量查询动漫，ID列表: {}", animeIds);

        if (animeIds == null || animeIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 转换AnimeId列表为Long列表
        List<Long> ids = animeIds.stream()
                .map(AnimeId::getValue)
                .collect(Collectors.toList());

        return animeRepository.findByIds(ids)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<AnimeDTO> findPage(AnimeQuery query) {
        log.debug("分页查询动漫，查询条件: {}", query);

        query.validate();

        // 根据查询条件选择合适的仓储方法
        List<Anime> animes;
        long total;

        if (query.getCategoryId() != null) {
            // 按分类查询
            AnimeCategory category = new AnimeCategory(CategoryId.of(query.getCategoryId()), "category");
            animes = animeRepository.findByCategory(category);
            total = animeRepository.countByCategory(category);
        } else if (query.getOnShelf() != null && query.getOnShelf()) {
            // 查询上架动漫
            animes = animeRepository.findOnShelfAnimes();
            total = animeRepository.countOnShelf();
        } else if (query.getName() != null) {
            // 按名称模糊查询
            animes = animeRepository.findByNameLike(query.getName());
            total = animes.size(); // 简化处理
        } else {
            // 查询所有 - 使用上架动漫作为默认
            animes = animeRepository.findOnShelfAnimes();
            total = animeRepository.countOnShelf();
        }

        // 手动分页处理
        int offset = query.getOffset();
        int size = query.getSize();
        List<Anime> pagedAnimeList = animes.stream()
                .skip(offset)
                .limit(size)
                .toList();

        List<AnimeDTO> dtoList = pagedAnimeList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(dtoList, total, query.getPage(), query.getSize());
    }

    @Override
    public PageResult<AnimeDTO> findOnShelf(int page, int size) {
        log.debug("查询上架动漫，页码: {}, 大小: {}", page, size);

        AnimeQuery query = new AnimeQuery();
        query.setOnShelf(true);
        query.setDeleted(false);
        query.setPage(page);
        query.setSize(size);
        return findPage(query);
    }

    @Override
    public PageResult<AnimeDTO> findByCategory(Long categoryId, int page, int size) {
        log.debug("根据分类查询动漫，分类ID: {}, 页码: {}, 大小: {}", categoryId, page, size);

        AnimeQuery query = new AnimeQuery();
        query.setCategoryId(categoryId);
        query.setDeleted(false);
        query.setPage(page);
        query.setSize(size);
        return findPage(query);
    }

    @Override
    public PageResult<AnimeDTO> findByTag(Long tagId, int page, int size) {
        log.debug("根据标签查询动漫，标签ID: {}, 页码: {}, 大小: {}", tagId, page, size);

        AnimeQuery query = new AnimeQuery();
        query.setDeleted(false);
        query.setPage(page);
        query.setSize(size);
        return findPage(query);
    }

    @Override
    public PageResult<AnimeDTO> findByNameLike(String name, int page, int size) {
        log.debug("根据名称模糊查询动漫，名称: {}, 页码: {}, 大小: {}", name, page, size);

        if (!StringUtils.hasText(name)) {
            return PageResult.empty(page, size);
        }

        AnimeQuery query = new AnimeQuery();
        query.setName(name.trim());
        query.setPage(page);
        query.setSize(size);

        return findPage(query);
    }

    @Override
    public long countTotal() {
        log.debug("统计动漫总数");

        return animeRepository.countAll();
    }

    @Override
    public long countOnShelf() {
        log.debug("统计上架动漫数量");

        return animeRepository.countOnShelf();
    }

    @Override
    public long countByCategory(Long categoryId) {
        log.debug("统计分类动漫数量，分类ID: {}", categoryId);

        AnimeCategory category = new AnimeCategory(CategoryId.of(categoryId), "category");
        return animeRepository.countByCategory(category);
    }

    @Override
    public boolean existsByName(String name) {
        log.debug("检查动漫名称是否存在: {}", name);

        if (!StringUtils.hasText(name)) {
            return false;
        }

        return animeRepository.existsByName(name.trim());
    }

    @Override
    public boolean existsByNameExcludeId(String name, AnimeId excludeId) {
        log.debug("检查动漫名称是否存在（排除ID）: {}, 排除ID: {}", name, excludeId);

        if (!StringUtils.hasText(name)) {
            return false;
        }

        return animeRepository.existsByNameExcludeId(name.trim(), excludeId);
    }

    @Override
    public List<AnimeDTO> findPopular(int limit) {
        log.debug("获取热门动漫，限制数量: {}", limit);

        if (limit <= 0) {
            return Collections.emptyList();
        }

        // 获取上架动漫并限制数量
        return animeRepository.findOnShelfAnimes()
                .stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnimeDTO> findLatest(int limit) {
        log.debug("获取最新动漫，限制数量: {}", limit);

        if (limit <= 0) {
            return Collections.emptyList();
        }

        AnimeQuery query = new AnimeQuery();
        query.setOnShelf(true);
        query.setDeleted(false);
        query.setPage(1);
        query.setSize(limit);

        return animeRepository.findByQuery(query)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnimeDTO> findRecommended(int limit) {
        log.debug("获取推荐动漫，限制数量: {}", limit);

        if (limit <= 0) {
            return Collections.emptyList();
        }

        // 推荐逻辑：综合考虑播放热度和更新时间
        AnimeQuery query = new AnimeQuery();
        query.setOnShelf(true);
        query.setDeleted(false);
        query.setPage(1);
        query.setSize(limit * 2); // 获取更多数据用于推荐算法

        return animeRepository.findByQuery(query)
                .stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnimeDTO> listAll() {
        return animeRepository.listAll().stream().map(this::convertToDTO).toList();
    }

    /**
     * 将动漫聚合根转换为DTO
     *
     * @param anime 动漫聚合根
     * @return 动漫DTO
     */
    private AnimeDTO convertToDTO(Anime anime) {
        if (anime == null) {
            return null;
        }

        // 获取分类信息
        String categoryName = null;
        if (anime.getAnimeCategory() != null) {
            // 暂时使用分类名称，后续需要根据实际的Category实体调整
            categoryName = anime.getAnimeCategory().name();
        }

        // 获取标签信息
        List<String> tagNames = Collections.emptyList();
        if (anime.getTags() != null && !anime.getTags().isEmpty()) {
            tagNames = anime.getTags().asList();
        }

        AnimeDTO animeDTO = animeApplicationMapper.mapToDto(anime);
        animeDTO.setTags(tagNames);
        return animeDTO;
    }
}
