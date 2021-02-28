package tv.zhanqi.essearch;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tv.zhanqi.essearch.svr.EsServerOpts;

import java.nio.charset.StandardCharsets;

//@SpringBootTest
class EsSearchApplicationTests {

    @Autowired
    EsServerOpts esServerOpts;

    @Test
    public void contextLoads() {
        System.out.println(esServerOpts);
    }

    @Test
    public void testConn() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap().group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                System.out.println("============== receive from server =================");
                                int byteLength = byteBuf.readIntLE();
                                byte[] bytes = new byte[byteLength];
                                byteBuf.readBytes(bytes);
                                System.out.println(new String(bytes));
                                byteBuf.release();
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                byte[] body = "{\"cmdid\":\"echo\", \"uid\": 120}".getBytes(StandardCharsets.UTF_8);
                                ByteBuf byteBuf = ctx.alloc().buffer(body.length + 4);
                                byteBuf.writeIntLE(body.length);
                                System.out.println("write message: " + new String(body));
                                byteBuf.writeBytes(body);
                                ctx.writeAndFlush(byteBuf);

                                ByteBuf byteBuf1 = ctx.alloc().buffer(body.length + 4);
                                byteBuf1.writeIntLE(body.length);
                                System.out.println("write message1: " + new String(body));
                                byteBuf1.writeBytes(body);
                                ctx.writeAndFlush(byteBuf1);

                                ctx.close();
                            }
                        });
                    }
                });
        ChannelFuture localhost = bootstrap.connect("0.0.0.0", 8081).sync();
        localhost.channel().closeFuture().sync();
        System.out.println("closed client channel");
    }

}
