package cn.chenyunlong.qing.domain.anime.attachement.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.anime.attachement.dto.creator.AttachmentCreator;
import cn.chenyunlong.qing.domain.anime.attachement.dto.query.AttachmentQuery;
import cn.chenyunlong.qing.domain.anime.attachement.dto.vo.AttachmentVO;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.MimeTypeUtils;

class IAttachmentServiceTest extends AbstractDomainTests {

    @Autowired
    private IAttachmentService attachmentService;

    @Test
    void createAttachment() {
        Long attachmentId = attachmentService.createAttachment(AttachmentCreator.builder()
                                                                   .path("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9.jpg@450w_600h.webp")
                                                                   .fileId(IdUtil.getSnowflakeNextId())
                                                                   .fileName("95d2881427fd43431f6a696a05623675ecdce9d9.jpg")
                                                                   .fileSize(100000000000L)
                                                                   .mimeType(MimeTypeUtils.APPLICATION_XML_VALUE)
                                                                   .build());

        AttachmentVO attachmentVO = attachmentService.findById(attachmentId);
        Assertions.assertNotNull(attachmentVO, "计划不能为空");
        Assertions.assertEquals("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9.jpg@450w_600h.webp", attachmentVO.getPath(), "保存路径不正确");

    }

    // TODO 附件可能不需要更新功能？
    @Test
    void updateAttachment() {
        Long attachmentId = attachmentService.createAttachment(AttachmentCreator.builder()
                                                                   .path("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9.jpg@450w_600h.webp")
                                                                   .fileId(IdUtil.getSnowflakeNextId())
                                                                   .fileName("95d2881427fd43431f6a696a05623675ecdce9d9.jpg")
                                                                   .fileSize(100000000000L)
                                                                   .mimeType(MimeTypeUtils.APPLICATION_XML_VALUE)
                                                                   .build());
    }

    @Test
    void findByPage() {
        for (int i = 0; i < 10; i++) {
            long snowflakeNextId = IdUtil.getSnowflakeNextId();
            attachmentService.createAttachment(AttachmentCreator.builder()
                                                   .path("https://i0.hdslb.com/bfs/bangumi/image/95d2881427fd43431f6a696a05623675ecdce9d9.jpg@450w_600h.webp")
                                                   .fileId(snowflakeNextId + i)
                                                   .fileName("95d2881427fd43431f6a696a05623675ecdce9d9.jpg")
                                                   .fileSize(100000000000L)
                                                   .mimeType(MimeTypeUtils.APPLICATION_XML_VALUE)
                                                   .build());
        }

        PageRequestWrapper<AttachmentQuery> requestWrapper = new PageRequestWrapper<>();
        requestWrapper.setPage(0);
        requestWrapper.setPageSize(8);
        Page<AttachmentVO> serviceByPage = attachmentService.findByPage(requestWrapper);
        Assertions.assertNotNull(serviceByPage, "查询结果不能为空");
        Assertions.assertEquals(8, serviceByPage.getContent().size(), "查询结果数量不正确");
    }
}
