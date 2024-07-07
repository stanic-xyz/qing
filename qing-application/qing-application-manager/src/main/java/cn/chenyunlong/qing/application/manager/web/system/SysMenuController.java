package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.menu.dto.creator.SysMenuCreator;
import cn.chenyunlong.qing.domain.auth.menu.dto.query.SysMenuQuery;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuCreateRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuQueryRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuUpdateRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.response.SysMenuResponse;
import cn.chenyunlong.qing.domain.auth.menu.dto.updater.SysMenuUpdater;
import cn.chenyunlong.qing.domain.auth.menu.dto.vo.SysMenuVO;
import cn.chenyunlong.qing.domain.auth.menu.mapper.SysMenuMapper;
import cn.chenyunlong.qing.domain.auth.menu.service.ISysMenuService;
import java.util.List;
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

@RestController
@Slf4j
@RequestMapping("api/v1/sys-menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createSysMenu(
        @RequestBody
        SysMenuCreateRequest request) {
        SysMenuCreator creator = SysMenuMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(sysMenuService.createSysMenu(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateSysMenu")
    public JsonResult<String> updateSysMenu(
        @RequestBody
        SysMenuUpdateRequest request) {
        SysMenuUpdater updater = SysMenuMapper.INSTANCE.request2Updater(request);
        sysMenuService.updateSysMenu(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validSysMenu(
        @PathVariable("id")
        Long id) {
        sysMenuService.validSysMenu(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidSysMenu(
        @PathVariable("id")
        Long id) {
        sysMenuService.invalidSysMenu(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<SysMenuResponse> findById(
        @PathVariable("id")
        Long id) {
        SysMenuVO vo = sysMenuService.findById(id);
        SysMenuResponse response = SysMenuMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<SysMenuResponse>> page(
        @RequestBody
        PageRequestWrapper<SysMenuQueryRequest> request) {
        PageRequestWrapper<SysMenuQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(SysMenuMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<SysMenuVO> page = sysMenuService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(SysMenuMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }

    /**
     * 查询树状列表
     */
    @PostMapping("tree")
    public JsonResult<List<SysMenuResponse>> tree() {
        List<SysMenuVO> page = sysMenuService.tree();
        return JsonResult.success(page.stream().map(SysMenuMapper.INSTANCE::vo2CustomResponse).collect(Collectors.toList()));
    }
}
