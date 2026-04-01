package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.AccountImportDTO;
import cn.chenyunlong.qing.service.llm.dto.AccountPreviewResult;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountImportServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountImportService accountImportService;

    @Test
    void testDownloadTemplate() throws Exception {
        byte[] bytes = accountImportService.downloadTemplate();
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    void testExecuteImport_SkipInvalid() {
        AccountImportDTO valid = new AccountImportDTO();
        valid.setProcessStatus("VALID");
        valid.setAccountName("Test1");
        valid.setAccountType("DEBIT");
        valid.setInitialBalance(BigDecimal.ZERO);

        AccountImportDTO invalid = new AccountImportDTO();
        invalid.setProcessStatus("INVALID");

        List<AccountImportDTO> list = Arrays.asList(valid, invalid);
        int count = accountImportService.executeImport(list, ImportModeEnum.SKIP);

        assertEquals(1, count);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testExecuteImport_Overwrite() {
        AccountImportDTO dto = new AccountImportDTO();
        dto.setProcessStatus("DUPLICATE_OVERWRITE");
        dto.setAccountName("ExistingAccount");
        dto.setExistingAccountId(1L);
        dto.setAccountType("CREDIT");
        dto.setInitialBalance(new BigDecimal("100"));

        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setAccountName("ExistingAccount");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        List<AccountImportDTO> list = Arrays.asList(dto);
        int count = accountImportService.executeImport(list, ImportModeEnum.OVERWRITE);

        assertEquals(1, count);
        verify(accountRepository, times(1)).save(argThat(acc -> 
            acc.getId() != null && acc.getId().equals(1L) && "CREDIT".equals(acc.getAccountType())
        ));
    }
}
