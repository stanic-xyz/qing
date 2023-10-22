package cn.chenyunlong.qing.domain.zan.filters;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.domain.user.User;
import cn.chenyunlong.qing.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.zan.LikeContext;
import cn.chenyunlong.qing.domain.zan.pipeline.AbstractEventFilter;
import cn.chenyunlong.qing.domain.zan.request.ZanCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserQueryFilter extends AbstractEventFilter<LikeContext> {

    private final UserRepository userService;

    @Override
    protected void handleEvent(LikeContext eventContext) {
        log.info("获取用户信息！");
        ZanCreateRequest createRequest = eventContext.getCreateRequest();
        Long userId = createRequest.getUserId();
        User user = userService.findById(userId)
            .orElseThrow(() -> new NotFoundException("未查询到用户：" + userId));
        // 将用户信息添加到上下文中
        eventContext.getLikeModel().setUser(user);
    }
}
