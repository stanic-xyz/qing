package cn.chenyunlong.qing.application.manager.web.zan;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.domain.zan.dto.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanUpdateRequest;
import cn.chenyunlong.qing.domain.zan.dto.updater.ZanUpdater;
import cn.chenyunlong.qing.domain.zan.mapper.ZanMapper;
import cn.chenyunlong.qing.domain.zan.service.IZanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "点赞管理")
@RestController
@Slf4j
@RequestMapping("api/v1/zan")
@RequiredArgsConstructor
public class ZanController {

    private final IZanService zanService;

    @PostMapping
    public JsonResult<Long> createZan(
        @RequestBody
        ZanCreateRequest request) {
        ZanCreator creator = ZanMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(zanService.createZan(creator));
    }

    @PostMapping("updateZan")
    public JsonResult<String> updateZan(
        @RequestBody
        ZanUpdateRequest request) {
        ZanUpdater updater = ZanMapper.INSTANCE.request2Updater(request);
        zanService.updateZan(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validZan(
        @PathVariable("id")
        Long id) {
        zanService.validZan(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidZan(
        @PathVariable("id")
        Long id) {
        zanService.invalidZan(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }
}
