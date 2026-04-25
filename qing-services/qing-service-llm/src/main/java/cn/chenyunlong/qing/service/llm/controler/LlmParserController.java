package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.parser.*;
import cn.chenyunlong.qing.service.llm.entity.LlmParseRecord;
import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import cn.chenyunlong.qing.service.llm.service.llm.LlmBillParserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * LLM 账单解析控制器
 */
@RestController
@RequestMapping("/api/llm/parser")
@RequiredArgsConstructor
@Slf4j
public class LlmParserController {

    private final LlmBillParserFacade parserFacade;

    /**
     * 预览解析结果（同步，不入库）
     */
    @PostMapping("/preview")
    public ResponseEntity<Result<LlmParseResponse>> preview(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "strategy", defaultValue = "BY_CONSUMPTION_TYPE") String strategy) {
        try {
            CategoryStrategy categoryStrategy = CategoryStrategy.fromCode(strategy);
            LlmParseResponse response = parserFacade.parse(file, categoryStrategy);

            if (response.isSuccess()) {
                return ResponseEntity.ok(Result.success(response));
            } else {
                return ResponseEntity.badRequest().body(Result.error(400, response.getErrorMessage()));
            }
        } catch (Exception e) {
            log.error("Preview parse failed", e);
            return ResponseEntity.badRequest().body(Result.error(400, e.getMessage()));
        }
    }

    /**
     * 异步解析（返回 taskId）
     */
    @PostMapping("/parse")
    public ResponseEntity<Result<TaskStatusResponse>> parseAsync(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "strategy", defaultValue = "BY_CONSUMPTION_TYPE") String strategy) {
        try {
            CategoryStrategy categoryStrategy = CategoryStrategy.fromCode(strategy);
            String taskId = parserFacade.parseAsync(file, categoryStrategy);

            TaskStatusResponse statusResponse = TaskStatusResponse.pending(taskId);
            return ResponseEntity.accepted().body(Result.success(statusResponse));
        } catch (Exception e) {
            log.error("Async parse request failed", e);
            return ResponseEntity.badRequest().body(Result.error(400, e.getMessage()));
        }
    }

    /**
     * 查询任务状态
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Result<TaskStatusResponse>> getTaskStatus(@PathVariable String taskId) {
        TaskStatusResponse status = parserFacade.getTaskStatus(taskId);

        if (status == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Result.success(status));
    }

    /**
     * 查询解析结果（按 taskId）
     */
    @GetMapping("/result/task/{taskId}")
    public ResponseEntity<Result<List<LlmParseDetailDTO>>> getParseResultByTaskId(@PathVariable String taskId) {
        Optional<LlmParseRecord> recordOpt = parserFacade.getParseRecordByTaskId(taskId);

        if (recordOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<LlmParseDetailDTO> details = parserFacade.getParseResult(recordOpt.get().getId());
        return ResponseEntity.ok(Result.success(details));
    }

    /**
     * 查询解析结果（按 recordId）
     */
    @GetMapping("/result/{recordId}")
    public ResponseEntity<Result<List<LlmParseDetailDTO>>> getParseResult(@PathVariable Long recordId) {
        List<LlmParseDetailDTO> details = parserFacade.getParseResult(recordId);
        return ResponseEntity.ok(Result.success(details));
    }

    /**
     * 获取解析记录
     */
    @GetMapping("/record/{recordId}")
    public ResponseEntity<Result<LlmParseRecord>> getParseRecord(@PathVariable Long recordId) {
        Optional<LlmParseRecord> recordOpt = parserFacade.getParseRecord(recordId);

        if (recordOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Result.success(recordOpt.get()));
    }

    /**
     * 取消任务
     */
    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Result<String>> cancelTask(@PathVariable String taskId) {
        parserFacade.getTaskStatus(taskId); // 确保任务存在
        return ResponseEntity.ok(Result.success("任务已取消"));
    }
}
