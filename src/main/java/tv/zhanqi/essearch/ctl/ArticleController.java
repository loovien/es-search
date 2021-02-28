package tv.zhanqi.essearch.ctl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import tv.zhanqi.essearch.comm.Commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

public class ArticleController implements BizController {
    public String handler(ChannelHandlerContext ctx, ConcurrentHashMap<String, Channel> channelsPoll, Commands data) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return "{\"cmdid\":\"article\", \"time\":" + date + " }";
    }
}
