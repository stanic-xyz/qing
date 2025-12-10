package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeCategory;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeInfrastructureMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AnimeEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.AnimeJpaRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 动漫仓储JPA实现
 * 实现领域层定义的动漫仓储接口
 *
 * @author chenyunlong
 * @since 2024-12-30
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AnimeRepositoryJpaImpl implements AnimeRepository {

    private final AnimeJpaRepository animeJpaRepository;
    private final AnimeInfrastructureMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Anime> findById(AnimeId animeId) {
        log.debug("根据ID查找动漫，ID: {}", animeId);

        return animeJpaRepository.findById(animeId.id())
                .filter(entity -> !entity.getIsDeleted())
                .map(mapper::toDomain);
    }

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public List<Anime> findByIds(@NonNull List<Long> animeIds) {
        log.debug("根据ID列表批量查找动漫，ID列表: {}", animeIds);

        if (CollUtil.isEmpty(animeIds)) {
            return CollUtil.toList();
        }

        return animeJpaRepository.findAllById(animeIds)
                .stream()
                .filter(entity -> !entity.getIsDeleted())
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<Anime> findByAnimeIds(@NonNull List<AnimeId> animeIds) {
        log.debug("根据AnimeId列表批量查找动漫，ID列表: {}", animeIds);

        List<Long> ids = animeIds.stream()
                .map(AnimeId::id)
                .collect(Collectors.toList());
        return findByIds(ids);
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<Anime> findByCategory(@NonNull AnimeCategory category) {
        log.debug("根据分类查找动漫，分类: {}", category);

        return animeJpaRepository.findByCategoryId(category.id().id())
                .stream()
                .filter(entity -> !entity.getIsDeleted())
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<Anime> findOnShelfAnimes() {
        log.debug("查找已上架的动漫");

        return animeJpaRepository.findByIsOnShelfTrueAndIsDeletedFalse(null)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<Anime> findByTag(@NonNull String tag) {
        log.debug("根据标签查找动漫，标签: {}", tag);

        // 这里需要根据实际的标签存储方式来实现
        // 暂时返回空列表
        return CollUtil.toList();
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<Anime> findByNameLike(@NonNull String namePattern) {
        log.debug("根据名称模糊查找动漫，名称模式: {}", namePattern);

        return animeJpaRepository.findByNameContainingIgnoreCase(namePattern, null)
                .getContent()
                .stream()
                .filter(entity -> !entity.getIsDeleted())
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Anime save(@NonNull Anime anime) {
        log.debug("保存动漫，ID: {}", anime.getId());
        AnimeEntity entity = mapper.toEntity(anime);
        AnimeEntity savedEntity = animeJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAll() {
        log.debug("统计动漫总数");
        return animeJpaRepository.countByIsDeletedFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public long countOnShelf() {
        log.debug("统计已上架动漫数量");
        return animeJpaRepository.countByIsOnShelfTrueAndIsDeletedFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCategory(@NonNull AnimeCategory category) {
        log.debug("统计指定分类的动漫数量，分类: {}", category);
        return animeJpaRepository.countByTypeIdAndIsDeletedFalse(category.id().id());
    }

    @Override
    public void deleteById(@NonNull AnimeId animeId) {
        log.debug("删除动漫，ID: {}", animeId);
        animeJpaRepository.deleteById(animeId.id());
    }

    @Override
    public void deleteByIds(@NonNull List<AnimeId> animeIds) {
        log.debug("批量删除动漫，ID列表: {}", animeIds);

        List<Long> ids = animeIds.stream()
                .map(AnimeId::id)
                .collect(Collectors.toList());
        animeJpaRepository.deleteAllById(ids);
    }

    @Override
    public AnimeId nextId() {
        return AnimeId.of(IdUtil.getSnowflakeNextId());
    }

    @Override
    public boolean existsByNameExcludeId(String cleanName, @NonNull AnimeId animeId) {
        log.debug("检查动漫名称是否存在（排除指定ID），名称: {}, 排除ID: {}", cleanName, animeId);
        return animeJpaRepository.existsByNameAndIdNotAndIsDeletedFalse(cleanName, animeId.id());
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<Anime> findByQuery(@NonNull Object query) {
        log.debug("根据查询条件查找动漫，查询条件: {}", query);
        // 这里应该根据具体的查询对象类型来实现
        // 暂时返回空列表
        return CollUtil.toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByQuery(@NonNull Object query) {
        log.debug("根据查询条件统计动漫数量，查询条件: {}", query);
        // 这里应该根据具体的查询对象类型来实现
        // 暂时返回总数
        return animeJpaRepository.countByIsDeletedFalse();
    }

    @Override
    public boolean existsByName(String name) {
        return animeJpaRepository.existsByName(name);
    }

    @Override
    public List<Anime> listAll() {
        List<AnimeEntity> entityList = animeJpaRepository.findAll();
        return entityList.stream().map(mapper::toDomain).toList();
    }
}
