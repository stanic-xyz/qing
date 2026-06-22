package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.AccountPreviewResult;
import cn.chenyunlong.qing.service.llm.dto.CategoryTreeDTO;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.category.AccountImportDTO;
import cn.chenyunlong.qing.service.llm.dto.category.CategoryPreviewResult;
import cn.chenyunlong.qing.service.llm.dto.category.CategoryStatisticsDTO;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryController {

    private final CategoryService categoryService;
    private final TransactionRecordRepository transactionRecordRepository;

    @GetMapping
    public ResponseEntity<Result> getAllCategories() {
        return ResponseEntity.ok(Result.success(categoryService.getAllCategories()));
    }

    @GetMapping("/{id}/statistics")
    public Result<CategoryStatisticsDTO> getCategoryStatistics(@PathVariable("id") Long id) {
        List<Category> categories = categoryService.getAllCategories();
        Category category = categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("分类不存在"));

        long transactionCount = transactionRecordRepository.countByCategory(category);

        CategoryStatisticsDTO stats = new CategoryStatisticsDTO();
        stats.setCategoryId(category.getId());
        stats.setCategoryName(category.getName());
        stats.setType(category.getType());
        stats.setTransactionCount(transactionCount);
        return Result.success(stats);
    }

    @GetMapping("/tree")
    public ResponseEntity<Result> getCategoryTree() {
        List<CategoryTreeDTO> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(Result.success(tree));
    }

    @PostMapping
    public ResponseEntity<Result> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(Result.success(categoryService.createCategory(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        return ResponseEntity.ok(Result.success(categoryService.updateCategory(id, category)));
    }

    /**
     * 删除指定分类，已知业务异常和未知异常均交由统一异常体系处理。
     *
     * @param id 分类 ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Result> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(Result.success(null));
    }

    @PostMapping("import")
    public ResponseEntity<Result> importTemplate(@PathVariable("id") Long id, @RequestBody Category category) {
        return ResponseEntity.ok(Result.success(categoryService.updateCategory(id, category)));
    }

    @GetMapping("import/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        byte[] data = categoryService.downloadTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "category_import_template.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }

    @PostMapping("/import/preview")
    public Result<CategoryPreviewResult> previewImport(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "mode", defaultValue = "SKIP") ImportModeEnum mode) throws Exception {
        CategoryPreviewResult result = categoryService.previewImport(file, mode);
        return Result.success(result);
    }

    @PostMapping("/import/execute")
    public Result<Integer> executeImport(
            @RequestBody List<AccountImportDTO> items,
            @RequestParam(value = "mode", defaultValue = "SKIP") ImportModeEnum mode) {
        int count = categoryService.executeImport(items, mode);
        return Result.success(count);
    }
}
