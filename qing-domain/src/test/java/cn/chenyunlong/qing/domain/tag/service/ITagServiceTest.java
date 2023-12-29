package cn.chenyunlong.qing.domain.tag.service;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.anime.tag.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.tag.service.ITagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

class ITagServiceTest extends AbstractDomainTests {


    @Autowired
    private ITagService typeService;

    @Rollback
    @Test
    void createTag() {
        TagCreator typeCreator = new TagCreator();
        typeCreator.setName("搞笑");
        typeCreator.setInstruction("typeCreator");
        Long typeId = typeService.createTag(typeCreator);
        Assertions.assertNotNull(typeId);
    }
}

