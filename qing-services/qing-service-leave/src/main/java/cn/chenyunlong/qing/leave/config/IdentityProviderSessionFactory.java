package cn.chenyunlong.qing.leave.config;

import cn.chenyunlong.qing.leave.service.MyIdmIdentityService;
import org.camunda.bpm.engine.impl.interceptor.Session;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;

public class IdentityProviderSessionFactory implements SessionFactory {
    public IdentityProviderSessionFactory(MyIdmIdentityService identityProvider) {}

    @Override
    public Class<?> getSessionType() {
        return null;
    }

    @Override
    public Session openSession() {
        return null;
    }
}
