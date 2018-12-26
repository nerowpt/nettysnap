import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.AbstractByteBufAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import protocol.PacketCodeC;
import protocol.constant.Constant;
import protocol.packet.MsgRequestPacket;
import protocol.utils.LoginUtil;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Connect {
    public static ChannelFuture connect(final AbstractBootstrap bootstrap, String host, int port, int tryTime) {
        return ((Bootstrap) bootstrap).connect(host,port).addListener(future -> {
            if (future.isSuccess()) {
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
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

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()){
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MsgRequestPacket msgRequestPacket = new MsgRequestPacket();
                    msgRequestPacket.setMsg(line);
                    ByteBuf byteBuf = PacketCodeC.getInstance().encode(channel.alloc(),msgRequestPacket);
                    channel.writeAndFlush(byteBuf);
                }
            }
        }).start();
    }
}
