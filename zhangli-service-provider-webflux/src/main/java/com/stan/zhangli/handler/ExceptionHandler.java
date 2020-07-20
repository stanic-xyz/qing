/**
 * @author:郭境科
 * @createdate:2020年4月16日
 * @description: 全局异常处理handler
 */
package com.stan.zhangli.handler;

import com.alibaba.fastjson.JSONException;
import com.stan.zhangli.entities.request.PacketReq;
import com.stan.zhangli.entities.response.PacketRes;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.stan.zhangli.handler.command.BackCodeCommand.MEANINGLESS_CODE;
import static com.stan.zhangli.handler.command.ResCommand.EXCEPTION_RESPONSE_CODE;
import static com.stan.zhangli.handler.command.StatusCodeCommand.*;

@ChannelHandler.Sharable
@Component
public class ExceptionHandler extends SimpleChannelInboundHandler<PacketReq<?>> {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketReq<?> msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Exception e = (Exception) cause;
        log.error(e.toString());
        PacketRes<?> packetRes = new PacketRes<>();
        String[] exceptionMessageArrays = e.getMessage().split(",");
        StringBuffer exceptionMessage = new StringBuffer();

        for (int i = 0; i < exceptionMessageArrays.length; i++) {
            exceptionMessage.append(exceptionMessageArrays[i]);
            if (i < exceptionMessageArrays.length - 1) {
                exceptionMessage.append(",");
            }
        }

        if (e instanceof JSONException || e instanceof DecoderException) {
            packetRes.setPushType(EXCEPTION_RESPONSE_CODE);
            packetRes.setStatusCode(WRONGFUL_PARAMETER_CODE);
            packetRes.setBackCode(MEANINGLESS_CODE);
            packetRes.setMessage(WRONGFUL_PARAMETER_MESSAGE + ":JSON格式错误");
            packetRes.setData(null);
            ctx.writeAndFlush(packetRes);
        } else {
            packetRes.setPushType(EXCEPTION_RESPONSE_CODE);
            packetRes.setStatusCode(SERVER_EXCEPTION_CODE);
            packetRes.setBackCode(MEANINGLESS_CODE);
            packetRes.setMessage(SERVER_EXCEPTION_MESSAGE);
            packetRes.setData(null);
            ctx.writeAndFlush(packetRes);
        }
    }

}
