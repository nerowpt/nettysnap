import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import protocol.command.ServerLoginHandle;

import java.util.Date;

public class NettyServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootStrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootStrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        //nioSocketChannel.pipeline().addLast(new StringDecoder());
                        /*nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                System.out.println(Thread.currentThread().getName());
                                System.out.println(s);
                            }
                        });*/
                        //nioSocketChannel.pipeline().addLast(new FirstServerHandle());
                        nioSocketChannel.pipeline().addLast(new ServerLoginHandle());
                    }
                });
        Bind.bind(serverBootStrap,1000);
    }

    public static class FirstServerHandle extends ChannelInboundHandlerAdapter {
        public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
            System.out.println(new Date() + ": 客户端写出数据");
            ByteBuf buffer = channelHandlerContext.alloc().buffer();
            byte[] bytes = "你好，这里是来自客户端的消息!".getBytes("utf-8");
            buffer.writeBytes(bytes);
            channelHandlerContext.channel().writeAndFlush(buffer);
        }
    }
}
