package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.enums.FileUploadStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.ParserConfigRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftBatchRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UploadFileRecordRepository;
import cn.chenyunlong.qing.service.llm.service.lock.BatchLockService;
import cn.chenyunlong.qing.service.llm.service.lock.LockFactory;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import cn.chenyunlong.qing.service.llm.service.script.ScriptExecutorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadServiceTest {

    @Mock
    private ParserConfigRepository parserConfigRepository;

    @Mock
    private TransactionRecordRepository transactionRecordRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UploadFileRecordRepository uploadFileRecordRepository;

    @Mock
    private UnifiedDraftBatchRepository unifiedDraftBatchRepository;

    @Mock
    private UnifiedDraftRecordRepository unifiedDraftRecordRepository;

    @Mock
    private ScriptExecutorFactory scriptExecutorFactory;

    @Mock
    private ReconciliationService reconciliationService;

    @Mock
    private MatcherService matcherService;

    @Mock
    private TransactionMatcherRepository transactionMatcherRepository;

    @Mock
    private UnifiedDraftRecordRepository draftRecordRepository;

    @Mock
    private UnifiedDraftBatchRepository draftBatchRepository;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private BatchLockService batchLockService;

    @Mock
    private LockFactory lockFactory;

    @Mock
    private Executor matchThreadPool;

    @Mock
    private DraftCommitService draftCommitService;

    @Mock
    private BatchMatchService batchMatchService;

    private UploadService uploadService;

    @BeforeEach
    void setUp() {
        uploadService = new UploadService(
                List.of(),
                parserConfigRepository,
                transactionRecordRepository,
                accountRepository,
                uploadFileRecordRepository,
                unifiedDraftBatchRepository,
                unifiedDraftRecordRepository,
                scriptExecutorFactory,
                reconciliationService,
                matcherService,
                transactionMatcherRepository,
                draftRecordRepository,
                draftBatchRepository,
                transactionTemplate,
                batchLockService,
                lockFactory,
                matchThreadPool,
                draftCommitService,
                batchMatchService
        );
    }

    /**
     * 验证内置解析器在缺少文件后缀时会抛出参数错误。
     */
    @Test
    @DisplayName("parseAndPreview 在文件后缀为空时抛出 IllegalArgumentException")
    void parseAndPreviewShouldThrowIllegalArgumentExceptionWhenFileExtensionIsBlank() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "statement",
                "text/plain",
                "content".getBytes()
        );
        when(accountRepository.findById(1L)).thenReturn(Optional.of(buildAccount("ALIPAY")));
        when(parserConfigRepository.findById(10L)).thenReturn(Optional.of(buildBuiltInParserConfig(10L, "ALIPAY")));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> uploadService.parseAndPreview(file, 10L, 1L)
        );

        assertTrue(exception.getMessage().contains("文件后缀不能为空"));
    }

    /**
     * 验证缺少匹配的内置解析器时会抛出资源不存在异常。
     */
    @Test
    @DisplayName("parseAndPreview 在找不到匹配内置解析器时抛出 NotFoundException")
    void parseAndPreviewShouldThrowNotFoundExceptionWhenBuiltinParserIsMissing() {
        uploadService = new UploadService(
                List.of(stubParser("WECHAT", List.of("CSV"))),
                parserConfigRepository,
                transactionRecordRepository,
                accountRepository,
                uploadFileRecordRepository,
                unifiedDraftBatchRepository,
                unifiedDraftRecordRepository,
                scriptExecutorFactory,
                reconciliationService,
                matcherService,
                transactionMatcherRepository,
                draftRecordRepository,
                draftBatchRepository,
                transactionTemplate,
                batchLockService,
                lockFactory,
                matchThreadPool,
                draftCommitService,
                batchMatchService
        );
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "statement.csv",
                "text/csv",
                "content".getBytes()
        );
        when(accountRepository.findById(1L)).thenReturn(Optional.of(buildAccount("ALIPAY")));
        when(parserConfigRepository.findById(10L)).thenReturn(Optional.of(buildBuiltInParserConfig(10L, "ALIPAY")));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> uploadService.parseAndPreview(file, 10L, 1L)
        );

        assertTrue(exception.getMessage().contains("找不到匹配的内置解析器"));
    }

    /**
     * 验证上传记录状态不允许匹配时会抛出业务异常。
     */
    @Test
    @DisplayName("startMatchingAsync 在状态不可匹配时抛出 BusinessException")
    void startMatchingAsyncShouldThrowBusinessExceptionWhenStatusIsInvalid() {
        UploadFileRecord record = new UploadFileRecord();
        record.setId(99L);
        record.setStatus(FileUploadStatusEnum.MATCHED);
        when(uploadFileRecordRepository.findById(99L)).thenReturn(Optional.of(record));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> uploadService.startMatchingAsync(99L, List.of())
        );

        assertTrue(exception.getMessage().contains("状态错误，当前状态不可匹配"));
    }

    /**
     * 构造一个满足内置解析器查找条件的解析器配置。
     */
    private ParserConfig buildBuiltInParserConfig(Long parserId, String channelCode) {
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.setId(parserId);
        parserConfig.setIsBuiltIn(true);
        parserConfig.setChannel(buildChannel(channelCode));
        parserConfig.setFileType("CSV");
        return parserConfig;
    }

    /**
     * 构造测试账户实体。
     */
    private Account buildAccount(String channelCode) {
        Account account = new Account();
        account.setId(1L);
        account.setChannel(buildChannel(channelCode));
        return account;
    }

    /**
     * 构造测试渠道实体。
     */
    private Channel buildChannel(String channelCode) {
        Channel channel = new Channel();
        channel.setCode(channelCode);
        channel.setName(channelCode);
        return channel;
    }

    /**
     * 创建仅用于匹配元数据的文件解析器桩实现。
     */
    private FileParser stubParser(String channelCode, List<String> supportedExtensions) {
        return new FileParser() {
            @Override
            public String channelCode() {
                return channelCode;
            }

            @Override
            public List<String> supportedFileExtensions() {
                return supportedExtensions;
            }

            @Override
            public ParseResult parse(InputStream inputStream, String originalFilename) {
                throw new UnsupportedOperationException("测试无需执行真实解析");
            }
        };
    }
}
