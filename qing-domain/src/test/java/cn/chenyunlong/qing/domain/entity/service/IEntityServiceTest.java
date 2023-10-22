package cn.chenyunlong.qing.domain.entity.service;

import cn.chenyunlong.qing.domain.AbstractDomainTests;
import cn.chenyunlong.qing.domain.entity.EntityType;
import cn.chenyunlong.qing.domain.entity.creator.EntityCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

@Rollback
class IEntityServiceTest extends AbstractDomainTests {

    @Autowired
    private IEntityService userService;

    @Test
    void createEntity() {
        EntityCreator userCreator = new EntityCreator();
        userCreator.setEntityType(EntityType.USER);
        userCreator.setName("华为");
        Long entityId = userService.createEntity(userCreator);
        Assertions.assertNotNull(entityId);
    }
}
