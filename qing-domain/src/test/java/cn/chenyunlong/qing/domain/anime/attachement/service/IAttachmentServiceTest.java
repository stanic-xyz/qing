package cn.chenyunlong.qing.domain.anime.attachement.service;

import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import cn.chenyunlong.qing.domain.anime.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.anime.attachement.repository.AttachmentRepository;
import cn.chenyunlong.qing.domain.anime.attachement.service.impl.AttachmentServiceImpl;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.MimeTypeUtils;


class IAttachmentServiceTest {

    @Test
    void createAttachment() {
        AttachmentRepository attachmentRepository = Mockito.mock(AttachmentRepository.class);
        Attachment returned = new Attachment();
        long attachmentId = IdUtil.getSnowflakeNextId();
        returned.setId(attachmentId);
        Mockito.doReturn(returned).when(attachmentRepository).save(Mockito.any());

        IAttachmentService attachmentService = new AttachmentServiceImpl(attachmentRepository);
        Long id = attachmentService.createAttachment(AttachmentCreator.builder()
                                                         .path("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9.jpg@450w_600h.webp")
                                                         .fileId(IdUtil.getSnowflakeNextId())
                                                         .fileName("95d2881427fd43431f6a696a05623675ecdce9d9.jpg")
                                                         .fileSize(100000000000L)
                                                         .mimeType(MimeTypeUtils.APPLICATION_XML_VALUE)
                                                         .build());
        Assertions.assertEquals(attachmentId, id, "创建的附件id不正确");
    }
}
