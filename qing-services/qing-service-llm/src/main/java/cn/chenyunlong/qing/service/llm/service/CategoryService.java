package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.CategoryTreeDTO;
import cn.chenyunlong.qing.service.llm.dto.category.AccountImportDTO;
import cn.chenyunlong.qing.service.llm.dto.category.CategoryPreviewResult;
import cn.chenyunlong.qing.service.llm.dto.counterpayty.CounterpartyUpdateDto;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final TransactionRecordRepository transactionRecordRepository;

    @PostConstruct
    public void initDefaultCategories() {
        if (categoryRepository.count() == 0) {
            String[] defaultExpenses = {"餐饮", "交通", "购物", "娱乐", "居家", "人情"};
            for (String name : defaultExpenses) {
                Category c = new Category();
                c.setName(name);
                c.setLevel(0);
                c.setType("EXPENSE");
                categoryRepository.save(c);
            }

            String[] defaultIncomes = {"工资", "理财", "红包", "其他收入"};
            for (String name : defaultIncomes) {
                Category c = new Category();
                c.setName(name);
                c.setLevel(0);
                c.setType("INCOME");
                categoryRepository.save(c);
            }
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findByIsDeletedFalse();
    }

    public List<CategoryTreeDTO> getCategoryTree() {
        List<Category> all = categoryRepository.findByIsDeletedFalse();
        List<CategoryTreeDTO> dtos = all.stream().map(this::convertToDTO).toList();

        Map<Long, List<CategoryTreeDTO>> childrenMap = dtos.stream()
                .filter(d -> d.getParentId() != null)
                .collect(Collectors.groupingBy(CategoryTreeDTO::getParentId));

        List<CategoryTreeDTO> tree = new ArrayList<>();
        for (CategoryTreeDTO dto : dtos) {
            dto.setChildren(childrenMap.getOrDefault(dto.getId(), new ArrayList<>()));
            if (dto.getParentId() == null) {
                tree.add(dto);
            }
        }
        return tree;
    }

    public Category createCategory(Category category) {
        if (category.getParentId() != null) {
            Category parent = categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new NotFoundException("父分类不存在"));
            category.setLevel(parent.getLevel() + 1);
            category.setType(parent.getType());
        } else {
            category.setLevel(0);
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updateInfo) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("分类不存在"));
        category.setName(updateInfo.getName());
        category.setIcon(updateInfo.getIcon());

        return categoryRepository.save(category);
    }

    /**
     * 删除分类，并将可预期的删除限制统一转换为业务异常。
     *
     * @param id 分类 ID
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("分类不存在"));

        // 1. 检查是否有子分类
        if (categoryRepository.existsByParentIdAndIsDeletedFalse(id)) {
            throw new BusinessException("无法删除：该分类下存在子分类，请先删除子分类");
        }

        // 2. 检查是否有流水关联 (简单通过名称匹配)
        if (transactionRecordRepository.existsByCategory(category)) {
            throw new BusinessException("无法删除：已有流水记录使用了该分类，请先解绑");
        }

        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    private CategoryTreeDTO convertToDTO(Category category) {
        CategoryTreeDTO dto = new CategoryTreeDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setParentId(category.getParentId());
        dto.setLevel(category.getLevel());
        dto.setIcon(category.getIcon());
        dto.setType(category.getType());
        return dto;
    }

    private static final String[] HEADERS = {
            "类型（支出、收入）", "分类名称", "大类名称", "图标"
    };

    /**
     * 生成模板文件流
     */
    public byte[] downloadTemplate() throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("分类导入模板");
            Row headerRow = sheet.createRow(0);

            // 设置表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // 创建标头列表
            IntStream.range(0, HEADERS.length).forEach(i -> {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256);
            });

            // 示例数据
            Row exampleRow1 = sheet.createRow(1);
            exampleRow1.createCell(0).setCellValue("支出");
            exampleRow1.createCell(1).setCellValue("餐饮");
            exampleRow1.createCell(2).setCellValue("三餐");
            exampleRow1.createCell(3).setCellValue("deal");

            Row exampleRow2 = sheet.createRow(2);
            exampleRow2.createCell(0).setCellValue("支出");
            exampleRow2.createCell(1).setCellValue("餐饮");
            exampleRow2.createCell(2).setCellValue("三餐");
            exampleRow2.createCell(3).setCellValue("deal");

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * 预览导入数据
     */
    public CategoryPreviewResult previewImport(MultipartFile file, ImportModeEnum mode) throws Exception {
        List<AccountImportDTO> dtoList = parseExcel(file);
        CategoryPreviewResult result = new CategoryPreviewResult();
        result.setTotalCount(dtoList.size());

        List<CategoryPreviewResult.PreviewItem> previewItems = new ArrayList<>();
        int validCount = 0;

        for (AccountImportDTO dto : dtoList) {
            CategoryPreviewResult.PreviewItem item = new CategoryPreviewResult.PreviewItem();
            item.setRowNum(dto.getRowNum());
            item.setType(dto.getType());
            item.setCategoryName(dto.getCategoryName());
            item.setParentName(dto.getParentName());
            item.setIcon(dto.getIcon());

            // 校验数据合法性
            String validateError = validateDto(dto);
            if (validateError != null) {
                item.setValid(false);
                item.setErrorMsg(validateError);
                item.setSuggestedAction("ERROR");
                previewItems.add(item);
                continue;
            }

            // 检查冲突
            Long parentId = resolveParentId(dto);
            // 父级不存在校验已经在validate中完成，这里直接查询冲突
            boolean conflict;
            Long conflictId = null;
            if (dto.getParentName() == null || dto.getParentName().trim().isEmpty()) {
                // 顶级分类
                Optional<Category> existing = categoryRepository.findByTypeAndNameAndParentIdIsNullAndIsDeletedFalse(
                        dto.getType(), dto.getCategoryName());
                conflict = existing.isPresent();
                conflictId = existing.map(Category::getId).orElse(null);
            } else {
                // 子分类
                Optional<Category> existing = categoryRepository.findByTypeAndNameAndParentIdAndIsDeletedFalse(
                        dto.getType(), dto.getCategoryName(), parentId);
                conflict = existing.isPresent();
                conflictId = existing.map(Category::getId).orElse(null);
            }

            item.setConflict(conflict);
            item.setConflictId(conflictId);
            item.setValid(true);

            // 根据冲突和模式给出建议操作
            if (!conflict) {
                item.setSuggestedAction("CREATE");
                validCount++;
            } else {
                if (mode == ImportModeEnum.SKIP) {
                    item.setSuggestedAction("SKIP");
                } else {
                    item.setSuggestedAction("UPDATE");
                    validCount++;
                }
            }
            previewItems.add(item);
        }

        result.setValidCount(validCount);
        result.setPreviewItems(previewItems);
        return result;
    }

    /**
     * 执行导入
     */
    @Transactional
    public int executeImport(List<AccountImportDTO> items, ImportModeEnum mode) {
        int successCount = 0;
        for (AccountImportDTO dto : items) {
            try {
                // 重新校验（防止前端篡改）
                String validateError = validateDto(dto);
                if (validateError != null) {
                    continue;
                }

                Long parentId = resolveParentId(dto);
                boolean conflict;
                Category existing;

                Optional<Category> opt;
                if (dto.getParentName() == null || dto.getParentName().trim().isEmpty()) {
                    opt = categoryRepository.findByTypeAndNameAndParentIdIsNullAndIsDeletedFalse(
                            dto.getType(), dto.getCategoryName());
                } else {
                    opt = categoryRepository.findByTypeAndNameAndParentIdAndIsDeletedFalse(
                            dto.getType(), dto.getCategoryName(), parentId);
                }
                conflict = opt.isPresent();
                existing = opt.orElse(null);

                if (!conflict) {
                    // 新增
                    Category newCat = getCategory(dto, parentId);
                    categoryRepository.save(newCat);
                    successCount++;
                } else {
                    // 存在冲突，根据mode处理
                    if (mode == ImportModeEnum.OVERWRITE) {
                        existing.setName(dto.getCategoryName());
                        existing.setIcon(dto.getIcon());
                        // 若之前软删除则恢复
                        if (existing.getIsDeleted()) {
                            existing.setIsDeleted(false);
                        }
                        categoryRepository.save(existing);
                        successCount++;
                    }
                    // SKIP 模式不做任何操作
                }
            } catch (Exception e) {
                // 记录日志，跳过当前条目不计数
            }
        }
        return successCount;
    }

    private static @NonNull Category getCategory(AccountImportDTO dto, Long parentId) {
        Category newCat = new Category();
        newCat.setName(dto.getCategoryName());
        newCat.setType(dto.getType());
        newCat.setIcon(dto.getIcon());
        if (parentId == null) {
            newCat.setLevel(0);
            newCat.setParentId(null);
        } else {
            newCat.setLevel(1);
            newCat.setParentId(parentId);
        }
        newCat.setIsDeleted(false);
        return newCat;
    }

    // ========== 私有辅助方法 ==========

    private List<AccountImportDTO> parseExcel(MultipartFile file) throws Exception {
        List<AccountImportDTO> list = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String typeRaw = getCellString(row.getCell(0));
                String categoryName = getCellString(row.getCell(1));
                String parentName = getCellString(row.getCell(2));
                String icon = getCellString(row.getCell(3));

                AccountImportDTO dto = new AccountImportDTO();
                dto.setRowNum(i + 1);
                dto.setCategoryName(categoryName);
                dto.setParentName(parentName);
                dto.setIcon(icon);

                // 类型转换
                if ("支出".equals(typeRaw)) {
                    dto.setType("EXPENSE");
                } else if ("收入".equals(typeRaw)) {
                    dto.setType("INCOME");
                } else {
                    dto.setType("UNKNOWN");
                }
                list.add(dto);
            }
        }
        return list;
    }

    private String validateDto(AccountImportDTO dto) {
        if (!"INCOME".equals(dto.getType()) && !"EXPENSE".equals(dto.getType())) {
            return "类型必须是支出或收入";
        }
        if (dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) {
            return "分类名称不能为空";
        }

        // 父级存在性校验
        String parentName = dto.getParentName();
        if (parentName != null && !parentName.trim().isEmpty()) {
            Long parentId = resolveParentId(dto);
            if (parentId == null) {
                return "大类名称 [" + parentName + "] 不存在或已被删除";
            }
        }
        return null;
    }

    private Long resolveParentId(AccountImportDTO dto) {
        String parentName = dto.getParentName();
        if (parentName == null || parentName.trim().isEmpty()) {
            return null;
        }
        Optional<Category> parentOpt = categoryRepository.findByTypeAndNameAndParentIdIsNullAndIsDeletedFalse(
                dto.getType(), parentName.trim());
        return parentOpt.map(Category::getId).orElse(null);
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }
}
