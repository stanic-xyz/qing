package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.AccountImportDTO;
import cn.chenyunlong.qing.service.llm.dto.AccountPreviewResult;
import cn.chenyunlong.qing.service.llm.dto.account.AccountDTO;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.ProcessStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import com.opencsv.CSVReader;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class AccountImportService {

    @Resource
    private AccountRepository accountRepository;

    private static final String[] HEADERS = {
            "账户名称(必填)", "账户类型(必填 DEBIT/CREDIT/WALLET)", "发卡机构", "渠道标识",
            "卡号", "期初余额", "状态(ACTIVE/CLOSED)", "备注"
    };
    @Autowired
    private ChannelRepository channelRepository;

    /**
     * 生成模板文件流
     */
    public byte[] downloadTemplate() throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("账户导入模板");
            Row headerRow = sheet.createRow(0);

            // 设置表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256);
            }

            // 示例数据
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("我的建设银行卡");
            exampleRow.createCell(1).setCellValue("DEBIT");
            exampleRow.createCell(2).setCellValue("建设银行");
            exampleRow.createCell(3).setCellValue("CCB");
            exampleRow.createCell(4).setCellValue("1234567890");
            exampleRow.createCell(5).setCellValue("100.50");
            exampleRow.createCell(6).setCellValue(AccountStatusEnum.ACTIVE.name());
            exampleRow.createCell(7).setCellValue("这是测试导入的数据");

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * 预览解析文件
     */
    public AccountPreviewResult preview(MultipartFile file, ImportModeEnum mode) throws Exception {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }

        List<AccountImportDTO> parsedList = parseFile(file);
        if (parsedList.size() > 100) {
            throw new IllegalArgumentException("单次导入最大支持100条记录");
        }

        AccountPreviewResult result = new AccountPreviewResult();
        result.setTotalCount(parsedList.size());

        List<Account> allExistingAccounts = accountRepository.findAll();

        int valid = 0, invalid = 0, overwrite = 0, skip = 0;
        Set<String> seenNamesInFile = new HashSet<>();

        for (AccountImportDTO dto : parsedList) {
            List<String> errors = new ArrayList<>();

            // 必填校验
            if (dto.getAccountName() == null || dto.getAccountName().trim().isEmpty()) {
                errors.add("账户名称不能为空");
            } else if (seenNamesInFile.contains(dto.getAccountName())) {
                errors.add("文件中存在重复账户名");
            }

            if (dto.getAccountName() != null) {
                seenNamesInFile.add(dto.getAccountName());
            }
            if (dto.getAccountType() == null) {
                errors.add("账户类型不能为空");
            } else if (!Arrays.asList(AccountType.DEBIT, AccountType.CREDIT, AccountType.WALLET).contains(dto.getAccountType())) {
                errors.add("账户类型无效");
            }

            // 状态校验
            if (dto.getStatus() != null) {
                if (!EnumSet.allOf(AccountStatusEnum.class).contains(dto.getStatus())) {
                    errors.add("状态无效");
                }
            } else {
                dto.setStatus(AccountStatusEnum.ACTIVE); // 默认状态
            }

            if (!errors.isEmpty()) {
                dto.setProcessStatus(ProcessStatusEnum.INVALID);
                dto.setErrorMsg(String.join("; ", errors));
                invalid++;
                continue;
            }

            AccountDTO account = new AccountDTO();
            account.setAccountName(dto.getAccountName());
            dto.setAccount(account);

            // 重复判断: 根据 accountName
            Optional<Account> existingOpt = allExistingAccounts.stream()
                    .filter(a -> a.getAccountName().equals(dto.getAccountName()))
                    .findFirst();

            if (existingOpt.isPresent()) {
                dto.setExistingAccountId(existingOpt.get().getId());
                if (mode == ImportModeEnum.OVERWRITE) {
                    dto.setProcessStatus(ProcessStatusEnum.DUPLICATE_OVERWRITE);
                    dto.setErrorMsg("重复数据，将覆盖");
                    overwrite++;
                } else {
                    dto.setProcessStatus(ProcessStatusEnum.DUPLICATE_SKIP);
                    dto.setErrorMsg("重复数据，将跳过");
                    skip++;
                }
            } else {
                dto.setProcessStatus(ProcessStatusEnum.VALID);
                dto.setErrorMsg("正常");
                valid++;
            }
        }

        result.setValidCount(valid);
        result.setInvalidCount(invalid);
        result.setDuplicateOverwriteCount(overwrite);
        result.setDuplicateSkipCount(skip);
        result.setItems(parsedList);

        return result;
    }

    /**
     * 实际导入执行
     */
    @Transactional
    public int executeImport(List<AccountImportDTO> items, ImportModeEnum mode) {
        int processedCount = 0;
        for (AccountImportDTO dto : items) {
            // 跳过无效或明确被标记跳过的数据
            if (ProcessStatusEnum.INVALID.equals(dto.getProcessStatus()) || ProcessStatusEnum.DUPLICATE_SKIP.equals(dto.getProcessStatus())) {
                continue;
            }

            Account account;
            if (ProcessStatusEnum.DUPLICATE_OVERWRITE.equals(dto.getProcessStatus()) && dto.getExistingAccountId() != null) {
                account = accountRepository.findById(dto.getExistingAccountId()).orElse(new Account());
            } else {
                account = new Account();
            }

            List<String> channelCodes = items.stream().map(AccountImportDTO::getChannelCode).toList();
            List<Channel> channels = channelRepository.findAllByIsDeletedFalseAndCodeIn(channelCodes);

            Channel channel = channels.stream().filter(c -> StringUtils.equals(c.getCode(), dto.getChannelCode())).findFirst().orElse(null);
            if (channel == null) {
                dto.setProcessStatus(ProcessStatusEnum.INVALID);
                dto.setErrorMsg("渠道不存在");
                continue;
            }
            account.setAccountName(dto.getAccountName());
            account.setAccountType(dto.getAccountType());
            account.setBankName(dto.getBankName());
            account.setChannel(channel);
            account.setCardNumber(dto.getCardNumber());
            account.setInitialBalance(dto.getInitialBalance() != null ? dto.getInitialBalance() : BigDecimal.ZERO);
            account.setStatus(dto.getStatus());
            account.setRemark(dto.getRemark());

            accountRepository.save(account);
            processedCount++;
        }

        log.info("Batch import accounts completed. Processed: {} records. Mode: {}", processedCount, mode);
        return processedCount;
    }

    private List<AccountImportDTO> parseFile(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("未知的文件格式");
        }

        if (filename.endsWith(".csv")) {
            return parseCsv(file);
        } else if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
            return parseExcel(file);
        } else {
            throw new IllegalArgumentException("仅支持 Excel 或 CSV 文件");
        }
    }

    private List<AccountImportDTO> parseExcel(MultipartFile file) throws Exception {
        List<AccountImportDTO> list = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                AccountImportDTO dto = new AccountImportDTO();
                dto.setAccountName(getCellValue(row.getCell(0)));

                if (dto.getAccountName() == null || dto.getAccountName().trim().isEmpty()) {
                    continue; // 忽略空行
                }

                dto.setAccountType(AccountType.valueOf(getCellValue(row.getCell(1))));
                dto.setBankName(getCellValue(row.getCell(2)));
                dto.setChannelCode(getCellValue(row.getCell(3)));
                dto.setCardNumber(getCellValue(row.getCell(4)));

                String balStr = getCellValue(row.getCell(5));
                try {
                    dto.setInitialBalance(balStr != null && !balStr.isEmpty() ? new BigDecimal(balStr) : BigDecimal.ZERO);
                } catch (Exception e) {
                    dto.setInitialBalance(null); // 依赖验证阶段报错
                }

                dto.setStatus(AccountStatusEnum.valueOf(getCellValue(row.getCell(6))));
                dto.setRemark(getCellValue(row.getCell(7)));

                list.add(dto);
            }
        }
        return list;
    }

    private List<AccountImportDTO> parseCsv(MultipartFile file) throws Exception {
        List<AccountImportDTO> list = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            List<String[]> lines = reader.readAll();
            for (int i = 1; i < lines.size(); i++) {
                String[] row = lines.get(i);
                if (row == null || row.length == 0 || (row.length > 0 && (row[0] == null || row[0].trim().isEmpty()))) {
                    continue;
                }
                AccountImportDTO dto = new AccountImportDTO();
                dto.setAccountName(getSafeCsvValue(row, 0));
                dto.setAccountType(AccountType.valueOf(getSafeCsvValue(row, 1)));
                dto.setBankName(getSafeCsvValue(row, 2));
                dto.setChannelCode(getSafeCsvValue(row, 3));
                dto.setCardNumber(getSafeCsvValue(row, 4));

                String balStr = getSafeCsvValue(row, 5);
                try {
                    dto.setInitialBalance(balStr != null && !balStr.isEmpty() ? new BigDecimal(balStr) : BigDecimal.ZERO);
                } catch (Exception e) {
                    dto.setInitialBalance(null);
                }

                dto.setStatus(AccountStatusEnum.valueOf(getSafeCsvValue(row, 6)));
                dto.setRemark(getSafeCsvValue(row, 7));
                list.add(dto);
            }
        }
        return list;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        try {
            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell).trim();
        } catch (Exception e) {
            return "";
        }
    }

    private String getSafeCsvValue(String[] row, int index) {
        if (index < row.length) {
            return row[index] != null ? row[index].trim() : null;
        }
        return null;
    }
}
