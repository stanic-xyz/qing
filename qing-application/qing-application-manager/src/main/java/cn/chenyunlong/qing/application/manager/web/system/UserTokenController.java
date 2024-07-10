package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户令牌管理")
@RestController
@Slf4j
@RequestMapping("api/v1/user-token")
@RequiredArgsConstructor
public class UserTokenController {

    private final IUserTokenService userTokenService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createUserToken(@RequestBody UserTokenCreateRequest request) {
        UserTokenCreator creator = UserTokenMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(userTokenService.createUserToken(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateUserToken")
    public JsonResult<String> updateUserToken(@RequestBody UserTokenUpdateRequest request) {
        UserTokenUpdater updater = UserTokenMapper.INSTANCE.request2Updater(request);
        userTokenService.updateUserToken(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validUserToken(@PathVariable Long id) {
        userTokenService.validUserToken(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidUserToken(@PathVariable Long id) {
        userTokenService.invalidUserToken(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<UserTokenResponse> findById(@PathVariable Long id) {
        UserTokenVO vo = userTokenService.findById(id);
        UserTokenResponse response = UserTokenMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<UserTokenResponse>> page(
        @RequestBody
        PageRequestWrapper<UserTokenQueryRequest> request) {
        PageRequestWrapper<UserTokenQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(UserTokenMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<UserTokenVO> page = userTokenService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(UserTokenMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
