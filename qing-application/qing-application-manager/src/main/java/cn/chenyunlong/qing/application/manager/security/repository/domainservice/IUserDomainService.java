package cn.chenyunlong.qing.application.manager.security.repository.domainservice;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IUserDomainService {

    private final JPAQueryFactory queryFactory;
    // private final UserRepository userRepository;

    // /**
    //  * 基础的用户登录方法
    //  *
    //  * @param loginParam 登录参数
    //  * @return 清空令牌响应
    //  */
    // public QingTokenResponse login(LoginParam loginParam) {
    //
    //     QQingUser user = QQingUser.qingUser;
    //     QingUser qingUserName =
    //         queryFactory.selectFrom(user).where(user.username.eq(loginParam.getUsername()))
    //             .fetchOne();
    //     if (qingUserName == null) {
    //         throw new RuntimeException("用户不存在");
    //     }
    //     if (!qingUserName.getPassword().equals(loginParam.getPassword())) {
    //         throw new RuntimeException("密码错误");
    //     }
    //     if (qingUserName.getValidStatus() != ValidStatus.VALID) {
    //         throw new RuntimeException("用户已被禁用");
    //     }
    //     // 创建一个token 返回到前端
    //     QingTokenResponse tokenResponse = new QingTokenResponse();
    //     tokenResponse.setAccessToken(IdUtil.randomUUID());
    //     tokenResponse.setExpiresIn(60 * 60 * 24);
    //     tokenResponse.setTokenType("Bearer");
    //     return tokenResponse;
    // }
}
