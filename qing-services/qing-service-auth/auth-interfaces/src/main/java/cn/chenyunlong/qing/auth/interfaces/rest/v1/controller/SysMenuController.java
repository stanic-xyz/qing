package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.SysMenuService;
import cn.chenyunlong.qing.auth.domain.menu.dto.command.CreateSysMenuCommand;
import cn.chenyunlong.qing.auth.domain.menu.dto.command.UpdateSysMenuCommand;
import cn.chenyunlong.qing.auth.domain.menu.dto.request.SysMenuCreateRequest;
import cn.chenyunlong.qing.auth.domain.menu.dto.request.SysMenuUpdateRequest;
import cn.chenyunlong.qing.auth.domain.menu.dto.response.SysMenuResponse;
import cn.chenyunlong.qing.auth.domain.menu.dto.vo.SysMenuVO;
import cn.chenyunlong.qing.auth.infrastructure.converter.SysMenuMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.MenuEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.SysMenuJpaRepository;
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

    private final SysMenuService sysMenuService;
    private final SysMenuJpaRepository sysMenuJpaRepository;
    private final SysMenuMapper sysMenuMapper;

    private static List<SysMenuResponse> convertMenuToResponse(List<SysMenuVO> menuVOList, SysMenuMapper sysMenuMapper) {
        return menuVOList.stream().map(menuVO -> {
            SysMenuResponse sysMenuResponse = sysMenuMapper.vo2Response(menuVO);
            sysMenuResponse.setChildren(convertMenuToResponse(menuVO.getChildren(), sysMenuMapper));
            return sysMenuResponse;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public JsonResult<Long> createSysMenu(
            @RequestBody
            SysMenuCreateRequest request) {
        CreateSysMenuCommand creator = sysMenuMapper.request2Dto(request);
        return JsonResult.success(sysMenuService.createSysMenu(creator));
    }

    @PutMapping("/{id}")
    public JsonResult<String> updateSysMenu(
            @PathVariable("id") Long id,
            @RequestBody SysMenuUpdateRequest request) {
        request.setId(id);
        UpdateSysMenuCommand updater = sysMenuMapper.request2Updater(request);
        sysMenuService.updateSysMenu(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PatchMapping("/{id}/valid")
    public JsonResult<String> validSysMenu(
            @PathVariable("id")
            Long id) {
        sysMenuService.validSysMenu(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PatchMapping("/{id}/invalid")
    public JsonResult<String> invalidSysMenu(
            @PathVariable("id")
            Long id) {
        sysMenuService.invalidSysMenu(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @GetMapping("/{id}")
    public JsonResult<SysMenuResponse> findById(
            @PathVariable("id")
            Long id) {
        SysMenuVO vo = sysMenuService.findById(id);
        SysMenuResponse response = sysMenuMapper.vo2Response(vo);
        return JsonResult.success(response);
    }

    /**
     * 查询树状列表
     */
    @GetMapping("/tree")
    public JsonResult<List<SysMenuResponse>> tree() {
        List<MenuEntity> menuEntityList = sysMenuJpaRepository.findAll();

        List<SysMenuVO> sysMenuVOS = sysMenuMapper.entity2Vo(menuEntityList);

        List<SysMenuResponse> menuResponseList = convertMenuToResponse(sysMenuVOS, sysMenuMapper);
        return JsonResult.success(menuResponseList);
    }
}
