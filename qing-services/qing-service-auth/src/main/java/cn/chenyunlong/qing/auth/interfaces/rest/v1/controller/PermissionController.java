package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.AuthApplicationService;
import cn.chenyunlong.qing.auth.domain.rbac.Operator;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionStatus;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionType;
import cn.chenyunlong.qing.auth.domain.rbac.permission.command.CreatePermissionCommand;
import cn.chenyunlong.qing.auth.domain.rbac.permission.command.DeletePermissionCommand;
import cn.chenyunlong.qing.auth.domain.rbac.permission.command.UpdatePermissionCommand;
import cn.chenyunlong.qing.auth.domain.rbac.permission.command.UpdatePermissionStatusCommand;
import cn.chenyunlong.qing.auth.domain.rbac.permission.exception.PermissionDuplicateException;
import cn.chenyunlong.qing.auth.domain.rbac.permission.exception.PermissionNotFoundException;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PermissionEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.PermissionJpaRepository;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.permission.BatchCreatePermissionRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.permission.CreatePermissionRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.permission.UpdatePermissionRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.permission.UpdatePermissionStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/v1/permissions")
@Tag(name = "权限管理")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PermissionController {

    private final AuthApplicationService authApplicationService;
    private final PermissionJpaRepository permissionJpaRepository;

    /**
     * 创建权限接口
     *
     * @param request 创建权限请求体
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "创建权限")
    @PreAuthorize("hasAuthority('permission:create')")
    public JsonResult<Void> createPermission(@RequestBody @Valid CreatePermissionRequest request) {
        CreatePermissionCommand command = CreatePermissionCommand.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .type(PermissionType.valueOf(request.getType()))
                .status(PermissionStatus.ENABLED)
                .resource(request.getResource())
                .action(request.getAction())
                .sortOrder(request.getSortOrder())
                .parentId(request.getParentId() != null ? new PermissionId(request.getParentId()) : null)
                .build();
        authApplicationService.createPermission(command);
        return JsonResult.success();
    }

    /**
     * 批量创建权限
     */
    @PostMapping("/batch")
    @Operation(summary = "批量创建权限")
    @PreAuthorize("hasAuthority('permission:create')")
    public ResponseEntity<JsonResult<Void>> batchCreate(@RequestBody @Valid BatchCreatePermissionRequest request) {
        List<CreatePermissionRequest> items = request.getItems();
        for (CreatePermissionRequest item : items) {
            authApplicationService.createPermission(CreatePermissionCommand.builder()
                    .code(item.getCode())
                    .name(item.getName())
                    .description(item.getDescription())
                    .type(PermissionType.valueOf(item.getType()))
                    .status(PermissionStatus.ENABLED)
                    .resource(item.getResource())
                    .action(item.getAction())
                    .sortOrder(item.getSortOrder())
                    .parentId(item.getParentId() != null ? new PermissionId(item.getParentId()) : null)
                    .build());
        }
        return ResponseEntity.ok(JsonResult.success());
    }

    /**
     * 修改权限
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改权限")
    @PreAuthorize("hasAuthority('permission:update')")
    public ResponseEntity<JsonResult<Void>> updatePermission(
            @PathVariable("id") Long permissionId,
            @RequestBody @Valid UpdatePermissionRequest request) {
        UpdatePermissionCommand command = UpdatePermissionCommand.builder()
                .id(PermissionId.of(permissionId))
                .name(request.getName())
                .description(request.getDescription())
                .resource(request.getResource())
                .action(request.getAction())
                .sortOrder(request.getSortOrder())
                .build();
        authApplicationService.updatePermission(command);
        return ResponseEntity.ok(JsonResult.success());
    }

    /**
     * 删除权限
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "禁用权限")
    @PreAuthorize("hasAuthority('permission:update')")
    public ResponseEntity<JsonResult<Void>> updatePermissionStatus(@PathVariable("id") Long permissionId, @RequestBody UpdatePermissionStatusRequest statusRequest) {
        UpdatePermissionStatusCommand command = UpdatePermissionStatusCommand.builder()
                .id(PermissionId.of(permissionId))
                .status(statusRequest.getStatus())
                .reason("system").operator(Operator.system())
                .build();
        authApplicationService.updateStatus(command);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限")
    @PreAuthorize("hasAuthority('permission:delete')")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable("id") Long permissionId) {
        authApplicationService.deletePermission(DeletePermissionCommand.builder().id(PermissionId.of(permissionId)).build());
        return ResponseEntity.noContent().build();
    }

    /**
     * 分页查询权限列表
     */
    @GetMapping
    @Operation(summary = "分页查询权限列表")
    @PreAuthorize("hasAuthority('permission:read')")
    public JsonResult<Page<PermissionEntity>> page(
            @Parameter(description = "页码", example = "0") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "每页条数", example = "20") @RequestParam(value = "size", defaultValue = "20") int size,
            @Parameter(description = "排序字段", example = "id,desc") @RequestParam(value = "sort", required = false) String sort) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Page<PermissionEntity> result = permissionJpaRepository.findAll(pageable);
        return JsonResult.success(result);
    }

    /**
     * 根据标识查询权限
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据标识查询权限")
    @PreAuthorize("hasAuthority('permission:read')")
    public ResponseEntity<JsonResult<PermissionEntity>> findById(@PathVariable("id") Long permissionId) {
        Optional<PermissionEntity> entity = permissionJpaRepository.findById(permissionId);
        return entity.map(e -> ResponseEntity.ok(JsonResult.success(e)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(JsonResult.fail("资源不存在")));
    }

    /**
     * 条件查询权限
     */
    @GetMapping("/search")
    @Operation(summary = "条件查询权限")
    @PreAuthorize("hasAuthority('permission:read')")
    public JsonResult<Page<PermissionEntity>> search(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "resource", required = false) String resource,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", required = false) String sort) {
        PermissionEntity probe = new PermissionEntity();
        probe.setCode(code);
        probe.setName(name);
        probe.setType(type);
        probe.setStatus(status);
        probe.setResource(resource);
        probe.setAction(action);
        probe.setParentId(parentId);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("version")
                .withMatcher("code", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<PermissionEntity> example = Example.of(probe, matcher);
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Page<PermissionEntity> result = permissionJpaRepository.findAll(example, pageable);
        return JsonResult.success(result);
    }

    /**
     * 解析排序参数
     */
    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.unsorted();
        }
        List<Sort.Order> orders = Stream.of(sort.split(","))
                .map(String::trim)
                .collect(Collectors.groupingBy(string -> string.contains(" ") ? string.split(" ")[1] : "desc",
                        Collectors.mapping(string -> string.contains(" ") ? string.split(" ")[0] : string, Collectors.toList())))
                .entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(string -> new Sort.Order(
                                "asc".equalsIgnoreCase(entry.getKey()) ? Sort.Direction.ASC : Sort.Direction.DESC, string)))
                .collect(Collectors.toList());
        return Sort.by(orders);
    }

    /**
     * 统一异常处理：校验错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResult<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getAllErrors().stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("参数错误");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(JsonResult.fail(msg));
    }

    /**
     * 统一异常处理：资源不存在
     */
    @ExceptionHandler(PermissionNotFoundException.class)
    public ResponseEntity<JsonResult<Void>> handleNotFound(PermissionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(JsonResult.fail(ex.getMessage()));
    }

    /**
     * 统一异常处理：权限冲突
     */
    @ExceptionHandler({PermissionDuplicateException.class, DataIntegrityViolationException.class})
    public ResponseEntity<JsonResult<Void>> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(JsonResult.fail(ex.getMessage()));
    }
}
