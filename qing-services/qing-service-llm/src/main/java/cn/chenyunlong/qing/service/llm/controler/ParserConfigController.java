package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.ParserItemDTO;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.service.ParserConfigService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/parsers")
public class ParserConfigController {

    @Resource
    private ParserConfigService parserConfigService;

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

    @PostMapping("/test")
    public Result<cn.chenyunlong.qing.service.llm.dto.parser.ParseResult> testParser(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("config") String configJson) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            ParserConfig config = mapper.readValue(configJson, ParserConfig.class);
            cn.chenyunlong.qing.service.llm.service.parser.DynamicFileParser parser = new cn.chenyunlong.qing.service.llm.service.parser.DynamicFileParser(config);
            cn.chenyunlong.qing.service.llm.dto.parser.ParseResult result = parser.parse(file.getInputStream(), file.getOriginalFilename());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "测试解析失败: " + e.getMessage());
        }
    }
}