package com.stan.zhangli.entities.response;

import com.alibaba.fastjson.annotation.JSONField;


public class PacketRes<T> {

    public PacketRes() {
    }

    public PacketRes(Integer pushType, Integer statusCode, Integer backCode, String message, T data) {
        super();
        this.pushType = pushType;
        this.statusCode = statusCode;
        this.backCode = backCode;
        this.message = message;
        this.data = data;
    }

    @JSONField(name = "PushType")
    private Integer pushType;

    @JSONField(name = "StatusCode")
    private Integer statusCode;

    @JSONField(name = "BackCode")
    private Integer backCode;

    @JSONField(name = "Message")
    private String message;

    @JSONField(name = "Data")
    private T data;

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getBackCode() {
        return backCode;
    }

    public void setBackCode(Integer backCode) {
        this.backCode = backCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
