package com.stan.zhangli.handler;

import com.stan.zhangli.entities.request.LoginInfoReqPacket;
import com.stan.zhangli.entities.request.PacketReq;
import com.stan.zhangli.entities.response.PacketRes;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static com.stan.zhangli.handler.command.BackCodeCommand.MEANINGLESS_CODE;
import static com.stan.zhangli.handler.command.ResCommand.LOGIN_RESPONSE_CODE;
import static com.stan.zhangli.handler.command.StatusCodeCommand.SUCCESS_CODE;
import static com.stan.zhangli.handler.command.StatusCodeCommand.SUCCESS_MESSAGE;

/**
 * @author:郭境科
 * @createdate:2020年4月9日
 * @description:身份认证handler
 */
@ChannelHandler.Sharable
@Component
public class LoginHandler extends SimpleChannelInboundHandler<PacketReq<LoginInfoReqPacket>> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        if (msg instanceof PacketReq<?>) {
            return ((PacketReq<?>) msg).getData() instanceof LoginInfoReqPacket;
        }
        return false;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketReq<LoginInfoReqPacket> msg) throws Exception {

        PacketRes<String> packetRes = new PacketRes<>();
        LoginInfoReqPacket loginInfoReqPacket = msg.getData();

        packetRes.setPushType(LOGIN_RESPONSE_CODE);
        packetRes.setStatusCode(SUCCESS_CODE);
        packetRes.setBackCode(MEANINGLESS_CODE);
        packetRes.setMessage(SUCCESS_MESSAGE);
        packetRes.setData(null);
        ctx.writeAndFlush(packetRes);
    }
}
