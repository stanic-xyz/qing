package cn.chenyunlong.qing.domain.type.service;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.type.creator.TypeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ITypeServiceTest extends AbstractDomainTests {


    @Autowired
    private ITypeService typeService;

    @Test
    void createType() {
        TypeCreator typeCreator = new TypeCreator();
        typeCreator.setName("搞笑");
        typeCreator.setInstruction("typeCreator");
        Long typeId = typeService.createType(typeCreator);
        Assertions.assertNotNull(typeId);
    }
}
