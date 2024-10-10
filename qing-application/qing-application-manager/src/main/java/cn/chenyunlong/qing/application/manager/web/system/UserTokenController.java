package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserTokenQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenCreateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenQueryRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenUpdateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.response.UserTokenResponse;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserTokenUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserTokenVO;
import cn.chenyunlong.qing.domain.auth.user.mapper.UserTokenMapper;
import cn.chenyunlong.qing.domain.auth.user.service.IUserTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户令牌管理")
@RestController
@Slf4j
@RequestMapping("api/v1/user-token")
@RequiredArgsConstructor
public class UserTokenController {

    private final IUserTokenService userTokenService;

    @PostMapping
    public JsonResult<Long> createUserToken(
        @RequestBody
        UserTokenCreateRequest request) {
        UserTokenCreator creator = UserTokenMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(userTokenService.createUserToken(creator));
    }

    @PostMapping("updateUserToken")
    public JsonResult<String> updateUserToken(
        @RequestBody
        UserTokenUpdateRequest request) {
        UserTokenUpdater updater = UserTokenMapper.INSTANCE.request2Updater(request);
        userTokenService.updateUserToken(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validUserToken(
        @PathVariable("id")
        Long id) {
        userTokenService.validUserToken(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidUserToken(
        @PathVariable("id")
        Long id) {
        userTokenService.invalidUserToken(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @GetMapping("findById/{id}")
    public JsonResult<UserTokenResponse> findById(
        @PathVariable("id")
        Long id) {
        UserTokenVO vo = userTokenService.findById(id);
        UserTokenResponse response = UserTokenMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<Page<UserTokenResponse>> page(
        @RequestBody
        PageRequestWrapper<UserTokenQueryRequest> request) {
        PageRequestWrapper<UserTokenQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(UserTokenMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<UserTokenVO> page = userTokenService.findByPage(wrapper);
        return JsonResult.success(page.map(UserTokenMapper.INSTANCE::vo2CustomResponse));
    }
}
