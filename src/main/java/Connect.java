import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import protocol.constant.Constant;

import java.util.concurrent.TimeUnit;

public class Connect {
    public static ChannelFuture connect(final AbstractBootstrap bootstrap, String host, int port, int tryTime) {
        return ((Bootstrap) bootstrap).connect(host,port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功！");
            } else {
                int order = (Constant.MAX_CONNECT_TRY_TIME - tryTime) + 1;
                System.out.println("第" + order + "次连接失败,尝试重连");
                int delay = 1 << order;
                bootstrap.config().group().schedule(()->{
                    connect(bootstrap,host,port,tryTime-1);
                },delay, TimeUnit.SECONDS);
            }
        });
    }
}
