package cn.chenyunlong.qing.leave.service;

import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.stereotype.Component;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;

@Component
public class MyIdmIdentityService implements ReadOnlyIdentityProvider {


    @Override
    public User findUserById(String s) {
        return null;
    }

    @Override
    public UserQuery createUserQuery() {
        return null;
    }

    @Override
    public UserQuery createUserQuery(CommandContext commandContext) {
        return null;
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        return null;
    }

    @Override
    public boolean checkPassword(String s, String s1) {
        return false;
    }

    @Override
    public Group findGroupById(String s) {
        return null;
    }

    @Override
    public GroupQuery createGroupQuery() {
        return null;
    }

    @Override
    public GroupQuery createGroupQuery(CommandContext commandContext) {
        return null;
    }

    @Override
    public Tenant findTenantById(String s) {
        return null;
    }

    @Override
    public TenantQuery createTenantQuery() {
        return null;
    }

    @Override
    public TenantQuery createTenantQuery(CommandContext commandContext) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {

    }
}
