package cn.chenyunlong.qing.domain.anime.attachement.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import cn.chenyunlong.qing.domain.anime.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.anime.attachement.dto.query.AttachmentQuery;
import cn.chenyunlong.qing.domain.anime.attachement.dto.updater.AttachmentUpdater;
import cn.chenyunlong.qing.domain.anime.attachement.dto.vo.AttachmentVO;
import cn.chenyunlong.qing.domain.anime.attachement.mapper.AttachmentMapper;
import cn.chenyunlong.qing.domain.anime.attachement.repository.AttachmentRepository;
import cn.chenyunlong.qing.domain.anime.attachement.service.IAttachmentService;
import jakarta.annotation.Resource;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentServiceImpl implements IAttachmentService {

    @Resource
    private final AttachmentRepository attachmentRepository;

    /**
     * createImpl
     */
    @Override
    public Long createAttachment(AttachmentCreator creator) {
        Optional<Attachment> attachment = EntityOperations.doCreate(attachmentRepository)
                                              .create(() -> AttachmentMapper.INSTANCE.dtoToEntity(creator))
                                              .update(Attachment::init)
                                              .execute();
        return attachment.isPresent() ? attachment.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateAttachment(Long id, AttachmentUpdater updater) {
        EntityOperations.doUpdate(attachmentRepository)
            .loadById(id)
            .update(updater::updateAttachment)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validAttachment(Long id) {
        EntityOperations.doUpdate(attachmentRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidAttachment(Long id) {
        EntityOperations.doUpdate(attachmentRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public AttachmentVO findById(Long id) {
        Optional<Attachment> attachment = attachmentRepository.findById(id);
        return attachment.map(AttachmentMapper.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    @Override
    public void deleteById(Long id) {
        attachmentRepository.deleteById(id);
    }

    /**
     * findByPage
     */
    @Override
    public Page<AttachmentVO> findByPage(PageRequestWrapper<AttachmentQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return attachmentRepository.findAll(pageRequest).map(AttachmentMapper.INSTANCE::entityToVo);
    }
}
