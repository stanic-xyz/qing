package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.department.dto.creator.DepartmentCreator;
import cn.chenyunlong.qing.domain.auth.department.dto.query.DepartmentQuery;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentCreateRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentQueryRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentUpdateRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.response.DepartmentResponse;
import cn.chenyunlong.qing.domain.auth.department.dto.updater.DepartmentUpdater;
import cn.chenyunlong.qing.domain.auth.department.dto.vo.DepartmentVO;
import cn.chenyunlong.qing.domain.auth.department.mapper.DepartmentMapper;
import cn.chenyunlong.qing.domain.auth.department.service.IDepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "部门管理")
@RestController
@Slf4j
@RequestMapping("api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final IDepartmentService departmentService;

    @PostMapping
    public JsonResult<Long> createDepartment(
        @RequestBody
        DepartmentCreateRequest request) {
        DepartmentCreator creator = DepartmentMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(departmentService.createDepartment(creator));
    }

    @PostMapping("updateDepartment")
    public JsonResult<String> updateDepartment(
        @RequestBody
        DepartmentUpdateRequest request) {
        DepartmentUpdater updater = DepartmentMapper.INSTANCE.request2Updater(request);
        departmentService.updateDepartment(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validDepartment(
        @PathVariable("id")
        Long id) {
        departmentService.validDepartment(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidDepartment(
        @PathVariable("id")
        Long id) {
        departmentService.invalidDepartment(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @GetMapping("findById/{id}")
    public JsonResult<DepartmentResponse> findById(
        @PathVariable("id")
        Long id) {
        DepartmentVO vo = departmentService.findById(id);
        DepartmentResponse response = DepartmentMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<PageResult<DepartmentResponse>> page(
        @RequestBody
        PageRequestWrapper<DepartmentQueryRequest> request) {
        PageRequestWrapper<DepartmentQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(DepartmentMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<DepartmentVO> page = departmentService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(DepartmentMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
