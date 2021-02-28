package tv.zhanqi.essearch.comm;

import io.netty.buffer.ByteBuf;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.zhanqi.essearch.exceptions.BizException;

public class Codec {

    private final static Logger logger = LoggerFactory.getLogger(Codec.class);

    private final static int lengthOfPkg = 4;

    private final static int typeOfCommand = 4;

    public static Commands read(ByteBuf byteBuf) {
        int length = byteBuf.getIntLE(0);
        int cmdid = byteBuf.getIntLE(4);
        byte[] body = new byte[length];
        byteBuf.readBytes(body, lengthOfPkg + typeOfCommand, length);
        logger.info("receive detail => length: {}, cmdid: {}, data: {}", length, cmdid, new String(body));
        for (Commands command : Commands.values()) {
            if (cmdid == command.getCmdid()) {
                command.setBody(body);
                return command;
            }
        }
        throw new BizException("command not implemented!");
    }

}
