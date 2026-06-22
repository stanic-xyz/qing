package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.entity.Tag;
import cn.chenyunlong.qing.service.llm.repository.TagRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TransactionRecordRepository transactionRecordRepository;

    @InjectMocks
    private TagService tagService;

    /**
     * 验证创建重复标签时会按业务冲突处理。
     */
    @Test
    @DisplayName("创建重复标签时应抛出业务异常")
    void createShouldThrowBusinessExceptionWhenTagNameAlreadyExists() {
        Tag existing = new Tag();
        existing.setId(1L);
        existing.setName("餐饮");
        when(tagRepository.findByNameAndIsDeletedFalse("餐饮")).thenReturn(Optional.of(existing));

        BusinessException exception = assertThrows(BusinessException.class, () -> tagService.create("餐饮", "#fff"));

        assertTrue(exception.getMessage().contains("标签名称已存在"));
    }

    /**
     * 验证查询不存在标签时会按资源不存在处理。
     */
    @Test
    @DisplayName("查询不存在标签时应抛出资源不存在异常")
    void getByIdShouldThrowNotFoundWhenTagMissing() {
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> tagService.getById(99L));

        assertTrue(exception.getMessage().contains("标签不存在"));
    }

    /**
     * 验证为不存在交易打标签时会按资源不存在处理。
     */
    @Test
    @DisplayName("为不存在交易打标签时应抛出资源不存在异常")
    void addTagToTransactionShouldThrowNotFoundWhenTransactionMissing() {
        when(transactionRecordRepository.findById(88L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> tagService.addTagToTransaction(88L, 1L));

        assertTrue(exception.getMessage().contains("交易记录不存在"));
    }
}
