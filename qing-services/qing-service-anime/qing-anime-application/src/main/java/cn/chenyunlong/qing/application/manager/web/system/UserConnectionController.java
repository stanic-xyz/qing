package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.connection.dto.creator.UserConnectionCreator;
import cn.chenyunlong.qing.domain.auth.connection.dto.query.UserConnectionQuery;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionCreateRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionQueryRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionUpdateRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.response.UserConnectionResponse;
import cn.chenyunlong.qing.domain.auth.connection.dto.updater.UserConnectionUpdater;
import cn.chenyunlong.qing.domain.auth.connection.dto.vo.UserConnectionVO;
import cn.chenyunlong.qing.domain.auth.connection.mapper.UserConnectionMapper;
import cn.chenyunlong.qing.domain.auth.connection.service.IUserConnectionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户连接表")
@RestController
@Slf4j
@RequestMapping("api/v1/user-connection")
@RequiredArgsConstructor
public class UserConnectionController {

    private final IUserConnectionService userConnectionService;

    @PostMapping
    public JsonResult<Long> createUserConnection(
        @RequestBody
        UserConnectionCreateRequest request) {
        UserConnectionCreator creator = UserConnectionMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(userConnectionService.createUserConnection(creator));
    }

    @PostMapping("updateUserConnection")
    public JsonResult<String> updateUserConnection(
        @RequestBody
        UserConnectionUpdateRequest request) {
        UserConnectionUpdater updater = UserConnectionMapper.INSTANCE.request2Updater(request);
        userConnectionService.updateUserConnection(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validUserConnection(
        @PathVariable("id")
        Long id) {
        userConnectionService.validUserConnection(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidUserConnection(
        @PathVariable("id")
        Long id) {
        userConnectionService.invalidUserConnection(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @GetMapping("findById/{id}")
    public JsonResult<UserConnectionResponse> findById(
        @PathVariable("id")
        Long id) {
        UserConnectionVO vo = userConnectionService.findById(id);
        UserConnectionResponse response = UserConnectionMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }
}
