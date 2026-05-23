package cn.chenyunlong.qing.auth.infrastructure.handler;

import cn.chenyunlong.qing.auth.domain.authentication.service.TokenDomainService;
import cn.chenyunlong.qing.auth.domain.user.event.UserRolesChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRolesChangedEventHandler {

    private final TokenDomainService tokenDomainService;

    @EventListener
    public void handleUserRolesChanged(UserRolesChangedEvent event) {
        log.info("处理用户角色变更事件: userId={}, roleIds={}, reason={}",
            event.getUserId().id(), event.getRoleIds(), event.getReason());

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    revokeUserTokens(event);
                }
            });
        } else {
            revokeUserTokens(event);
        }
    }

    private void revokeUserTokens(UserRolesChangedEvent event) {
        try {
            int revokedCount = tokenDomainService.revokeAllUserTokens(
                event.getUserId(),
                event.getReason()
            );
            log.info("用户 {} 的令牌已撤销, 共撤销 {} 个令牌", event.getUserId().id(), revokedCount);
        } catch (Exception e) {
            log.error("撤销用户令牌失败: userId={}", event.getUserId().id(), e);
        }
    }
}
