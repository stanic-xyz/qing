package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.QingUserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.QingUserQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.request.QingUserCreateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.QingUserQueryRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.QingUserUpdateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.response.QingUserResponse;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.QingUserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.QingUserVO;
import cn.chenyunlong.qing.domain.auth.user.mapper.QingUserMapper;
import cn.chenyunlong.qing.domain.auth.user.service.IQingUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户管理-Qing", description = "用户管理-Qing")
@RestController
@Slf4j
@RequestMapping("api/v1/qing-user")
@RequiredArgsConstructor
public class QingUserController {

    private final IQingUserService qingUserService;

    @PostMapping
    public JsonResult<Long> createQingUser(
        @RequestBody
        QingUserCreateRequest request) {
        QingUserCreator creator = QingUserMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(qingUserService.createQingUser(creator));
    }

    @PostMapping("updateQingUser")
    public JsonResult<String> updateQingUser(
        @RequestBody
        QingUserUpdateRequest request) {
        QingUserUpdater updater = QingUserMapper.INSTANCE.request2Updater(request);
        qingUserService.updateQingUser(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validQingUser(
        @PathVariable
        Long id) {
        qingUserService.validQingUser(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidQingUser(
        @PathVariable
        Long id) {
        qingUserService.invalidQingUser(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @GetMapping("findById/{id}")
    public JsonResult<QingUserResponse> findById(
        @PathVariable
        Long id) {
        QingUserVO vo = qingUserService.findById(id);
        QingUserResponse response = QingUserMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<Page<QingUserResponse>> page(
        @RequestBody
        PageRequestWrapper<QingUserQueryRequest> request) {
        PageRequestWrapper<QingUserQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(QingUserMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<QingUserVO> page = qingUserService.findByPage(wrapper);
        return JsonResult.success(page.map(QingUserMapper.INSTANCE::vo2CustomResponse));
    }
}
