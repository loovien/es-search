package tv.zhanqi.essearch.comm;

import tv.zhanqi.essearch.ctl.ArticleController;
import tv.zhanqi.essearch.ctl.BizController;
import tv.zhanqi.essearch.ctl.LoginController;

public enum Commands {


    COMMAND_LOGIN(0, LoginController.class),

    COMMAND_ARTICLE(1, ArticleController.class);


    private byte[] body;

    private final int cmdid;

    private final Class<? extends BizController> handlerClass;

    Commands(int cmdid, Class<? extends BizController> handlerClass) {
        this.cmdid = cmdid;
        this.handlerClass = handlerClass;
    }


    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return this.body;
    }

    public int getCmdid() {
        return cmdid;
    }

    public Class<? extends BizController> getHandlerClass() {
        return handlerClass;
    }
}
