package com.stan.zhangli;

import com.stan.zhangli.config.EchoServerConfig;
import com.stan.zhangli.handler.ExceptionHandler;
import com.stan.zhangli.handler.PackageCodeCHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author: 陈云龙
 * @date: 2020/7/7
 * @description Netty 应答机
 */

@Configuration
public class EchoServer {

    private Logger logger = LoggerFactory.getLogger(EchoServer.class);

    @Autowired
    private EchoServerConfig echoServerConfi;
    @Autowired
    private ExceptionHandler exceptionHandler;
    @Autowired
    private IdleStateHandler idleStateHandler;
    @Autowired
    private PackageCodeCHandler packetCodecHandler;
    private ServerBootstrap bootstrap;
    private ChannelFuture channelFuture;


    @PostConstruct
    public void start() throws Exception {

        if (echoServerConfi.getPort() == 0)
            throw new Exception("端口配置错误");


        bootstrap = new ServerBootstrap();


        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ChannelInitializer initializer = new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new ChunkedWriteHandler());
                pipeline.addLast(new HttpObjectAggregator(65536));
                pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                pipeline.addLast(new WebSocketServerProtocolHandler("/", null, true));
                pipeline.addLast(idleStateHandler);
                pipeline.addLast(packetCodecHandler);
                pipeline.addLast(exceptionHandler);
            }
        };
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(initializer);
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO));

        channelFuture = bootstrap.bind(echoServerConfi.getPort());
        channelFuture.sync();
        channelFuture.addListener(future -> {
            if (future.isSuccess())
                logger.info("Echo 服务启动成功");
            else
                logger.info("echo 服务启动失败");
        });

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                close();
            }
        });

    }

    private void close() {
        if (bootstrap == null) {
            logger.info("WebSocket server is not running!");
            return;
        }
        logger.info("WebSocket server is stopping");
        if (channelFuture != null) {
            channelFuture.channel().close().awaitUninterruptibly(10, TimeUnit.SECONDS);
            channelFuture = null;
        }
        if (bootstrap != null && bootstrap.config().group() != null) {
            bootstrap.config().group().shutdownGracefully();
        }
        if (bootstrap != null && bootstrap.config().childGroup() != null) {
            bootstrap.config().childGroup().shutdownGracefully();
        }
        bootstrap = null;

        logger.info("WebSocket server stopped");
    }


}
