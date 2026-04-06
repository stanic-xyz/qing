package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.CategoryTreeDTO;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.counterpayty.CounterpartyUpdateDto;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Result> getAllCategories() {
        return ResponseEntity.ok(Result.success(categoryService.getAllCategories()));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Result.success(null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Result.error(400, e.getMessage()));
        }
    }
}
