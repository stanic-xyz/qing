package com.stan.zhangli.handler;

import com.stan.zhangli.entities.request.PacketReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.stan.zhangli.handler.command.ReqCommand.SINGLECHATMSG_REQUEST_CODE;

/**
 * @author: 陈云龙
 * @date: 2020/7/7
 * @description 消息转发器
 */
@Component
public class IMHandler extends SimpleChannelInboundHandler<PacketReq<?>> {

    private Map<Integer, SimpleChannelInboundHandler> handlerMap = new LinkedHashMap<>();
    @Autowired
    private SingleChatMsgHandler singleChatMsgHandler;


    @PostConstruct
    public void putHandlerMap() {

        handlerMap.put(SINGLECHATMSG_REQUEST_CODE, singleChatMsgHandler);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PacketReq<?> packetReq) throws Exception {


        handlerMap.get(packetReq.getMessageType()).channelRead(channelHandlerContext, packetReq);

    }
}
