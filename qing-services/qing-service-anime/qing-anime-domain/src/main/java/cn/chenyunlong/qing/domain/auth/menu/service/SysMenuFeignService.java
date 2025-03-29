package cn.chenyunlong.qing.domain.auth.menu.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuCreateRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuQueryRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuUpdateRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.response.SysMenuResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic-feign-client",
    contextId = "sysMenuClient",
    path = "sysMenu/v1"
)
public interface SysMenuFeignService {

    /**
     * 创建
     */
    @PostMapping("createSysMenu")
    JsonResult<Long> createSysMenu(
        @RequestBody
        SysMenuCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateSysMenu")
    JsonResult<String> updateSysMenu(
        @RequestBody
        SysMenuUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validSysMenu(
        @PathVariable("id")
        Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidSysMenu(
        @PathVariable("id")
        Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<SysMenuResponse> findById(
        @PathVariable("id")
        Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<SysMenuResponse>> page(
        @RequestBody
        PageRequestWrapper<SysMenuQueryRequest> request);
}
