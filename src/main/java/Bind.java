import io.netty.bootstrap.AbstractBootstrap;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class Bind {
    public static void bind(final AbstractBootstrap bootstrap, int port) {
        final int p = port;
        bootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("端口:" + p + " 绑定成功!");
                } else {
                   System.out.println("端口:" + p + " 绑定失败!");
                   bind(bootstrap,p+1);
                }
            }
        });
    }
}
