package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.entity.SystemConfig;
import cn.chenyunlong.qing.service.llm.repository.SystemConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemConfigServiceTest {

    @Mock
    private SystemConfigRepository systemConfigRepo;

    @InjectMocks
    private SystemConfigService systemConfigService;

    @Nested
    @DisplayName("读取配置")
    class ReadConfig {

        @Test
        @DisplayName("getValue 返回存在的配置值")
        void shouldReturnValueWhenKeyExists() {
            SystemConfig config = new SystemConfig();
            config.setConfigKey("dedup.timeToleranceMinutes");
            config.setConfigValue("10");
            when(systemConfigRepo.findByConfigKey("dedup.timeToleranceMinutes")).thenReturn(Optional.of(config));

            String value = systemConfigService.getValue("dedup.timeToleranceMinutes", "5");

            assertEquals("10", value);
        }

        @Test
        @DisplayName("getValue 返回默认值当键不存在")
        void shouldReturnDefaultWhenKeyMissing() {
            when(systemConfigRepo.findByConfigKey("missing.key")).thenReturn(Optional.empty());

            String value = systemConfigService.getValue("missing.key", "defaultVal");

            assertEquals("defaultVal", value);
        }

        @Test
        @DisplayName("getInt 解析整数值")
        void shouldParseIntValue() {
            SystemConfig config = new SystemConfig();
            config.setConfigKey("dedup.timeToleranceMinutes");
            config.setConfigValue("10");
            when(systemConfigRepo.findByConfigKey("dedup.timeToleranceMinutes")).thenReturn(Optional.of(config));

            int value = systemConfigService.getInt("dedup.timeToleranceMinutes", 5);

            assertEquals(10, value);
        }

        @Test
        @DisplayName("getInt 返回默认值当解析失败")
        void shouldReturnDefaultWhenParseFails() {
            SystemConfig config = new SystemConfig();
            config.setConfigKey("key");
            config.setConfigValue("not-a-number");
            when(systemConfigRepo.findByConfigKey("key")).thenReturn(Optional.of(config));

            int value = systemConfigService.getInt("key", 42);

            assertEquals(42, value);
        }

        @Test
        @DisplayName("getBoolean 解析布尔值")
        void shouldParseBooleanValue() {
            SystemConfig config = new SystemConfig();
            config.setConfigKey("dedup.matchMerchant");
            config.setConfigValue("false");
            when(systemConfigRepo.findByConfigKey("dedup.matchMerchant")).thenReturn(Optional.of(config));

            boolean value = systemConfigService.getBoolean("dedup.matchMerchant", true);

            assertFalse(value);
        }

        @Test
        @DisplayName("getBoolean 返回默认值当键不存在")
        void shouldReturnDefaultBooleanWhenMissing() {
            when(systemConfigRepo.findByConfigKey("missing")).thenReturn(Optional.empty());

            boolean value = systemConfigService.getBoolean("missing", true);

            assertTrue(value);
        }
    }

    @Nested
    @DisplayName("写入配置")
    class WriteConfig {

        @Test
        @DisplayName("setValue 创建新配置")
        void shouldCreateNewConfig() {
            when(systemConfigRepo.findByConfigKey("new.key")).thenReturn(Optional.empty());

            systemConfigService.setValue("new.key", "value1", "desc");

            verify(systemConfigRepo).save(argThat(c ->
                    c.getConfigKey().equals("new.key") && c.getConfigValue().equals("value1")));
        }

        @Test
        @DisplayName("setValue 更新已有配置")
        void shouldUpdateExistingConfig() {
            SystemConfig existing = new SystemConfig();
            existing.setConfigKey("key");
            existing.setConfigValue("old");
            when(systemConfigRepo.findByConfigKey("key")).thenReturn(Optional.of(existing));

            systemConfigService.setValue("key", "new", "desc");

            assertEquals("new", existing.getConfigValue());
            verify(systemConfigRepo).save(existing);
        }
    }

    @Nested
    @DisplayName("列表与删除")
    class ListDelete {

        @Test
        @DisplayName("findAll 返回所有配置")
        void shouldReturnAllConfigs() {
            when(systemConfigRepo.findAll()).thenReturn(List.of(new SystemConfig(), new SystemConfig()));

            List<SystemConfig> list = systemConfigService.findAll();

            assertEquals(2, list.size());
        }

        @Test
        @DisplayName("delete 删除存在的配置")
        void shouldDeleteExisting() {
            SystemConfig config = new SystemConfig();
            config.setId(1L);
            when(systemConfigRepo.findById(1L)).thenReturn(Optional.of(config));

            systemConfigService.delete(1L);

            verify(systemConfigRepo).delete(config);
        }

        @Test
        @DisplayName("delete 抛出 NotFound 当配置不存在")
        void shouldThrowWhenDeleteMissing() {
            when(systemConfigRepo.findById(99L)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> systemConfigService.delete(99L));
        }
    }
}
