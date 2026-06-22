package cn.chenyunlong.qing.service.llm.service.imports.qianji;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.UploadFileRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QianjiFileServiceTest {

    @Mock
    private UploadFileRecordRepository uploadFileRecordRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private QianjiFileService qianjiFileService;

    /**
     * 初始化文件服务的测试配置，避免依赖外部配置文件。
     */
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(qianjiFileService, "maxFileSize", 20 * 1024 * 1024L);
        ReflectionTestUtils.setField(qianjiFileService, "storagePath", System.getProperty("java.io.tmpdir"));
    }

    /**
     * 验证上传时账户不存在会抛出标准资源不存在异常。
     */
    @Test
    @DisplayName("uploadFile 在账户不存在时抛出 NotFoundException")
    void shouldThrowNotFoundExceptionWhenUploadAccountDoesNotExist() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "qianji.csv",
                "text/csv",
                "id,time\n1,2024-01-01 10:00:00".getBytes()
        );
        when(uploadFileRecordRepository.findByFileHashAndAccountId(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(404L)))
                .thenReturn(Optional.empty());
        when(accountRepository.findById(404L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> qianjiFileService.uploadFile(file, 404L)
        );

        assertEquals("账户不存在: 404", exception.getMessage());
    }

    /**
     * 验证查询文件信息时不存在的记录会抛出标准资源不存在异常。
     */
    @Test
    @DisplayName("getFileInfo 在文件不存在时抛出 NotFoundException")
    void shouldThrowNotFoundExceptionWhenFileDoesNotExist() {
        when(uploadFileRecordRepository.findById(404L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> qianjiFileService.getFileInfo(404L)
        );

        assertEquals("文件不存在: 404", exception.getMessage());
    }

    /**
     * 验证空文件仍然保持参数错误语义。
     */
    @Test
    @DisplayName("uploadFile 对空文件保留 IllegalArgumentException")
    void shouldKeepIllegalArgumentExceptionForEmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "qianji.csv",
                "text/csv",
                new byte[0]
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> qianjiFileService.uploadFile(emptyFile, 1L)
        );

        assertEquals("文件不能为空", exception.getMessage());
    }
}
