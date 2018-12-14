package protocol.command;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.login.LoginRequestPacket;
import protocol.Packet;
import protocol.login.LoginResponsePacket;

import java.util.Date;

public class ServerLoginHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录……");
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.getInstance().decode(byteBuf);

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(packet.getVersion());
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            System.out.println(new Date() + ": 开始登录校验");
            if (loginValid(loginRequestPacket)) {
                loginResponsePacket.setReason("登录成功！");
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + ": 校验通过 " + loginRequestPacket.getUserName());
            } else {
                loginResponsePacket.setReason("登录失败！");
                loginResponsePacket.setSuccess(false);
                System.out.println(new Date() + ": 校验失败 " + loginRequestPacket.getUserName());
            }
        } else {
            loginResponsePacket.setReason("not loginRequestPacket!");
            loginResponsePacket.setSuccess(false);
        }
        ByteBuf buf = PacketCodeC.getInstance().encode(ctx.alloc(),loginResponsePacket);
        ctx.channel().writeAndFlush(buf);
    }

    private boolean loginValid(LoginRequestPacket loginRequestPacket) {
        Integer userId = loginRequestPacket.getUserId();
        String userName = loginRequestPacket.getUserName();
        String password = loginRequestPacket.getPassword();
        return 123==userId && "nero".equals(userName) && "abc".equals(password);
    }
}
