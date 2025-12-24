package cn.chenyunlong.qing.auth.application;

/**
 * 服务/领域级测试：
 * - 使用内存仓库（InMemory*Repository）
 * - 真实 JWT 签发与验证
 * - 覆盖 register/login/validate/logout
 */
//@SpringBootTest(classes = {AuthApplication.class, TestConfig.class})
//@ActiveProfiles("test")
//@Import({InMemoryUserRepository.class,
//        InMemoryTokenRepository.class,
//        InMemoryAuthenticationRepository.class,
//        InMemoryRoleRepository.class})
//class AuthApplicationServiceIntegrationTest {
//
//    @Autowired
//    private AuthApplicationService authApplicationService;
//    @Autowired
//    private UserDomainService userDomainService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private TokenRepository tokenRepository;
//
//    @Test
//    @DisplayName("应用服务与领域服务：注册 -> 激活 -> 登录 -> 验证 -> 登出")
//    void serviceDomainFlow() {
//        User user = userDomainService.register(UserRegistrationCommand.create("demo", "123456@adminA", "test@demo.com", "13800138000", "stanic"));
////        authApplicationService.activateUser("demo", user.getActivationCode());
////        AuthenticationByUsernamePasswordCommand command = AuthenticationByUsernamePasswordCommand.create("demo", "123456@adminA", "127.0.0.1", "JUnit");
//
//        //        AuthenticationResultDTO loginResult = authApplicationService.login(command);
//        //        assertTrue(loginResult.success());
//        //        assertNotNull(loginResult.tokenInfo());
//        //        String accessToken = loginResult.tokenInfo().getTokenValue();
//        //        assertNotNull(accessToken);
//
//        // TODO
//        //        authApplicationService.logout(accessToken);
//        //        Assertions.assertThrowsExactly(AuthenticationException.class, () -> authApplicationService.getUserInfoByToken(accessToken));
//    }
//}
