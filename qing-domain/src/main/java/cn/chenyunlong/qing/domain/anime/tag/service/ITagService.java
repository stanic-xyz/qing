package cn.chenyunlong.qing.domain.anime.tag.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.tag.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.tag.dto.query.TagQuery;
import cn.chenyunlong.qing.domain.anime.tag.dto.updater.TagUpdater;
import cn.chenyunlong.qing.domain.anime.tag.dto.vo.TagVO;
import org.springframework.data.domain.Page;

public interface ITagService {

    /**
     * create
     */
    Long createTag(TagCreator creator);

    /**
     * update
     */
    void updateTag(TagUpdater updater);

    /**
     * valid
     */
    void validTag(Long id);

    /**
     * invalid
     */
    void invalidTag(Long id);

    /**
     * findById
     */
    TagVO findById(Long id);

    /**
     * findByPage
     */
    Page<TagVO> findByPage(PageRequestWrapper<TagQuery> query);
}
