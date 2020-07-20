package com.stan.zhangli.handler;

import com.stan.zhangli.entities.request.PacketReq;
import com.stan.zhangli.entities.request.SingleChatMsgReqMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author: 陈云龙
 * @date: 2020/7/7
 * @description 单例消息处理器
 */
@Component
public class SingleChatMsgHandler extends SimpleChannelInboundHandler<PacketReq<SingleChatMsgReqMessage>> {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SingleChatMsgHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PacketReq<SingleChatMsgReqMessage> singleChatMsgReqMessagePacketReq) throws Exception {

        log.info("输出结果，");

    }
}
