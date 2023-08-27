package cn.chenyunlong.qing.domain.tag.service;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.tag.creator.TagCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class ITagServiceTest extends AbstractDomainTests {


    @Autowired
    private ITagService typeService;

    @Test
    void createTag() {
        TagCreator typeCreator = new TagCreator();
        typeCreator.setName("搞笑");
        typeCreator.setInstruction("typeCreator");
        Long typeId = typeService.createTag(typeCreator);
        Assertions.assertNotNull(typeId);
    }
}

