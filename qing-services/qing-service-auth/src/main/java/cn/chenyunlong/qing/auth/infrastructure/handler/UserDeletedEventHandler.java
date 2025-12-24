package cn.chenyunlong.qing.auth.infrastructure.handler;

import cn.chenyunlong.qing.auth.domain.user.event.UserDeleted;
import cn.chenyunlong.qing.auth.domain.user.event.UserRegistered;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// 用户注销事件处理器
@Component
@RequiredArgsConstructor
public class UserDeletedEventHandler {

    private final UserRepository userRepository;

    @EventListener
    public void handleUserDeleted(UserDeleted event) {

    }

    @EventListener
    public void handleUserRegistered(UserRegistered event) {

    }
}
