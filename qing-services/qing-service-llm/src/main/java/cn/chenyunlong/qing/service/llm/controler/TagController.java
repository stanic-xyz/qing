package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.tag.CreateTagRequest;
import cn.chenyunlong.qing.service.llm.dto.tag.UpdateTagRequest;
import cn.chenyunlong.qing.service.llm.entity.Tag;
import cn.chenyunlong.qing.service.llm.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/tags")
@Slf4j
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public Result<List<Tag>> listTags() {
        try {
            List<Tag> tags = tagService.listAll();
            return Result.success(tags);
        } catch (Exception e) {
            log.error("获取标签列表失败", e);
            return Result.error(500, "获取标签列表失败");
        }
    }

    @GetMapping("/{id}")
    public Result<Tag> getTag(@PathVariable Long id) {
        try {
            Tag tag = tagService.getById(id);
            return Result.success(tag);
        } catch (IllegalArgumentException e) {
            return Result.error(404, e.getMessage());
        } catch (Exception e) {
            log.error("获取标签失败", e);
            return Result.error(500, "获取标签失败");
        }
    }

    @PostMapping
    public Result<Tag> create(@RequestBody CreateTagRequest request) {
        try {
            if (request.getName() == null || request.getName().isBlank()) {
                return Result.error(400, "标签名称不能为空");
            }
            Tag tag = tagService.create(request.getName(), request.getColor());
            return Result.success(tag);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("创建标签失败", e);
            return Result.error(500, "创建标签失败");
        }
    }

    @PutMapping("/{id}")
    public Result<Tag> update(@PathVariable Long id, @RequestBody UpdateTagRequest request) {
        try {
            if (request.getName() == null || request.getName().isBlank()) {
                return Result.error(400, "标签名称不能为空");
            }
            Tag tag = tagService.update(id, request.getName(), request.getColor());
            return Result.success(tag);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("更新标签失败", e);
            return Result.error(500, "更新标签失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            tagService.delete(id);
            return Result.success(null);
        } catch (IllegalArgumentException e) {
            return Result.error(404, e.getMessage());
        } catch (Exception e) {
            log.error("删除标签失败", e);
            return Result.error(500, "删除标签失败");
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Long>> getStatistics() {
        try {
            Map<String, Long> statistics = tagService.getTagStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取标签统计失败", e);
            return Result.error(500, "获取标签统计失败");
        }
    }

    @PostMapping("/transactions/{transactionId}")
    public Result<Void> addTagToTransaction(
            @PathVariable Long transactionId,
            @RequestParam Long tagId) {
        try {
            tagService.addTagToTransaction(transactionId, tagId);
            return Result.success(null);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("添加标签到交易记录失败", e);
            return Result.error(500, "添加标签失败");
        }
    }

    @DeleteMapping("/transactions/{transactionId}/{tagId}")
    public Result<Void> removeTagFromTransaction(
            @PathVariable Long transactionId,
            @PathVariable Long tagId) {
        try {
            tagService.removeTagFromTransaction(transactionId, tagId);
            return Result.success(null);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("移除交易记录标签失败", e);
            return Result.error(500, "移除标签失败");
        }
    }

    @GetMapping("/transactions/{transactionId}")
    public Result<List<Tag>> getTagsByTransaction(@PathVariable Long transactionId) {
        try {
            List<Tag> tags = tagService.getTagsByTransactionId(transactionId);
            return Result.success(tags);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("获取交易标签失败", e);
            return Result.error(500, "获取交易标签失败");
        }
    }
}
