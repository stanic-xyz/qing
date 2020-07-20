package com.stan.zhangli.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.stan.zhangli.channel.attribute.Attributes;
import com.stan.zhangli.channel.codec.PackageCodeC;
import com.stan.zhangli.entities.request.PacketReq;
import com.stan.zhangli.entities.response.PacketRes;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: 陈云龙
 * @date: 2020/7/7
 * @description
 */
@Component
public class PackageCodeCHandler extends MessageToMessageCodec<WebSocketFrame, PacketRes<?>> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PackageCodeCHandler.class);
    @Autowired
    private PackageCodeC packetCodec;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, PacketRes<?> packetRes, List<Object> list) throws Exception {

        log.info("channel：" + channelHandlerContext.channel().id() + " 发送消息：" + channelHandlerContext.channel().attr(Attributes.UserID).get() + "   Last：" + JSONObject.toJSONString(packetRes));


        list.add(new TextWebSocketFrame(JSONObject.toJSONString(packetRes, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero)));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {

        if (webSocketFrame instanceof TextWebSocketFrame) {

            String msg = ((TextWebSocketFrame) webSocketFrame).text();

            JSONObject msgJson = JSONObject.parseObject(msg);
            PacketReq<?> packetReq = packetCodec.decode(msgJson);

            log.info("channel：" + channelHandlerContext.channel().id() + " 收到消息：" + channelHandlerContext.channel().attr(Attributes.UserID).get() + "   Last：" + JSONObject.toJSONString(packetReq));

            if (packetReq != null) {
                list.add(packetReq);
            } else {
                channelHandlerContext.writeAndFlush(new TextWebSocketFrame("滚"));
                channelHandlerContext.channel().close();
            }


        }
    }
}
