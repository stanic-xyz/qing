package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.application.manager.web.anime.query.AnimeQueryService;
import cn.chenyunlong.qing.domain.auth.menu.dto.creator.SysMenuCreator;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuCreateRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuUpdateRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.response.SysMenuResponse;
import cn.chenyunlong.qing.domain.auth.menu.dto.updater.SysMenuUpdater;
import cn.chenyunlong.qing.domain.auth.menu.dto.vo.SysMenuVO;
import cn.chenyunlong.qing.domain.auth.menu.mapper.SysMenuMapper;
import cn.chenyunlong.qing.domain.auth.menu.service.ISysMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "菜单管理")
@RestController
@Slf4j
@RequestMapping("api/v1/sys-menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService sysMenuService;
    private final AnimeQueryService animeQueryService;

    private static List<SysMenuResponse> convertMenuToResponse(List<SysMenuVO> menuVOList) {
        return menuVOList.stream().map(menuVO -> {
            SysMenuResponse sysMenuResponse = SysMenuMapper.INSTANCE.vo2Response(menuVO);
            sysMenuResponse.setChildren(convertMenuToResponse(menuVO.getChildren()));
            return sysMenuResponse;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public JsonResult<Long> createSysMenu(
        @RequestBody
        SysMenuCreateRequest request) {
        SysMenuCreator creator = SysMenuMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(sysMenuService.createSysMenu(creator));
    }

    @PostMapping("updateSysMenu")
    public JsonResult<String> updateSysMenu(
        @RequestBody
        SysMenuUpdateRequest request) {
        SysMenuUpdater updater = SysMenuMapper.INSTANCE.request2Updater(request);
        sysMenuService.updateSysMenu(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validSysMenu(
        @PathVariable("id")
        Long id) {
        sysMenuService.validSysMenu(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidSysMenu(
        @PathVariable("id")
        Long id) {
        sysMenuService.invalidSysMenu(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @GetMapping("findById/{id}")
    public JsonResult<SysMenuResponse> findById(
        @PathVariable("id")
        Long id) {
        SysMenuVO vo = sysMenuService.findById(id);
        SysMenuResponse response = SysMenuMapper.INSTANCE.vo2Response(vo);
        return JsonResult.success(response);
    }

    /**
     * 查询树状列表
     */
    @GetMapping("menuTree")
    public JsonResult<List<SysMenuResponse>> tree() {
        List<SysMenuVO> menuVOList = animeQueryService.treeMenu();
        List<SysMenuResponse> menuResponseList = convertMenuToResponse(menuVOList);
        return JsonResult.success(menuResponseList);
    }
}
