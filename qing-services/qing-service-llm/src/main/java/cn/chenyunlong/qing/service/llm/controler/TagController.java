package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.tag.CreateTagRequest;
import cn.chenyunlong.qing.service.llm.dto.tag.UpdateTagRequest;
import cn.chenyunlong.qing.service.llm.entity.Tag;
import cn.chenyunlong.qing.service.llm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public Result<List<Tag>> listTags() {
        List<Tag> tags = tagService.listAll();
        return Result.success(tags);
    }

    @GetMapping("/{id}")
    public Result<Tag> getTag(@PathVariable Long id) {
        Tag tag = tagService.getById(id);
        return Result.success(tag);
    }

    @PostMapping
    public Result<Tag> create(@RequestBody CreateTagRequest request) {
        validateTagName(request);
        Tag tag = tagService.create(request.getName(), request.getColor());
        return Result.success(tag);
    }

    @PutMapping("/{id}")
    public Result<Tag> update(@PathVariable Long id, @RequestBody UpdateTagRequest request) {
        validateTagName(request);
        Tag tag = tagService.update(id, request.getName(), request.getColor());
        return Result.success(tag);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return Result.success(null);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Long>> getStatistics() {
        Map<String, Long> statistics = tagService.getTagStatistics();
        return Result.success(statistics);
    }

    @PostMapping("/transactions/{transactionId}")
    public Result<Void> addTagToTransaction(
            @PathVariable Long transactionId,
            @RequestParam Long tagId) {
        tagService.addTagToTransaction(transactionId, tagId);
        return Result.success(null);
    }

    @DeleteMapping("/transactions/{transactionId}/{tagId}")
    public Result<Void> removeTagFromTransaction(
            @PathVariable Long transactionId,
            @PathVariable Long tagId) {
        tagService.removeTagFromTransaction(transactionId, tagId);
        return Result.success(null);
    }

    @GetMapping("/transactions/{transactionId}")
    public Result<List<Tag>> getTagsByTransaction(@PathVariable Long transactionId) {
        List<Tag> tags = tagService.getTagsByTransactionId(transactionId);
        return Result.success(tags);
    }

    /**
     * 校验标签写接口的名称字段，避免 controller 直接返回错误结果。
     *
     * @param request 创建或更新请求
     */
    private void validateTagName(CreateTagRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("标签名称不能为空");
        }
    }

    /**
     * 校验标签写接口的名称字段，避免 controller 直接返回错误结果。
     *
     * @param request 创建或更新请求
     */
    private void validateTagName(UpdateTagRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("标签名称不能为空");
        }
    }
}
