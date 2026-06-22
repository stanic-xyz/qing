package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.AccountImportDTO;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import cn.chenyunlong.qing.service.llm.enums.ProcessStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountImportServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ChannelRepository channelRepository;

    @InjectMocks
    private AccountImportService accountImportService;

    /**
     * 初始化测试依赖并为渠道查询提供默认桩行为。
     */
    @BeforeEach
    void setUp() {
        Channel mockChannel = new Channel();
        mockChannel.setCode("TEST_CHANNEL");
        lenient().when(channelRepository.findAllByIsDeletedFalseAndCodeIn(anyList())).thenReturn(java.util.Collections.singletonList(mockChannel));
    }

    /**
     * 验证模板下载会返回非空文件内容。
     */
    @Test
    @DisplayName("downloadTemplate 返回非空模板内容")
    void testDownloadTemplate() throws Exception {
        byte[] bytes = accountImportService.downloadTemplate();
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    /**
     * 验证执行导入时会跳过无效记录并只持久化有效账户。
     */
    @Test
    @DisplayName("executeImport 跳过无效记录")
    void testExecuteImport_SkipInvalid() {
        AccountImportDTO valid = new AccountImportDTO();
        valid.setAccountName("Test1");
        valid.setAccountType(AccountType.DEBIT);
        valid.setInitialBalance(BigDecimal.ZERO);
        valid.setChannelCode("TEST_CHANNEL");

        AccountImportDTO invalid = new AccountImportDTO();

        List<AccountImportDTO> list = Arrays.asList(valid, invalid);
        int count = accountImportService.executeImport(list, ImportModeEnum.SKIP);

        assertEquals(1, count);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    /**
     * 验证覆盖导入会更新已存在账户。
     */
    @Test
    @DisplayName("executeImport 覆盖导入已存在账户")
    void testExecuteImport_Overwrite() {
        AccountImportDTO dto = new AccountImportDTO();
        dto.setAccountName("ExistingAccount");
        dto.setExistingAccountId(1L);
        dto.setAccountType(AccountType.CREDIT);
        dto.setInitialBalance(new BigDecimal("100"));
        dto.setProcessStatus(ProcessStatusEnum.DUPLICATE_OVERWRITE);
        dto.setChannelCode("TEST_CHANNEL");

        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setAccountName("ExistingAccount");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        List<AccountImportDTO> list = Arrays.asList(dto);
        int count = accountImportService.executeImport(list, ImportModeEnum.OVERWRITE);

        assertEquals(1, count);
        verify(accountRepository, times(1)).save(argThat(acc ->
            acc.getId() != null && acc.getId().equals(1L) && AccountType.CREDIT.equals(acc.getAccountType())
        ));
    }

    /**
     * 验证覆盖导入引用的目标账户不存在时会抛出资源不存在异常。
     */
    @Test
    @DisplayName("executeImport 覆盖目标账户不存在时抛出 NotFoundException")
    void shouldThrowNotFoundExceptionWhenOverwriteTargetDoesNotExist() {
        AccountImportDTO dto = new AccountImportDTO();
        dto.setAccountName("GhostAccount");
        dto.setExistingAccountId(99L);
        dto.setAccountType(AccountType.CREDIT);
        dto.setProcessStatus(ProcessStatusEnum.DUPLICATE_OVERWRITE);
        dto.setChannelCode("TEST_CHANNEL");

        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> accountImportService.executeImport(List.of(dto), ImportModeEnum.OVERWRITE)
        );

        assertEquals("待覆盖账户不存在: 99", exception.getMessage());
    }

    /**
     * 验证文件格式不合法仍然保持参数异常语义。
     */
    @Test
    @DisplayName("preview 遇到不支持的文件格式时保留 IllegalArgumentException")
    void shouldKeepIllegalArgumentExceptionForUnsupportedPreviewFileType() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "accounts.txt",
                "text/plain",
                "name,type\nA,DEBIT".getBytes()
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountImportService.preview(file, ImportModeEnum.SKIP)
        );

        assertEquals("仅支持 Excel 或 CSV 文件", exception.getMessage());
    }
}
