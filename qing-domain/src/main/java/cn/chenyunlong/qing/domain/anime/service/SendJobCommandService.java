//package cn.chenyunlong.qing.domain.anime.service;
//
//import cn.chenyunlong.qing.domain.anime.AnimeInfo;
//import cn.chenyunlong.qing.domain.anime.context.CreateAnimeContext;
//import cn.chenyunlong.qing.domain.anime.repository.AnimeInfoRepository;
//import cn.chenyunlong.qing.domain.anime.request.AnimeInfoCreateRequest;
//import cn.chenyunlong.qing.infrastructure.loader.LazyLoadProxyFactory;
//import com.google.common.base.Preconditions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.support.TransactionTemplate;
//
//@Service
//public class SendJobCommandService extends AbstractCommandService {
//    @Autowired
//    private LazyLoadProxyFactory lazyLoadProxyFactory;
//    @Autowired
//    private ApplicationEventPublisher eventPublisher;
//    @Autowired
//    private AnimeInfoRepository jobRepository;
//    @Autowired
//    private TransactionTemplate transactionTemplate;
//    @Autowired
//    private ValidateService validateService;
//
//    //    @Transactional // 不可用取，原因为边界过大，浪费数据库连接资源
//    public AnimeInfo createSendMessageJob(AnimeInfoCreateRequest createRequest) {
//        // 1. 基本参数验证
//        validateParam(createRequest);
//
//        CreateAnimeContext context = new CreateAnimeContext(createRequest);
//        CreateAnimeContext contextProxy = lazyLoadProxyFactory.createProxyFor(context);
//
//        // 2. 业务验证
//        validateBiz(contextProxy);
//
//        // 3. 构建 CreateSendMessage 模型
//        // 1. 一般情况，【*】静态工厂 + 构造函数
//        //      1. 一般情况下 静态工厂 等同于 构造函数，对认知没有冲击
//        //          构造函数完成 内存申请 和 资源初始化，但是很难控制业务逻辑
//        //      2. 特殊情况下，静态工厂表现能力更强，泛化（聚合根中存在 type）
//        //          增加 createXXX，createYYY 表达能力更强
//        // 2. 特殊情况，工厂方法设计模式
//        SendJob sendMessageJob = SendJob.create(contextProxy);
//
//        // 4. 验证模型有效性
//        sendMessageJob.validate();
//
//        // 5. 发布内部领域事件
//        //      1. 谁创建的？
//        //          【本质】一个重要业务的操作结果
//        //          业务动作创建领域事件
//        //      2. 谁发布的？
//        //          流程来发布
//        //          持久化之后进行发布（已经成为一种事实）
//        //      3. 订阅关系如何？
//        //          1. 强关系，应用级别。Spring 启动后便存在，调整成本很高。@EventListener 来订阅消息
//        //          2. 弱关系，请求级别。根据请求信息（方法、参数）动态调整订阅关系。
//        //              【核心实现思路】下单后发送短信，订阅下单成功事件，触发短信发送。用户生单发短信，赠送不发短信
//        //              1. 【*】参数透传，command -> context -> event -> 【决策点】SendListener 做逻辑判断，决定发不发
//        //              2. 在 【决策点】ApplicationService 方法中，根据参数判断 是不是要增加 SendListener 这个监听器
//        //      4. 如何解决领域事件的传递（创建 -》发布）
//        //          1. 【*】聚合根暂存（不是实体），只有聚合根才能捕获全部变化
//        //          2. ThreadLocal
//
//
//        this.transactionTemplate.execute(transactionStatus -> {
//
//            // 5. 保存到数据库
//            jobRepository.sync(sendMessageJob);
//
//            // 6. 触发领域事件
//            // 1. 本地消息表方法，要求 Event 入库 和 业务操作在同一数据库事务，使用 @EventListener
//            // 2. 如果是直接发送方案，可以使用 @TransactionEventListener
//            //      1. 调用点，只做回调的注册，不执行任何业务方法
//            //      2. 事务成功提交后，执行被注册的方法
//            sendMessageJob.consumeAndClearEvent(domainEvent -> eventPublisher.publishEvent(domainEvent));
//            return null;
//        });
//
//        return sendMessageJob;
//
//    }
//
//    private void validateBiz(CreateSendJobContext context) {
//        this.validateService.validateBusiness(context);
//        // 1. 验证 code 和相关配置是否有效
//
//        // 2. 验证发送目前用户是否有效
//        // 3. 验证发送内容是否有效
//    }
//
//    private void validateParam(AnimeInfoCreateRequest cmd) {
//        Preconditions.checkArgument(cmd != null);
//        ValidateErrorHandler validateErrorHandler = null; ///;
//        cmd.validate(validateErrorHandler);
//    }
//
//    public void cancelSendMessageJob(Long jobId) {
//        this
//                .<AnimeInfo, Long, Long>updaterFor(jobRepository)
//                .loadBy(id -> jobRepository.findById(id))
//                .contextFactory(o -> o)
//                .update((sendMessageJob, aLong) -> sendMessageJob.cancel())
//                .exe(jobId);
//    }
//
//    public void createTask(Long jobId) {
//        this
//                .<AnimeInfo, Long, Long>updaterFor(jobRepository)
//                .loadBy(id -> jobRepository.findById(id))
//                .contextFactory(o -> o)
//                .update((sendMessageJob, aLong) -> sendMessageJob.createdTask())
//                .exe(jobId);
//    }
//
//    public void sendSuccess(Long jobId) {
//        this
//                .<AnimeInfo, Long, Long>updaterFor(jobRepository)
//                .loadBy(id -> jobRepository.findById(id))
//                .contextFactory(o -> o)
//                .update((sendMessageJob, aLong) -> sendMessageJob.success())
//                .exe(jobId);
//    }
//
//    public void sendFailure(Long jobId) {
//        this
//                .<AnimeInfo, Long, Long>updaterFor(jobRepository)
//                .loadBy(id -> jobRepository.findById(id))
//                .contextFactory(o -> o)
//                .update((sendMessageJob, aLong) -> sendMessageJob.failure())
//                .exe(jobId);
//    }
//}
