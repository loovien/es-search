package tv.zhanqi.essearch.svr;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.zhanqi.essearch.comm.Codec;
import tv.zhanqi.essearch.comm.Commands;
import tv.zhanqi.essearch.ctl.LoginController;
import tv.zhanqi.essearch.exceptions.BizException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class EsServerHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(EsServerHandler.class);

    private final ConcurrentHashMap<String, Channel> channelsPool = new ConcurrentHashMap<>();

    //    @Override
    public void channelActive1(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("receive remote address connection: {}", channel.remoteAddress().toString());
        channelsPool.put(channel.remoteAddress().toString(), channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bytesBuffer = (ByteBuf) msg;
        String fromAddr = ctx.channel().remoteAddress().toString();
        try {
            Commands commands = Codec.read(bytesBuffer);
            if (!commands.getHandlerClass().isInstance(new LoginController())) {
                throw new BizException("connect login required");
            }
            String result = commands.getHandlerClass().getConstructor().newInstance()
                    .handler(ctx, channelsPool, commands);
            logger.info("socket: {} authorization success return:{}", fromAddr, result);
            ctx.writeAndFlush(ctx.alloc().buffer().writeBytes(result.getBytes(StandardCharsets.UTF_8)));
            while (bytesBuffer.isReadable()) { // loop for read data
                commands = Codec.read(bytesBuffer);
                result = commands.getHandlerClass().getConstructor().newInstance()
                        .handler(ctx, channelsPool, commands);
                logger.info("socket: {} command: {} return :{} success", fromAddr, commands, result);
                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes(result.getBytes(StandardCharsets.UTF_8)));
            }
        } catch (Exception e) {
            logger.error("socket: {} occur exception: {}", fromAddr, e.getMessage());
            ctx.close();
        } finally {
            bytesBuffer.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server channel process exception: {}, {}", ctx.name(), cause.toString());
        ctx.close();
    }
}
