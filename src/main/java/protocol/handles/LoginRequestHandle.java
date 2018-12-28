package protocol.handles;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.packet.LoginRequestPacket;
import protocol.packet.LoginResponsePacket;

import java.util.Date;

public class LoginRequestHandle extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws Exception {
        if (loginValid(loginRequestPacket)) {
            System.out.println(new Date() + ": 校验通过 " + loginRequestPacket.getUserName());
        } else {
            System.out.println(new Date() + ": 校验失败 " + loginRequestPacket.getUserName());
        }
    }

    private boolean loginValid(LoginRequestPacket loginRequestPacket) {
        Integer userId = loginRequestPacket.getUserId();
        String userName = loginRequestPacket.getUserName();
        String password = loginRequestPacket.getPassword();
        return 123==userId && "nero".equals(userName) && "abc".equals(password);
    }
}
