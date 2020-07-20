package com.stan.zhangli.entities.request;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author:郭境科
 * @createdate:2020年4月10日
 * @description:客户端请求格式类
 */
public class PacketReq<T> {

    @JSONField(name = "MessageType")
    private Integer messageType;


    @JSONField(name = "ClientType")
    private Integer clientType;

    @JSONField(name = "Data")
    private T data;

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
