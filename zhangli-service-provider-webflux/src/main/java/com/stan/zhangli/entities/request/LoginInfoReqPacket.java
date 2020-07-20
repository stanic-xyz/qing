package com.stan.zhangli.entities.request;

import com.alibaba.fastjson.annotation.JSONField;

public class LoginInfoReqPacket {

    @JSONField(name = "UserID")
    private String userID;

    @JSONField(name = "Authorization")
    private String authorization;

    public LoginInfoReqPacket() {
        super();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}