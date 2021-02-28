package tv.zhanqi.essearch.ctl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import tv.zhanqi.essearch.comm.Commands;

import java.util.concurrent.ConcurrentHashMap;

public interface BizController {
    String handler(ChannelHandlerContext ctx, ConcurrentHashMap<String, Channel> channelsPoll, Commands data) throws Exception;
}
