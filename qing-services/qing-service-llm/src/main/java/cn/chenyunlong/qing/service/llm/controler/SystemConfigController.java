package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.SystemConfigUpdateDTO;
import cn.chenyunlong.qing.service.llm.entity.SystemConfig;
import cn.chenyunlong.qing.service.llm.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统通用配置管理接口。
 */
@RestController
@RequestMapping("/api/finance/system-configs")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @GetMapping
    public Result<List<SystemConfig>> getAll() {
        return Result.success(systemConfigService.findAll());
    }

    @GetMapping("/{key}")
    public Result<String> getByKey(@PathVariable String key) {
        String value = systemConfigService.getValue(key, null);
        return Result.success(value);
    }

    @PutMapping("/{key}")
    public Result<Void> setValue(@PathVariable String key,
                                 @RequestBody SystemConfigUpdateDTO body) {
        systemConfigService.setValue(key, body.getValue(), body.getDescription());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        systemConfigService.delete(id);
        return Result.success();
    }
}
