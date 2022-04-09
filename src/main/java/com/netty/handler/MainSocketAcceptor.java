package com.netty.handler;

import com.netty.coder.AppMessageEncoder;
import com.netty.coder.WebMessageDecoder;
import com.netty.coder.WebMessageEncoder;
import com.netty.config.ChannelAttr;
import com.netty.model.Ping;
import com.netty.model.SentBody;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author hzy
 * @date 2022-04-02
 */
public class MainSocketAcceptor  extends SimpleChannelInboundHandler<SentBody> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainSocketAcceptor.class);
    private static final int PONG_TIME_OUT_COUNT = 3;
    private final ThreadFactory bossThreadFactory;
    private final ThreadFactory workerThreadFactory;
    private EventLoopGroup appBossGroup;
    private EventLoopGroup appWorkerGroup;
    private EventLoopGroup webBossGroup;
    private EventLoopGroup webWorkerGroup;
    private final Integer appPort;
    private final Integer webPort;
    private final CIMRequestHandler outerRequestHandler;
    private final ChannelHandler loggingHandler = new LoggingHandler();
    public final Duration writeIdle = Duration.ofSeconds(45L);
    public final Duration readIdle = Duration.ofSeconds(60L);


    public MainSocketAcceptor(MainSocketAcceptor.Builder builder) {
        this.webPort = builder.webPort;
        this.appPort = builder.appPort;
        this.outerRequestHandler = builder.outerRequestHandler;
        this.bossThreadFactory = (r) -> {
            Thread thread = new Thread(r);
            thread.setName("nio-boss-");
            return thread;
        };
        this.workerThreadFactory = (r) -> {
            Thread thread = new Thread(r);
            thread.setName("nio-worker-");
            return thread;
        };
    }

    private void createWebEventGroup() {
        if (this.isLinuxSystem()) {
            this.webBossGroup = new EpollEventLoopGroup(this.bossThreadFactory);
            this.webWorkerGroup = new EpollEventLoopGroup(this.workerThreadFactory);
        } else {
            this.webBossGroup = new NioEventLoopGroup(this.bossThreadFactory);
            this.webWorkerGroup = new NioEventLoopGroup(this.workerThreadFactory);
        }

    }

    private void createAppEventGroup() {
        if (this.isLinuxSystem()) {
            this.appBossGroup = new EpollEventLoopGroup(this.bossThreadFactory);
            this.appWorkerGroup = new EpollEventLoopGroup(this.workerThreadFactory);
        } else {
            this.appBossGroup = new NioEventLoopGroup(this.bossThreadFactory);
            this.appWorkerGroup = new NioEventLoopGroup(this.workerThreadFactory);
        }

    }

    public void bind() {
        if (this.appPort != null) {
            this.bindAppPort();
        }

        if (this.webPort != null) {
            this.bindWebPort();
        }

    }

    public void destroy(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        if (bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown()) {
            try {
                bossGroup.shutdownGracefully();
            } catch (Exception var5) {
            }
        }

        if (workerGroup != null && !workerGroup.isShuttingDown() && !workerGroup.isShutdown()) {
            try {
                workerGroup.shutdownGracefully();
            } catch (Exception var4) {
            }
        }

    }

    public void destroy() {
        this.destroy(this.appBossGroup, this.appWorkerGroup);
        this.destroy(this.webBossGroup, this.webWorkerGroup);
    }

    private void bindAppPort() {
        this.createAppEventGroup();
        ServerBootstrap bootstrap = this.createServerBootstrap(this.appBossGroup, this.appWorkerGroup);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new ChannelHandler[]{new AppMessageDecoder()});
                ch.pipeline().addLast(new ChannelHandler[]{new AppMessageEncoder()});
                ch.pipeline().addLast(new ChannelHandler[]{MainSocketAcceptor.this.loggingHandler});
                ch.pipeline().addLast(new ChannelHandler[]{new IdleStateHandler(MainSocketAcceptor.this.readIdle.getSeconds(), MainSocketAcceptor.this.writeIdle.getSeconds(), 0L, TimeUnit.SECONDS)});
                ch.pipeline().addLast(new ChannelHandler[]{MainSocketAcceptor.this});
            }
        });
        ChannelFuture channelFuture = bootstrap.bind(this.appPort).syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener((future) -> {
            String logBanner = "\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n*                                                                                   *\n*                                                                                   *\n*                   App Socket Server started on port {}.                        *\n*                                                                                   *\n*                                                                                   *\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n";
            LOGGER.info(logBanner, this.appPort);
        });
        channelFuture.channel().closeFuture().addListener((future) -> {
            this.destroy(this.appBossGroup, this.appWorkerGroup);
        });
    }

    private void bindWebPort() {
        this.createWebEventGroup();
        ServerBootstrap bootstrap = this.createServerBootstrap(this.webBossGroup, this.webWorkerGroup);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new ChannelHandler[]{new HttpServerCodec()});
                ch.pipeline().addLast(new ChannelHandler[]{new ChunkedWriteHandler()});
                ch.pipeline().addLast(new ChannelHandler[]{new HttpObjectAggregator(65536)});
                ch.pipeline().addLast(new ChannelHandler[]{new WebSocketServerProtocolHandler("/", false)});
                ch.pipeline().addLast(new ChannelHandler[]{new WebMessageDecoder()});
                ch.pipeline().addLast(new ChannelHandler[]{new WebMessageEncoder()});
                ch.pipeline().addLast(new ChannelHandler[]{MainSocketAcceptor.this.loggingHandler});
                ch.pipeline().addLast(new ChannelHandler[]{new IdleStateHandler(MainSocketAcceptor.this.readIdle.getSeconds(), MainSocketAcceptor.this.writeIdle.getSeconds(), 0L, TimeUnit.SECONDS)});
                ch.pipeline().addLast(new ChannelHandler[]{MainSocketAcceptor.this});
            }
        });
        ChannelFuture channelFuture = bootstrap.bind(this.webPort).syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener((future) -> {
            String logBanner = "\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n*                                                                                   *\n*                                                                                   *\n*                   Websocket Server started on port {}.                         *\n*                                                                                   *\n*                                                                                   *\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n";
            LOGGER.info(logBanner, this.webPort);
        });
        channelFuture.channel().closeFuture().addListener((future) -> {
            this.destroy(this.webBossGroup, this.webWorkerGroup);
        });
    }

    private ServerBootstrap createServerBootstrap(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.channel(this.isLinuxSystem() ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
        return bootstrap;
    }

    protected void channelRead0(ChannelHandlerContext ctx, SentBody body) {
        this.outerRequestHandler.process(ctx.channel(), body);
    }

    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().attr(ChannelAttr.ID).set(ctx.channel().id().asShortText());
    }

    public void channelInactive(ChannelHandlerContext ctx) {
        if (ctx.channel().attr(ChannelAttr.UID) != null) {
            SentBody body = new SentBody();
            body.setKey("client_closed");
            this.outerRequestHandler.process(ctx.channel(), body);
        }
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleEvent = (IdleStateEvent)evt;
            String uid = (String)ctx.channel().attr(ChannelAttr.UID).get();
            if (idleEvent.state() == IdleState.WRITER_IDLE && uid == null) {
                ctx.close();
            } else {
                Integer pingCount;
                if (idleEvent.state() == IdleState.WRITER_IDLE && uid != null) {
                    pingCount = (Integer)ctx.channel().attr(ChannelAttr.PING_COUNT).get();
                    ctx.channel().attr(ChannelAttr.PING_COUNT).set(pingCount == null ? 1 : pingCount + 1);
                    ctx.channel().writeAndFlush(Ping.getInstance());
                } else {
                    pingCount = (Integer)ctx.channel().attr(ChannelAttr.PING_COUNT).get();
                    if (idleEvent.state() == IdleState.READER_IDLE && pingCount != null && pingCount >= 3) {
                        ctx.close();
                        LOGGER.info("{} pong timeout.", ctx.channel());
                    }

                }
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.warn("EXCEPTION", cause);
    }

    private boolean isLinuxSystem() {
        String osName = System.getProperty("os.name").toLowerCase();
        System.out.println(osName);
        return osName.contains("linux");
    }

    public static class Builder {
        private Integer appPort;
        private Integer webPort;
        private CIMRequestHandler outerRequestHandler;

        public Builder() {
        }

        public MainSocketAcceptor.Builder setAppPort(Integer appPort) {
            this.appPort = appPort;
            return this;
        }

        public MainSocketAcceptor.Builder setWebsocketPort(Integer port) {
            this.webPort = port;
            return this;
        }

        public MainSocketAcceptor.Builder setOuterRequestHandler(CIMRequestHandler outerRequestHandler) {
            this.outerRequestHandler = outerRequestHandler;
            return this;
        }

        public MainSocketAcceptor build() {
            return new MainSocketAcceptor(this);
        }
    }


}
