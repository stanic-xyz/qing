package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 分类策略枚举测试
 */
class CategoryStrategyTest {

    @Test
    void testByConsumptionType() {
        CategoryStrategy strategy = CategoryStrategy.BY_CONSUMPTION_TYPE;

        assertEquals("BY_CONSUMPTION_TYPE", strategy.getCode());
        assertEquals("按消费类型分类", strategy.getDescription());
    }

    @Test
    void testByPlatform() {
        CategoryStrategy strategy = CategoryStrategy.BY_PLATFORM;

        assertEquals("BY_PLATFORM", strategy.getCode());
        assertEquals("按平台来源分类", strategy.getDescription());
    }

    @Test
    void testHybrid() {
        CategoryStrategy strategy = CategoryStrategy.HYBRID;

        assertEquals("HYBRID", strategy.getCode());
        assertEquals("混合模式", strategy.getDescription());
    }

    @Test
    void testFromCode() {
        assertEquals(CategoryStrategy.BY_CONSUMPTION_TYPE, CategoryStrategy.fromCode("BY_CONSUMPTION_TYPE"));
        assertEquals(CategoryStrategy.BY_PLATFORM, CategoryStrategy.fromCode("BY_PLATFORM"));
        assertEquals(CategoryStrategy.HYBRID, CategoryStrategy.fromCode("HYBRID"));
    }

    @Test
    void testFromCodeUnknown() {
        assertEquals(CategoryStrategy.BY_CONSUMPTION_TYPE, CategoryStrategy.fromCode("UNKNOWN"));
        assertEquals(CategoryStrategy.BY_CONSUMPTION_TYPE, CategoryStrategy.fromCode(null));
    }

    @Test
    void testValues() {
        CategoryStrategy[] values = CategoryStrategy.values();

        assertEquals(3, values.length);
        assertEquals(CategoryStrategy.BY_CONSUMPTION_TYPE, values[0]);
        assertEquals(CategoryStrategy.BY_PLATFORM, values[1]);
        assertEquals(CategoryStrategy.HYBRID, values[2]);
    }

    @Test
    void testGetCode() {
        for (CategoryStrategy strategy : CategoryStrategy.values()) {
            assertNotNull(strategy.getCode());
            assertFalse(strategy.getCode().isEmpty());
        }
    }

    @Test
    void testGetDescription() {
        for (CategoryStrategy strategy : CategoryStrategy.values()) {
            assertNotNull(strategy.getDescription());
            assertFalse(strategy.getDescription().isEmpty());
        }
    }
}
