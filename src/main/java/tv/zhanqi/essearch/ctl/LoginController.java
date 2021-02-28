package tv.zhanqi.essearch.ctl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import tv.zhanqi.essearch.comm.Commands;
import tv.zhanqi.essearch.dto.LoginDto;
import tv.zhanqi.essearch.exceptions.BizException;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
public class LoginController extends tv.zhanqi.essearch.ctl.Controller implements BizController {
    @Override
    public String handler(ChannelHandlerContext ctx, ConcurrentHashMap<String, Channel> channelsPoll, Commands data) throws IOException {
        LoginDto loginDto = this.objectMapper.readValue(data.getBody(), LoginDto.class);
        log.info("controller login: {}", loginDto);
        if (loginDto.getToken().equals("admin")) {
            return wrapOKResult(0, "success", null);
        }
        throw new BizException("token invalid");
    }
}
