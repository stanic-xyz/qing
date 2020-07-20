package com.stan.zhangli.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author:郭境科
 * @createdate:2020年4月12日
 * @description:空闲检测
 */

@ChannelHandler.Sharable
@Component
public class IMIdleStateHandler extends IdleStateHandler {

    private final static int READER_IDLE_TIME = 30;
    private static final Logger log = LoggerFactory.getLogger(IMIdleStateHandler.class);

    public IMIdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        log.info(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
        if (evt.state() == IdleState.READER_IDLE) {
            ctx.channel().close();
        }

    }
}