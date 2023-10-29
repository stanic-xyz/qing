package cn.chenyunlong.qing.domain.attachement.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.attachement.dto.query.AttachmentQuery;
import cn.chenyunlong.qing.domain.attachement.dto.updater.AttachmentUpdater;
import cn.chenyunlong.qing.domain.attachement.dto.vo.AttachmentVO;
import java.lang.Long;
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
