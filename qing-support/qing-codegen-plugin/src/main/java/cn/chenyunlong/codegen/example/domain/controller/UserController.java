package cn.chenyunlong.codegen.example.domain.controller;

import cn.chenyunlong.codegen.example.domain.dto.creator.UserCreator;
import cn.chenyunlong.codegen.example.domain.dto.query.UserQuery;
import cn.chenyunlong.codegen.example.domain.dto.request.UserCreateRequest;
import cn.chenyunlong.codegen.example.domain.dto.request.UserQueryRequest;
import cn.chenyunlong.codegen.example.domain.dto.request.UserUpdateRequest;
import cn.chenyunlong.codegen.example.domain.dto.response.UserResponse;
import cn.chenyunlong.codegen.example.domain.dto.updater.UserUpdater;
import cn.chenyunlong.codegen.example.domain.dto.vo.UserVO;
import cn.chenyunlong.codegen.example.domain.mapper.UserMapper;
import cn.chenyunlong.codegen.example.domain.service.IUserService;
import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.common.AggregateId;
import java.lang.Long;
import java.lang.String;
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
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    /**
     * createUser request
     */
    @PostMapping("create")
    public JsonResult<Long> createUser(@RequestBody UserCreateRequest request) {
        UserCreator creator = UserMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(userService.createUser(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateUser")
    public JsonResult<String> updateUser(@RequestBody UserUpdateRequest request) {
        UserUpdater updater = UserMapper.INSTANCE.request2Updater(request);
        userService.updateUser(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validUser(@PathVariable AggregateId id) {
        userService.validUser(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidUser(@PathVariable AggregateId id) {
        userService.invalidUser(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<UserResponse> findById(@PathVariable AggregateId id) {
        UserVO vo = userService.findById(id);
        UserResponse response = UserMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<UserResponse>> page(
            @RequestBody PageRequestWrapper<UserQueryRequest> request) {
        PageRequestWrapper<UserQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(UserMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<UserVO> page = userService.findByPage(wrapper);
        return JsonResult.success(
                PageResult.of(
                    page.getContent().stream()
                        .map(UserMapper.INSTANCE::vo2CustomResponse)
                        .collect(Collectors.toList()),
                    page.getTotalElements(),
                    page.getSize(),
                    page.getNumber())
            );
    }
}
