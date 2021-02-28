package tv.zhanqi.essearch.svr;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Component
public class EsSearchSvr implements ApplicationRunner {

    private EsServerOpts esServerOpts;

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossEventGroup;

    private EventLoopGroup workEventGroup;

    @Autowired
    public EsSearchSvr(EsServerOpts esServerOpts) {
        this.esServerOpts = esServerOpts;
        this.bootstrap = new ServerBootstrap();
        this.bossEventGroup = new NioEventLoopGroup(esServerOpts.getServerBossNum());
        this.workEventGroup = new NioEventLoopGroup(esServerOpts.getServerWorkNum());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ServerBootstrap serverBootstrap = this.bootstrap.group(bossEventGroup, workEventGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EsServerHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, esServerOpts.getServerTcpBacklog())
                .childOption(ChannelOption.SO_TIMEOUT, 10000)
                .childOption(ChannelOption.SO_KEEPALIVE, esServerOpts.getServerTcpKeepalive());


        log.info("elastic search listen: {}:{}", esServerOpts.getServerHost(), esServerOpts.getServerPort());
        ChannelFuture channelFuture = serverBootstrap.bind(esServerOpts.getServerHost(), esServerOpts.getServerPort()).sync();
        channelFuture.channel().closeFuture();
    }

    public void shutdown() {
        this.bossEventGroup.shutdownGracefully();
        this.workEventGroup.shutdownGracefully();
    }
}
