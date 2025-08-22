package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.qing.anime.domain.episode.Episode;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.EpisodeEntity;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.infrastructure.converter.DomainEntityConverter;
import org.springframework.stereotype.Component;

@Component
public class EpisodeConverter implements DomainEntityConverter<Episode, EpisodeEntity> {

    @Override
    public EpisodeEntity toEntity(Episode episode) {
        if (episode == null) {
            return null;
        }
        
        EpisodeEntity entity = new EpisodeEntity();
        if (episode.getId() != null) {
            entity.setId(episode.getId().getId());
        }
        entity.setName(episode.getName());
        entity.setAnimeId(episode.getAnimeId());
        entity.setPlayListId(episode.getPlayListId());
        entity.setDescription(episode.getDescription());
        entity.setPlayUrl(episode.getPlayUrl());
        entity.setEpisodeNumber(episode.getEpisodeNumber());
        
        // 设置基础字段
        entity.setValidStatus(episode.getValidStatus());
        entity.setVersion(episode.getVersion());
        // createdAt和updatedAt由JPA自动管理，不需要手动设置
        
        return entity;
    }

    @Override
    public Episode toDomain(EpisodeEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Episode episode = new Episode();
        episode.setId(new AggregateId(entity.getId()));
        episode.setName(entity.getName());
        episode.setAnimeId(entity.getAnimeId());
        episode.setPlayListId(entity.getPlayListId());
        episode.setDescription(entity.getDescription());
        episode.setPlayUrl(entity.getPlayUrl());
        episode.setEpisodeNumber(entity.getEpisodeNumber());
        
        // 设置基础字段
        if (entity.getValidStatus() != null) {
            episode.setValidStatus(entity.getValidStatus());
        }
        episode.setVersion(entity.getVersion());
        // 暂时跳过时间字段设置，等待BaseAggregate修复setter方法
        // episode.setCreatedAt(entity.getCreatedAt());
        // episode.setUpdatedAt(entity.getUpdatedAt());
        
        return episode;
    }
}