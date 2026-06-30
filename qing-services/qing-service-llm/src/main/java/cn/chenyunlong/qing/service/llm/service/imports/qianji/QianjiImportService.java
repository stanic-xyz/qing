package cn.chenyunlong.qing.service.llm.service.imports.qianji;

import cn.chenyunlong.qing.service.llm.dto.CategoryTreeDTO;
import cn.chenyunlong.qing.service.llm.dto.imports.*;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionType;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.service.CategoryService;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class QianjiImportService {

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final TransactionRecordRepository transactionRepository;
    private final CategoryService categoryService;
    private final QianjiFileService qianjiFileService;

    private static final DateTimeFormatter QIANJI_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String PLACEHOLDER_VALUE = "/";

    private static final int COL_ORIGINAL_ID = 0;
    private static final int COL_TIME = 1;
    private static final int COL_CATEGORY1 = 2;
    private static final int COL_CATEGORY2 = 3;
    private static final int COL_TYPE = 4;
    private static final int COL_AMOUNT = 5;
    private static final int COL_CURRENCY = 6;
    private static final int COL_ACCOUNT1 = 7;
    private static final int COL_ACCOUNT2 = 8;
    private static final int COL_REMARK = 9;
    private static final int COL_REIMBURSED = 10;
    private static final int COL_FEE = 11;
    private static final int COL_COUPON = 12;
    private static final int COL_BOOKKEEPER = 13;
    private static final int COL_BILL_MARK = 14;
    private static final int COL_TAGS = 15;
    private static final int COL_BILL_IMAGE = 16;
    private static final int COL_LINKED_BILL_ID = 17;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    /**
     * 预览钱迹账单：解析 CSV 并返回分类树、账户列表与样例交易。
     * @deprecated 请使用 {@link #previewByFileId(Long)} 替代
     */
    @Deprecated
    public QianjiPreviewResult preview(MultipartFile file) throws Exception {
        List<QianjiRawRecord> rawRecords = parseQianjiCsv(file);

        List<CategoryNode> categoryTree = buildCategoryTree(rawRecords);
        List<String> accounts = extractAccounts(rawRecords);
        List<PreviewTransaction> samples = rawRecords.stream()
                .limit(10)
                .map(this::convertToPreview)
                .collect(Collectors.toList());

        QianjiPreviewResult result = new QianjiPreviewResult();
        result.setTotalRecords(rawRecords.size());
        result.setCategories(categoryTree);
        result.setAccounts(accounts);
        result.setSampleTransactions(samples);
        return result;
    }

    /**
     * 执行钱迹账单导入：按策略创建/匹配账户与分类，并写入交易记录（可 dryRun）。
     */
    public ImportResult execute(MultipartFile file, QianjiImportRequest request) throws Exception {
        List<QianjiRawRecord> rawRecords = parseQianjiCsv(file);
        return executeRawRecords(rawRecords, file.getOriginalFilename(), request);
    }

    /**
     * 基于文件ID预览钱迹账单：解析已上传文件并返回分类树、账户列表与样例交易。
     */
    public QianjiPreviewResult previewByFileId(Long fileId) throws Exception {
        List<QianjiRawRecord> rawRecords = parseQianjiCsvByFileId(fileId);

        List<CategoryNode> categoryTree = buildCategoryTree(rawRecords);
        List<String> accounts = extractAccounts(rawRecords);
        List<PreviewTransaction> samples = rawRecords.stream()
                .limit(10)
                .map(this::convertToPreview)
                .collect(Collectors.toList());

        QianjiPreviewResult result = new QianjiPreviewResult();
        result.setTotalRecords(rawRecords.size());
        result.setCategories(categoryTree);
        result.setAccounts(accounts);
        result.setSampleTransactions(samples);
        return result;
    }

    /**
     * 基于文件ID执行钱迹账单导入：按策略创建/匹配账户与分类，并写入交易记录。
     */
    public ImportResult executeByFileId(Long fileId, QianjiImportRequest request) throws Exception {
        UploadFileRecord fileRecord = qianjiFileService.getFileInfo(fileId);
        List<QianjiRawRecord> rawRecords = parseQianjiCsvByFileId(fileId);
        return executeRawRecords(rawRecords, fileRecord.getFileName(), request);
    }

    private List<QianjiRawRecord> parseQianjiCsvByFileId(Long fileId) throws Exception {
        try (java.io.InputStream inputStream = qianjiFileService.getFileInputStream(fileId);
             CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).build()) {
            List<QianjiRawRecord> records = new ArrayList<>();
            int currentLine = 0;

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (++currentLine <= 1) {
                    continue;
                }
                try {
                    String originalId = getCol(line, COL_ORIGINAL_ID);
                    String timeStr = getCol(line, COL_TIME);
                    LocalDateTime time = LocalDateTime.parse(timeStr, QIANJI_TIME_FORMATTER);
                    String category1 = defaultCategory(getCol(line, COL_CATEGORY1));
                    String category2 = getCol(line, COL_CATEGORY2);
                    String typeText = getCol(line, COL_TYPE);
                    BigDecimal amount = parseBigDecimal(getCol(line, COL_AMOUNT));
                    String currency = getCol(line, COL_CURRENCY);
                    String account1 = getCol(line, COL_ACCOUNT1);
                    String account2 = getCol(line, COL_ACCOUNT2);
                    String remark = getCol(line, COL_REMARK);
                    String reimbursed = getCol(line, COL_REIMBURSED);
                    BigDecimal fee = parseBigDecimalNullable(getCol(line, COL_FEE));
                    BigDecimal coupon = parseBigDecimalNullable(getCol(line, COL_COUPON));
                    String bookkeeper = getCol(line, COL_BOOKKEEPER);
                    String billMark = getCol(line, COL_BILL_MARK);
                    String tags = getCol(line, COL_TAGS);
                    String billImage = getCol(line, COL_BILL_IMAGE);
                    String linkedBillId = getCol(line, COL_LINKED_BILL_ID);
                    records.add(new QianjiRawRecord(
                            originalId, time, category1, category2, typeText, amount,
                            currency, account1, account2, remark, reimbursed,
                            fee, coupon, bookkeeper, billMark, tags, billImage, linkedBillId));
                } catch (Exception e) {
                    log.warn("解析钱迹账单行失败: {}, 错误: {}", Arrays.toString(line), e.getMessage());
                }
            }
            return records;
        }
    }

    /**
     * 执行钱迹账单导入（records 模式）：由前端在预览后直接提交 records，避免服务端再次解析文件。
     */
    public ImportResult executeRecords(QianjiExecuteRecordsRequest body) {
        if (body == null || body.getRequest() == null || body.getRecords() == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        String sourceFile = StrUtil.blankToDefault(body.getSourceFile(), "qianji.json");
        List<QianjiRawRecord> rawRecords = body.getRecords().stream()
                .filter(Objects::nonNull)
                .map(this::toRaw)
                .toList();
        return executeRawRecords(rawRecords, sourceFile, body.getRequest());
    }

    /**
     * 执行导入的公共实现：可由“上传文件解析”或“前端回传 records”两种方式复用。
     */
    private ImportResult executeRawRecords(List<QianjiRawRecord> rawRecords, String sourceFile, QianjiImportRequest request) {
        ImportResult result = new ImportResult();
        result.setTotalRecords(rawRecords.size());

        AccountPrepareResult accountPrepareResult = prepareAccounts(rawRecords, request.getAccountMode());
        result.setImportedAccounts(accountPrepareResult.createdOrUpdatedCount());

        CategoryPrepareResult categoryPrepareResult = prepareCategories(rawRecords, request.getCategoryMode());
        result.setImportedCategories(categoryPrepareResult.createdOrUpdatedCount());

        if (request.isDryRun()) {
            result.setImportedTransactions(0);
            return result;
        }

        final int inClauseBatchSize = 1000;
        List<String> allOriginalIds = rawRecords.stream()
                .map(QianjiRawRecord::originalId)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .toList();

        Set<String> existingOrSeenOriginalIds = new HashSet<>(Math.max(16, allOriginalIds.size()));
        for (int i = 0; i < allOriginalIds.size(); i += inClauseBatchSize) {
            int end = Math.min(i + inClauseBatchSize, allOriginalIds.size());
            List<String> batch = allOriginalIds.subList(i, end);
            existingOrSeenOriginalIds.addAll(transactionRepository.findExistingOriginalIds(batch));
        }

        final int saveBatchSize = 500;
        int importedRecords = 0;
        List<TransactionRecord> toSave = new ArrayList<>(Math.min(saveBatchSize, rawRecords.size()));
        for (QianjiRawRecord raw : rawRecords) {
            String originalId = raw.originalId();
            if (StrUtil.isBlank(originalId)) {
                continue;
            }
            if (existingOrSeenOriginalIds.contains(originalId)) {
                continue;
            }
            TransactionRecord record = toTransactionRecord(raw, sourceFile, accountPrepareResult.accountByName(), categoryPrepareResult);
            if (record == null) {
                continue;
            }
            toSave.add(record);
            existingOrSeenOriginalIds.add(originalId);
            if (toSave.size() >= saveBatchSize) {
                transactionRepository.saveAll(toSave);
                transactionRepository.flush();
                importedRecords += toSave.size();
                toSave.clear();
            }
        }
        if (!toSave.isEmpty()) {
            transactionRepository.saveAll(toSave);
            transactionRepository.flush();
            importedRecords += toSave.size();
        }
        result.setImportedTransactions(importedRecords);

        return result;
    }

    /**
     * 解析钱迹 CSV 文件为内部原始记录列表。
     */
    private List<QianjiRawRecord> parseQianjiCsv(MultipartFile file) throws Exception {
        if (file.getSize() > 20 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小不能超过20MB");
        }
        List<QianjiRawRecord> records = new ArrayList<>();
        int currentLine = 0;

        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (++currentLine <= 1) {
                    continue;
                }
                try {
                    String originalId = getCol(line, COL_ORIGINAL_ID);
                    String timeStr = getCol(line, COL_TIME);
                    LocalDateTime time = LocalDateTime.parse(timeStr, QIANJI_TIME_FORMATTER);
                    String category1 = defaultCategory(getCol(line, COL_CATEGORY1));
                    String category2 = getCol(line, COL_CATEGORY2);
                    String typeText = getCol(line, COL_TYPE);
                    BigDecimal amount = parseBigDecimal(getCol(line, COL_AMOUNT));
                    String currency = getCol(line, COL_CURRENCY);
                    String account1 = getCol(line, COL_ACCOUNT1);
                    String account2 = getCol(line, COL_ACCOUNT2);
                    String remark = getCol(line, COL_REMARK);
                    String reimbursed = getCol(line, COL_REIMBURSED);
                    BigDecimal fee = parseBigDecimalNullable(getCol(line, COL_FEE));
                    BigDecimal coupon = parseBigDecimalNullable(getCol(line, COL_COUPON));
                    String bookkeeper = getCol(line, COL_BOOKKEEPER);
                    String billMark = getCol(line, COL_BILL_MARK);
                    String tags = getCol(line, COL_TAGS);
                    String billImage = getCol(line, COL_BILL_IMAGE);
                    String linkedBillId = getCol(line, COL_LINKED_BILL_ID);
                    QianjiRawRecord record = new QianjiRawRecord(
                            originalId,
                            time,
                            category1,
                            category2,
                            typeText,
                            amount,
                            currency,
                            account1,
                            account2,
                            remark,
                            reimbursed,
                            fee,
                            coupon,
                            bookkeeper,
                            billMark,
                            tags,
                            billImage,
                            linkedBillId
                    );
                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析钱迹账单行失败: {}, 错误: {}", Arrays.toString(line), e.getMessage());
                }
            }
        }
        return records;
    }

    /**
     * 构建分类树：按(收支类型 + 一级分类)聚合，并附带二级分类列表。
     */
    private List<CategoryNode> buildCategoryTree(List<QianjiRawRecord> records) {
        // 1. 获取现有分类树
        List<CategoryTreeDTO> existingTree = categoryService.getCategoryTree();

        // 2. 构建一级分类索引：key = type + ":" + name，value = CategoryTreeDTO
        Map<String, CategoryTreeDTO> firstLevelIndex = new HashMap<>();
        // 同时构建每个一级分类下子分类的索引：key = parentId，value = Map<childName, CategoryTreeDTO>
        Map<Long, Map<String, CategoryTreeDTO>> childrenIndex = new HashMap<>();

        buildIndex(existingTree, firstLevelIndex, childrenIndex);

        // 3. 收集需要处理的分类（去重）
        Map<CategoryKey, Set<String>> neededCategories = new LinkedHashMap<>();
        for (QianjiRawRecord record : records) {
            String type = mapTypeForCategoryNode(record);      // "EXPENSE"/"INCOME"/"TRANSFER"
            String top = defaultCategory(record.category1());  // 一级分类名
            CategoryKey key = new CategoryKey(type, top);
            neededCategories.computeIfAbsent(key, k -> new LinkedHashSet<>());
            String second = record.category2();
            if (StrUtil.isNotBlank(second)) {
                neededCategories.get(key).add(second);
            }
        }

        // 4. 构建 CategoryNode 列表
        List<CategoryNode> result = new ArrayList<>(neededCategories.size());
        for (Map.Entry<CategoryKey, Set<String>> entry : neededCategories.entrySet()) {
            CategoryKey key = entry.getKey();
            String type = key.type;
            String firstName = key.name;

            // 创建一级分类节点
            CategoryNode firstNode = new CategoryNode();
            firstNode.setType(type);
            firstNode.setName(firstName);

            // 查找匹配的现有分类
            String indexKey = type + ":" + firstName;
            CategoryTreeDTO matchedFirst = firstLevelIndex.get(indexKey);
            if (matchedFirst != null) {
                firstNode.setNeedToCreate(false);
                firstNode.setMatchedCategory(matchedFirst);
            } else {
                firstNode.setNeedToCreate(true);
                firstNode.setMatchedCategory(null);
            }

            // 处理二级分类
            List<CategoryNode> childNodes = new ArrayList<>();
            Set<String> secondNames = entry.getValue();
            for (String secondName : secondNames) {
                CategoryNode secondNode = new CategoryNode();
                secondNode.setType(type);  // 二级分类继承一级的类型
                secondNode.setName(secondName);

                // 查找二级分类是否存在于当前一级分类下
                CategoryTreeDTO matchedSecond = null;
                if (matchedFirst != null) {
                    Map<String, CategoryTreeDTO> subMap = childrenIndex.get(matchedFirst.getId());
                    if (subMap != null) {
                        matchedSecond = subMap.get(secondName);
                    }
                }
                if (matchedSecond != null) {
                    secondNode.setNeedToCreate(false);
                    secondNode.setMatchedCategory(matchedSecond);
                } else {
                    secondNode.setNeedToCreate(true);
                    secondNode.setMatchedCategory(null);
                }
                childNodes.add(secondNode);
            }
            firstNode.setChildren(childNodes);
            result.add(firstNode);
        }
        return result;
    }

    /**
     * 递归构建一级和二级分类索引
     *
     * @param tree          分类树
     * @param firstLevelIdx 一级分类索引 (type:name -> DTO)
     * @param childrenIdx   子分类索引 (parentId -> (childName -> DTO))
     */
    private void buildIndex(List<CategoryTreeDTO> tree,
                            Map<String, CategoryTreeDTO> firstLevelIdx,
                            Map<Long, Map<String, CategoryTreeDTO>> childrenIdx) {
        if (tree == null) return;
        for (CategoryTreeDTO node : tree) {
            // 假设一级分类 level == 1，或者根据 parentId == null/0 判断
            if (node.getParentId() == null || node.getParentId() == 0) {
                String key = node.getType() + ":" + node.getName();
                firstLevelIdx.put(key, node);
            }
            // 无论几级，都为其建立子索引（用于后续快速查找）
            Map<String, CategoryTreeDTO> subMap = childrenIdx.computeIfAbsent(node.getId(), k -> new HashMap<>());
            if (node.getChildren() != null) {
                for (CategoryTreeDTO child : node.getChildren()) {
                    subMap.put(child.getName(), child);
                }
            }
            // 递归处理子节点（虽然上面已处理了直接子节点，但为了多层树结构，仍递归构建深层索引）
            // 如果分类树只有两层，这一步可以省略；但为了通用，保留递归
            buildIndex(node.getChildren(), firstLevelIdx, childrenIdx);
        }
    }

    /**
     * 从原始记录提取账户列表（包含账户1与账户2）。
     */
    private List<String> extractAccounts(List<QianjiRawRecord> records) {
        Set<String> set = new LinkedHashSet<>();
        for (QianjiRawRecord r : records) {
            if (StrUtil.isNotBlank(r.account1())) {
                set.add(r.account1());
            }
            if (StrUtil.isNotBlank(r.account2()) && !PLACEHOLDER_VALUE.equals(r.account2())) {
                set.add(r.account2());
            }
        }
        return new ArrayList<>(set);
    }

    /**
     * 准备账户：按导入策略创建/复用账户，并返回名称->账户映射。
     */
    private AccountPrepareResult prepareAccounts(List<QianjiRawRecord> records, ImportModeEnum mode) {
        List<String> accounts = extractAccounts(records);
        if (accounts.isEmpty()) {
            return new AccountPrepareResult(new HashMap<>(), 0);
        }

        List<Account> existingAccounts = accountRepository.findByAccountNameIn(accounts);
        Map<String, Account> accountByName = new HashMap<>();
        existingAccounts.forEach(a -> accountByName.put(a.getAccountName(), a));

        int createdOrUpdated = 0;
        for (String accountName : accounts) {
            if (StrUtil.isBlank(accountName)) {
                continue;
            }
            Account exist = accountByName.get(accountName);
            if (exist == null) {
                Account account = new Account();
                account.setAccountName(accountName);
                account.setAccountType(guessAccountType(accountName));
                account.setStatus(AccountStatusEnum.ACTIVE);
                accountRepository.save(account);
                createdOrUpdated++;
                accountByName.put(accountName, account);
                continue;
            }
            if (mode == ImportModeEnum.OVERWRITE) {
                boolean changed = false;
                AccountType guessed = guessAccountType(accountName);
                if (exist.getAccountType() == null) {
                    exist.setAccountType(guessed);
                    changed = true;
                }
                if (exist.getStatus() == null) {
                    exist.setStatus(AccountStatusEnum.ACTIVE);
                    changed = true;
                }
                if (changed) {
                    accountRepository.save(exist);
                    createdOrUpdated++;
                }
            }
        }
        return new AccountPrepareResult(accountByName, createdOrUpdated);
    }

    /**
     * 准备分类：按导入策略创建/复用分类，并返回按 key 查询的能力。
     */
    private CategoryPrepareResult prepareCategories(List<QianjiRawRecord> records, ImportModeEnum mode) {
        Set<CategoryTopKey> topKeys = new LinkedHashSet<>();
        Set<CategorySubKey> subKeys = new LinkedHashSet<>();

        for (QianjiRawRecord r : records) {
            TransactionType txType = mapTransactionType(r.typeText());
            if (txType == null) {
                continue;
            }
            String topName = defaultCategory(r.category1());
            String typeStr = txType.name();
            topKeys.add(new CategoryTopKey(typeStr, topName));
            if (StrUtil.isNotBlank(r.category2())) {
                subKeys.add(new CategorySubKey(typeStr, topName, r.category2()));
            }
        }

        List<Category> allExistingCategories = categoryRepository.findAll();
        Map<CategoryFullKey, Category> existingByFullKey = new HashMap<>(allExistingCategories.size());
        for (Category c : allExistingCategories) {
            existingByFullKey.put(new CategoryFullKey(c.getType(), c.getParentId(), c.getName()), c);
        }

        Map<CategoryTopKey, Category> topCategoryByKey = new HashMap<>();
        int createdOrUpdated = 0;

        for (CategoryTopKey key : topKeys) {
            Category existing = existingByFullKey.get(new CategoryFullKey(key.type(), null, key.name()));
            if (existing == null) {
                Category created = new Category();
                created.setType(key.type());
                created.setName(key.name());
                created.setLevel(0);
                created.setParentId(null);
                created.setIsDeleted(false);
                categoryRepository.save(created);
                createdOrUpdated++;
                topCategoryByKey.put(key, created);
                continue;
            }
            if (mode == ImportModeEnum.OVERWRITE && Boolean.TRUE.equals(existing.getIsDeleted())) {
                existing.setIsDeleted(false);
                categoryRepository.save(existing);
                createdOrUpdated++;
            }
            topCategoryByKey.put(key, existing);
        }

        Map<CategoryFullKey, Category> categoryByFullKey = new HashMap<>();
        for (Map.Entry<CategoryTopKey, Category> entry : topCategoryByKey.entrySet()) {
            CategoryTopKey key = entry.getKey();
            Category category = entry.getValue();
            categoryByFullKey.put(new CategoryFullKey(key.type(), null, key.name()), category);
        }

        for (CategorySubKey key : subKeys) {
            Category parent = topCategoryByKey.get(new CategoryTopKey(key.type(), key.parentName()));
            if (parent == null) {
                continue;
            }
            Category existing = existingByFullKey.get(new CategoryFullKey(key.type(), parent.getId(), key.name()));
            if (existing == null) {
                Category created = new Category();
                created.setType(key.type());
                created.setName(key.name());
                created.setLevel(1);
                created.setParentId(parent.getId());
                created.setIsDeleted(false);
                categoryRepository.save(created);
                createdOrUpdated++;
                categoryByFullKey.put(new CategoryFullKey(key.type(), parent.getId(), key.name()), created);
                continue;
            }
            if (mode == ImportModeEnum.OVERWRITE && Boolean.TRUE.equals(existing.getIsDeleted())) {
                existing.setIsDeleted(false);
                categoryRepository.save(existing);
                createdOrUpdated++;
            }
            categoryByFullKey.put(new CategoryFullKey(key.type(), parent.getId(), key.name()), existing);
        }

        return new CategoryPrepareResult(topCategoryByKey, categoryByFullKey, createdOrUpdated);
    }

    /**
     * 将原始记录映射为可落库的交易记录；无法映射时返回 null。
     */
    private TransactionRecord toTransactionRecord(
            QianjiRawRecord raw,
            String sourceFile,
            Map<String, Account> accountByName,
            CategoryPrepareResult categoryPrepareResult) {
        if (raw == null) {
            return null;
        }
        if (StrUtil.isBlank(raw.account1())) {
            return null;
        }
        Account fromAccount = accountByName.get(raw.account1());
        if (fromAccount == null) {
            return null;
        }

        TransactionType txType = mapTransactionType(raw.typeText());
        if (txType == null) {
            return null;
        }
        String typeStr = txType.name();

        Category category = resolveCategory(categoryPrepareResult, txType, raw.category1(), raw.category2());
        if (category == null) {
            return null;
        }

        TransactionRecord record = new TransactionRecord();
        record.setOriginalId(raw.originalId());
        record.setTransactionTime(raw.time());
        record.setTransactionType(txType);
        record.setAmount(raw.amount() == null ? BigDecimal.ZERO : raw.amount().abs());

        record.setAccount(fromAccount);
        record.setAccountName(fromAccount.getAccountName());
        record.setAccountType(fromAccount.getAccountType());

        record.setCategory(category);

        String merchant = raw.remark();
        if (StrUtil.isBlank(merchant)) {
            if (StrUtil.isNotBlank(raw.account2()) && !PLACEHOLDER_VALUE.equals(raw.account2())) {
                merchant = raw.account2();
            } else {
                merchant = raw.category1();
            }
        }
        record.setMerchant(merchant);
        record.setDetail(raw.remark());

        if (raw.fee() != null) {
            record.setFee(raw.fee());
        }


        if (txType == TransactionType.TRANSFER && StrUtil.isNotBlank(raw.account2()) && !PLACEHOLDER_VALUE.equals(raw.account2())) {
            Account toAccount = accountByName.get(raw.account2());
            if (toAccount != null) {
                record.setTargetAccountId(toAccount.getId());
            } else {
                record.setCounterpartyStr(raw.account2());
            }
        } else if (StrUtil.isNotBlank(raw.account2()) && !PLACEHOLDER_VALUE.equals(raw.account2())) {
            record.setCounterpartyStr(raw.account2());
        }

        record.setStatus(TransactionStatusEnum.SUCCESS);
        record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
        record.setConfirmed(false);
        record.setRecordRole(RecordRoleEnum.PRIMARY);

        record.setTags(buildTagsJson(raw));

        if ("退款".equals(raw.typeText()) || "报销记录".equals(raw.typeText())) {
            record.setTransactionType(TransactionType.INCOME);
        }
        if ("还款".equals(raw.typeText())) {
            record.setTransactionType(TransactionType.TRANSFER);
        }

        return record;
    }

    /**
     * 预览 DTO 转换。
     */
    private PreviewTransaction convertToPreview(QianjiRawRecord record) {
        PreviewTransaction previewTransaction = new PreviewTransaction();
        previewTransaction.setTime(record.time());
        previewTransaction.setAmount(record.amount());
        previewTransaction.setFromAccount(record.account1());
        previewTransaction.setToAccount(record.account2());
        previewTransaction.setCategory(record.category1());
        previewTransaction.setSubCategory(record.category2());
        previewTransaction.setRemark(record.remark());
        previewTransaction.setType(record.typeText());
        return previewTransaction;
    }

    /**
     * 原始记录 -> DTO（用于 preview 全量回传）。
     */
    private QianjiRecordDTO toDto(QianjiRawRecord raw) {
        QianjiRecordDTO dto = new QianjiRecordDTO();
        dto.setOriginalId(raw.originalId());
        dto.setTime(raw.time());
        dto.setCategory1(raw.category1());
        dto.setCategory2(raw.category2());
        dto.setTypeText(raw.typeText());
        dto.setAmount(raw.amount());
        dto.setCurrency(raw.currency());
        dto.setAccount1(raw.account1());
        dto.setAccount2(raw.account2());
        dto.setRemark(raw.remark());
        dto.setReimbursed(raw.reimbursed());
        dto.setFee(raw.fee());
        dto.setCoupon(raw.coupon());
        dto.setBookkeeper(raw.bookkeeper());
        dto.setBillMark(raw.billMark());
        dto.setTags(raw.tags());
        dto.setBillImage(raw.billImage());
        dto.setLinkedBillId(raw.linkedBillId());
        return dto;
    }

    /**
     * DTO -> 原始记录（用于 executeRecords）。
     */
    private QianjiRawRecord toRaw(QianjiRecordDTO dto) {
        return new QianjiRawRecord(
                dto.getOriginalId(),
                dto.getTime(),
                defaultCategory(dto.getCategory1()),
                StrUtil.trimToEmpty(dto.getCategory2()),
                StrUtil.trimToEmpty(dto.getTypeText()),
                dto.getAmount() == null ? BigDecimal.ZERO : dto.getAmount(),
                StrUtil.trimToEmpty(dto.getCurrency()),
                StrUtil.trimToEmpty(dto.getAccount1()),
                StrUtil.trimToEmpty(dto.getAccount2()),
                StrUtil.trimToEmpty(dto.getRemark()),
                StrUtil.trimToEmpty(dto.getReimbursed()),
                dto.getFee(),
                dto.getCoupon(),
                StrUtil.trimToEmpty(dto.getBookkeeper()),
                StrUtil.trimToEmpty(dto.getBillMark()),
                StrUtil.trimToEmpty(dto.getTags()),
                StrUtil.trimToEmpty(dto.getBillImage()),
                StrUtil.trimToEmpty(dto.getLinkedBillId())
        );
    }

    private Category resolveCategory(CategoryPrepareResult result, TransactionType txType, String category1, String category2) {
        if (result == null) {
            return null;
        }
        String topName = defaultCategory(category1);
        Category parent = result.topCategoryByKey().get(new CategoryTopKey(txType.name(), topName));
        if (parent == null) {
            return null;
        }
        if (StrUtil.isBlank(category2)) {
            return parent;
        }
        Category child = result.categoryByFullKey().get(new CategoryFullKey(txType.name(), parent.getId(), category2));
        return child != null ? child : parent;
    }

    private AccountType guessAccountType(String accountName) {
        if (StrUtil.isBlank(accountName)) {
            return AccountType.WALLET;
        }
        if (accountName.contains("信用卡")) {
            return AccountType.CREDIT;
        }
        if (accountName.contains("储蓄卡") || accountName.contains("银行")) {
            return AccountType.DEBIT;
        }
        if (accountName.contains("零钱") || accountName.contains("微信") || accountName.contains("支付宝")) {
            return AccountType.WALLET;
        }
        return AccountType.WALLET;
    }

    private TransactionType mapTransactionType(String typeText) {
        if (StrUtil.isBlank(typeText)) {
            return null;
        }
        return switch (typeText) {
            case "支出", "报销" -> TransactionType.EXPENSE;
            case "收入", "退款", "报销记录" -> TransactionType.INCOME;
            case "转账", "还款" -> TransactionType.TRANSFER;
            default -> null;
        };
    }

    private String mapTypeForCategoryNode(QianjiRawRecord record) {
        TransactionType type = mapTransactionType(record.typeText());
        if (type == TransactionType.EXPENSE) {
            return "EXPENSE";
        }
        if (type == TransactionType.INCOME) {
            return "INCOME";
        }
        if (type == TransactionType.TRANSFER) {
            return "TRANSFER";
        }
        return "EXPENSE";
    }

    private static String getCol(String[] line, int idx) {
        if (line == null || idx < 0 || idx >= line.length) {
            return "";
        }
        return line[idx] == null ? "" : line[idx].trim();
    }

    private static BigDecimal parseBigDecimal(String raw) {
        if (StrUtil.isBlank(raw)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(raw.trim());
    }

    private static BigDecimal parseBigDecimalNullable(String raw) {
        if (StrUtil.isBlank(raw)) {
            return null;
        }
        try {
            return new BigDecimal(raw.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static String defaultCategory(String category) {
        if (StrUtil.isBlank(category)) {
            return "其它";
        }
        return category.trim();
    }

    private String buildTagsJson(QianjiRawRecord raw) {
        List<String> tags = new ArrayList<>();
        if (StrUtil.isNotBlank(raw.billMark())) {
            tags.add(raw.billMark());
        }
        if (StrUtil.isNotBlank(raw.tags())) {
            tags.add(raw.tags());
        }
        if (StrUtil.isNotBlank(raw.reimbursed())) {
            tags.add("已报销:" + raw.reimbursed());
        }
        if (raw.coupon() != null && raw.coupon().compareTo(BigDecimal.ZERO) != 0) {
            tags.add("优惠券:" + raw.coupon());
        }
        if (tags.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(tags);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private String buildOriginalDataJson(QianjiRawRecord raw) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("originalId", raw.originalId());
        map.put("time", raw.time() != null ? raw.time().toString() : null);
        map.put("category1", raw.category1());
        map.put("category2", raw.category2());
        map.put("type", raw.typeText());
        map.put("amount", raw.amount());
        map.put("currency", raw.currency());
        map.put("account1", raw.account1());
        map.put("account2", raw.account2());
        map.put("remark", raw.remark());
        map.put("reimbursed", raw.reimbursed());
        map.put("fee", raw.fee());
        map.put("coupon", raw.coupon());
        map.put("bookkeeper", raw.bookkeeper());
        map.put("billMark", raw.billMark());
        map.put("tags", raw.tags());
        map.put("billImage", raw.billImage());
        map.put("linkedBillId", raw.linkedBillId());
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private record QianjiRawRecord(
            String originalId,
            LocalDateTime time,
            String category1,
            String category2,
            String typeText,
            BigDecimal amount,
            String currency,
            String account1,
            String account2,
            String remark,
            String reimbursed,
            BigDecimal fee,
            BigDecimal coupon,
            String bookkeeper,
            String billMark,
            String tags,
            String billImage,
            String linkedBillId) {
    }

    private record CategoryTreeKey(String type, String name) {
    }

    /**
     * 分类的复合键（类型 + 名称）
     */
    @Data
    @AllArgsConstructor
    private static class CategoryKey {
        String type;
        String name;
    }

    private record CategoryTopKey(String type, String name) {
    }

    private record CategorySubKey(String type, String parentName, String name) {
    }

    private record CategoryFullKey(String type, Long parentId, String name) {
    }

    private record AccountPrepareResult(Map<String, Account> accountByName, int createdOrUpdatedCount) {
    }

    private record CategoryPrepareResult(
            Map<CategoryTopKey, Category> topCategoryByKey,
            Map<CategoryFullKey, Category> categoryByFullKey,
            int createdOrUpdatedCount) {
    }
}
