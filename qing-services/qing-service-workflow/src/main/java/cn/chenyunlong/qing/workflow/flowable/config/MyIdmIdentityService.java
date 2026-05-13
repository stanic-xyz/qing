package cn.chenyunlong.qing.workflow.flowable.config;

import org.flowable.engine.IdentityService;
import org.flowable.idm.api.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyIdmIdentityService implements IdentityService {
    @Override
    public User newUser(String userId) {
        return null;
    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public void updateUserPassword(User user) {

    }

    @Override
    public UserQuery createUserQuery() {
        return null;
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public Group newGroup(String groupId) {
        return null;
    }

    @Override
    public GroupQuery createGroupQuery() {
        return null;
    }

    @Override
    public NativeGroupQuery createNativeGroupQuery() {
        return null;
    }

    @Override
    public List<Group> getPotentialStarterGroups(String processDefinitionId) {
        return List.of();
    }

    @Override
    public List<User> getPotentialStarterUsers(String processDefinitionId) {
        return List.of();
    }

    @Override
    public void saveGroup(Group group) {

    }

    @Override
    public void deleteGroup(String groupId) {

    }

    @Override
    public void createMembership(String userId, String groupId) {

    }

    @Override
    public void deleteMembership(String userId, String groupId) {

    }

    @Override
    public boolean checkPassword(String userId, String password) {
        return false;
    }

    @Override
    public void setAuthenticatedUserId(String authenticatedUserId) {

    }

    @Override
    public void setUserPicture(String userId, Picture picture) {

    }

    @Override
    public Picture getUserPicture(String userId) {
        return null;
    }

    @Override
    public void setUserInfo(String userId, String key, String value) {

    }

    @Override
    public String getUserInfo(String userId, String key) {
        return "";
    }

    @Override
    public List<String> getUserInfoKeys(String userId) {
        return List.of();
    }

    @Override
    public void deleteUserInfo(String userId, String key) {

    }
}
