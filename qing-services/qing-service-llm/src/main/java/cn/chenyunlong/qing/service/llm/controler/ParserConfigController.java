package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.ParserItemDTO;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.dto.parser.ScriptTestRequest;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.service.ParserConfigService;
import cn.chenyunlong.qing.service.llm.service.parser.DynamicFileParser;
import cn.chenyunlong.qing.service.llm.service.script.ScriptExecutorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/parsers")
public class ParserConfigController {

    @Resource
    private ParserConfigService parserConfigService;

    @Resource
    private ScriptExecutorFactory scriptExecutorFactory;

    @GetMapping
    public Result<List<ParserItemDTO>> getAvailableParsers() {
        return Result.success(parserConfigService.getAllAvailableParsers());
    }

    @GetMapping("/configs")
    public Result<List<ParserConfig>> getAllConfigs() {
        return Result.success(parserConfigService.findAllConfigs());
    }

    @GetMapping("/configs/{id}")
    public Result<ParserConfig> getConfig(@PathVariable Long id) {
        return Result.success(parserConfigService.getConfig(id));
    }

    @PostMapping("/configs")
    public Result<ParserConfig> createConfig(@RequestBody ParserConfig config) {
        return Result.success(parserConfigService.saveConfig(config));
    }

    @PutMapping("/configs/{id}")
    public Result<ParserConfig> updateConfig(@PathVariable Long id, @RequestBody ParserConfig config) {
        config.setId(id);
        return Result.success(parserConfigService.saveConfig(config));
    }

    @DeleteMapping("/configs/{id}")
    public Result<Void> deleteConfig(@PathVariable Long id) {
        parserConfigService.deleteConfig(id);
        return Result.success(null);
    }

    @PostMapping("/script/test")
    public Result<Object> testScript(@RequestBody ScriptTestRequest request) {
        try {
            if (request == null || request.getScript() == null || request.getScript().trim().isEmpty()) {
                return Result.error(400, "脚本不能为空");
            }
            String language = request.getLanguage() == null ? "groovy" : request.getLanguage();
            Object result = scriptExecutorFactory.execute(language, request.getScript(), request.getContext());

            boolean allowMap = Boolean.TRUE.equals(request.getAllowMap());
            if (!allowMap && result instanceof java.util.Map<?, ?>) {
                return Result.error(400, "字段映射脚本不支持返回 Map，请改用后置脚本处理");
            }

            boolean expectNumber = request.getExpectNumber() == null || request.getExpectNumber();
            if (expectNumber && result != null && !(result instanceof Number)) {
                return Result.error(400, "脚本返回值必须为数值类型(Number)，实际为: " + result.getClass().getSimpleName());
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "脚本执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/test")
    public Result<ParseResult> testParser(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("config") String configJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ParserConfig config = mapper.readValue(configJson, ParserConfig.class);
            DynamicFileParser parser = new DynamicFileParser(config, scriptExecutorFactory, true);
            ParseResult result = parser.parse(file.getInputStream(), file.getOriginalFilename());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "测试解析失败: " + e.getMessage());
        }
    }
}
