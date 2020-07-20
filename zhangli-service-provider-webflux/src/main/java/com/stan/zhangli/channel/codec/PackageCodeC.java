package com.stan.zhangli.channel.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.stan.zhangli.entities.request.LoginInfoReqPacket;
import com.stan.zhangli.entities.request.PacketReq;

import java.util.HashMap;
import java.util.Map;

import static com.stan.zhangli.handler.command.ReqCommand.LOGIN_REQUEST_CODE;


/**
 * @author: 陈云龙
 * @date: 2020/7/7
 * @description
 */
public class PackageCodeC {

    private final Map<Integer, Class<?>> packetTypeMap;


    /**
     * 初始化消息类型
     */
    public PackageCodeC() {
        this.packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST_CODE, LoginInfoReqPacket.class);
    }

    /**
     * 解析Socket包
     *
     * @param msgJson 消息的json对象
     * @param <T>     消息实体
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> PacketReq<T> decode(JSONObject msgJson) {
        Integer MessageType = msgJson.getIntValue("MessageType");
        Class<?> requestType = packetTypeMap.get(MessageType);

        if (requestType != null) {
            return (PacketReq<T>) deserialize(requestType, msgJson);
        }

        return null;
    }

    /**
     * 反序列化
     *
     * @param clazz   需要序列化到的类型
     * @param msgJson 消息实体
     * @param <T>     消息类型
     * @return
     */
    public <T> PacketReq<T> deserialize(Class<T> clazz, JSONObject msgJson) {

        return JSON.parseObject(msgJson.toJSONString(), new TypeReference<PacketReq<T>>(clazz) {
        });

    }
}
