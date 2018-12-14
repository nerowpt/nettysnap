package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.command.PacketCodeC;
import protocol.login.LoginRequestPacket;
import protocol.login.LoginResponsePacket;

import java.util.Date;

public class ClientLoginHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录");

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserName("nero");
        loginRequestPacket.setPassword("abc");
        loginRequestPacket.setUserId(123);

        ByteBuf byteBuf = PacketCodeC.getInstance().encode(ctx.alloc(),loginRequestPacket);

        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodeC.getInstance().decode(byteBuf);

        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if (loginResponsePacket.isSuccess()) {
                System.out.println(loginResponsePacket.getReason());
            } else {
                System.out.println("登录失败，失败原因：" + loginResponsePacket.getReason());
            }
        } else {
            System.out.println("not loginResponsePacket!");
        }

    }
}
