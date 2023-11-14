package cn.chenyunlong.qing.domain.entity.mapper;

import cn.chenyunlong.qing.domain.entity.EntityType;
import cn.chenyunlong.qing.domain.entity.dto.creator.EntityCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author 陈云龙 on 2023/11/10
 */
class EntityMapperTest {

    @Test
    void dtoToEntity() {
        EntityCreator dto = new EntityCreator();
        dto.setName("name");
        dto.setZanCount(18L);
        dto.setEntityType(EntityType.USER);
        EntityMapper.INSTANCE.dtoToEntity(dto);
        Assertions.assertEquals("name", dto.getName());
        Assertions.assertEquals(18L, dto.getZanCount());
        Assertions.assertEquals(EntityType.USER, dto.getEntityType());
    }
}
