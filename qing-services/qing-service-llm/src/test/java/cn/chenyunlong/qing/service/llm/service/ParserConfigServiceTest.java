package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.enums.ConfigStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.repository.ParserConfigRepository;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParserConfigServiceTest {

    @Mock
    private ParserConfigRepository repository;

    @Mock
    private List<FileParser> parserList;

    @Mock
    private ChannelRepository channelRepository;

    @InjectMocks
    private ParserConfigService parserConfigService;

    /**
     * 验证查询不存在配置时会按资源不存在处理。
     */
    @Test
    @DisplayName("查询不存在配置时应抛出资源不存在异常")
    void getConfigShouldThrowNotFoundWhenConfigMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> parserConfigService.getConfig(1L));

        assertTrue(exception.getMessage().contains("解析器配置不存在"));
    }

    /**
     * 验证编辑内置配置时会按业务冲突处理。
     */
    @Test
    @DisplayName("编辑内置配置时应抛出业务异常")
    void updateConfigShouldThrowBusinessExceptionWhenConfigIsBuiltIn() {
        ParserConfig existing = new ParserConfig();
        existing.setId(2L);
        existing.setIsBuiltIn(true);
        existing.setStatus(ConfigStatusEnum.DRAFT);
        when(repository.findById(2L)).thenReturn(Optional.of(existing));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> parserConfigService.updateConfig(2L, new ParserConfig()));

        assertTrue(exception.getMessage().contains("内置解析器不支持编辑"));
    }

    /**
     * 验证删除不存在配置时会按资源不存在处理。
     */
    @Test
    @DisplayName("删除不存在配置时应抛出资源不存在异常")
    void deleteConfigShouldThrowNotFoundWhenConfigMissing() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> parserConfigService.deleteConfig(3L));

        assertTrue(exception.getMessage().contains("解析器配置不存在"));
    }

    /**
     * 验证发布非草稿配置时会按业务冲突处理。
     */
    @Test
    @DisplayName("发布非草稿配置时应抛出业务异常")
    void publishShouldThrowBusinessExceptionWhenStatusIsNotDraft() {
        ParserConfig existing = new ParserConfig();
        existing.setId(4L);
        existing.setStatus(ConfigStatusEnum.PUBLISHED);
        when(repository.findById(4L)).thenReturn(Optional.of(existing));

        BusinessException exception = assertThrows(BusinessException.class, () -> parserConfigService.publish(4L));

        assertTrue(exception.getMessage().contains("只有草稿状态的解析器才能发布"));
    }

    /**
     * 验证删除存在配置时会委托仓储执行删除。
     */
    @Test
    @DisplayName("删除存在配置时应调用仓储删除")
    void deleteConfigShouldDeleteWhenConfigExists() {
        ParserConfig existing = new ParserConfig();
        existing.setId(5L);
        when(repository.findById(5L)).thenReturn(Optional.of(existing));

        parserConfigService.deleteConfig(5L);

        verify(repository).delete(existing);
    }
}
