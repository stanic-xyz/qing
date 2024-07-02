package cn.chenyunlong.qing.domain.auth.role.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.role.dto.creator.RoleCreator;
import cn.chenyunlong.qing.domain.auth.role.dto.query.RoleQuery;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleCreateRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleQueryRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleUpdateRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.response.RoleResponse;
import cn.chenyunlong.qing.domain.auth.role.dto.updater.RoleUpdater;
import cn.chenyunlong.qing.domain.auth.role.dto.vo.RoleVO;
import cn.chenyunlong.qing.domain.auth.role.mapper.RoleMapper;
import cn.chenyunlong.qing.domain.auth.role.service.IRoleService;
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

@Tag(name = "角色管理")
@RestController
@Slf4j
@RequestMapping("api/v1/role")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createRole(
        @RequestBody
        RoleCreateRequest request) {
        RoleCreator creator = RoleMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(roleService.createRole(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateRole")
    public JsonResult<String> updateRole(
        @RequestBody
        RoleUpdateRequest request) {
        RoleUpdater updater = RoleMapper.INSTANCE.request2Updater(request);
        roleService.updateRole(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validRole(
        @PathVariable
        Long id) {
        roleService.validRole(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidRole(
        @PathVariable
        Long id) {
        roleService.invalidRole(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<RoleResponse> findById(
        @PathVariable
        Long id) {
        RoleVO vo = roleService.findById(id);
        RoleResponse response = RoleMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<RoleResponse>> page(
        @RequestBody
        PageRequestWrapper<RoleQueryRequest> request) {
        PageRequestWrapper<RoleQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(RoleMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<RoleVO> page = roleService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(RoleMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
