package cn.chenyunlong.qing.domain.anime.attachement.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.anime.attachement.dto.query.AttachmentQuery;
import cn.chenyunlong.qing.domain.anime.attachement.dto.updater.AttachmentUpdater;
import cn.chenyunlong.qing.domain.anime.attachement.dto.vo.AttachmentVO;
import org.springframework.data.domain.Page;

public interface IAttachmentService {

    /**
     * create
     */
    Long createAttachment(AttachmentCreator creator);

    /**
     * update
     */
    void updateAttachment(AttachmentUpdater updater);

    /**
     * valid
     */
    void validAttachment(Long id);

    /**
     * invalid
     */
    void invalidAttachment(Long id);

    /**
     * findById
     */
    AttachmentVO findById(Long id);

    /**
     * findByPage
     */
    Page<AttachmentVO> findByPage(PageRequestWrapper<AttachmentQuery> query);
}
