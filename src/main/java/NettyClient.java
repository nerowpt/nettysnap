import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import protocol.ClientLoginHandle;
import protocol.constant.Constant;

import java.nio.charset.Charset;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel channel) throws Exception {
                        //channel.pipeline().addLast(new StringEncoder());
                        //channel.pipeline().addLast(new FirstClientHandle());
                        channel.pipeline().addLast(new ClientLoginHandle());
                    }
                });

        Channel channel = Connect.connect(bootstrap,"127.0.0.1",1000, Constant.MAX_CONNECT_TRY_TIME).channel();
        //channel.attr().set();

        /*while (true){
            channel.writeAndFlush(new Date() + ": hello world");
            Thread.sleep(2000);
        }*/
    }

    public static class FirstClientHandle extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(byteBuf.toString(Charset.forName("utf-8")));
        }
    }
}
