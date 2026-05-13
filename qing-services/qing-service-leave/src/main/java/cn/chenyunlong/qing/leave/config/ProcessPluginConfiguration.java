package cn.chenyunlong.qing.leave.config;

import cn.chenyunlong.qing.leave.service.MyIdmIdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.db.entitymanager.DbEntityManagerFactory;
import org.camunda.bpm.engine.impl.persistence.GenericManagerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessPluginConfiguration implements ProcessEnginePlugin {

    private final MyIdmIdentityService identityProvider;

    @Override
    public void preInit(ProcessEngineConfigurationImpl config) {
        log.info("注册插件！");
        // 注册自定义的身份认证会话工厂
        //        config.setIdentityProviderSessionFactory(
        //                new IdentityProviderSessionFactory(identityProvider)
        //        );
        //        // 禁用内置的身份认证表
        //        config.setDbIdentityUsed(false);
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl config) {}

    @Override
    public void postProcessEngineBuild(ProcessEngine engine) {}
}
