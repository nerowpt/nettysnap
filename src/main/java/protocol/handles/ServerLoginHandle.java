package protocol.handles;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.PacketCodeC;
import protocol.packet.*;

import java.util.Date;

public class ServerLoginHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录……");
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.getInstance().decode(byteBuf);

        Packet packetResponse;
        if (packet instanceof LoginRequestPacket) {
            packetResponse = new LoginResponsePacket();
            packetResponse.setVersion(packet.getVersion());
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            System.out.println(new Date() + ": 开始登录校验");
            if (loginValid(loginRequestPacket)) {
                ((LoginResponsePacket)packetResponse).setReason("登录成功！");
                ((LoginResponsePacket)packetResponse).setSuccess(true);
                System.out.println(new Date() + ": 校验通过 " + loginRequestPacket.getUserName());
            } else {
                ((LoginResponsePacket)packetResponse).setReason("登录失败！");
                ((LoginResponsePacket)packetResponse).setSuccess(false);
                System.out.println(new Date() + ": 校验失败 " + loginRequestPacket.getUserName());
            }
        } else if (packet instanceof MsgRequestPacket) {
            packetResponse = new MsgResponsePacket();
            packetResponse.setVersion(packet.getVersion());
            MsgRequestPacket msgRequestPacket = (MsgRequestPacket) packet;
            String msgRequestPacketMsg = msgRequestPacket.getMsg();
            System.out.println(new Date() + ": 收到客户端消息: " + msgRequestPacketMsg);
            ((MsgResponsePacket)packetResponse).setMsg("服务端回复【" + msgRequestPacketMsg + "】");
        } else {
            packetResponse = new LoginResponsePacket();
            packetResponse.setVersion(packet.getVersion());
            ((LoginResponsePacket)packetResponse).setReason("not loginRequestPacket!");
            ((LoginResponsePacket)packetResponse).setSuccess(false);
        }
        ByteBuf buf = PacketCodeC.getInstance().encode(ctx.alloc(),packetResponse);
        ctx.channel().writeAndFlush(buf);
    }

    private boolean loginValid(LoginRequestPacket loginRequestPacket) {
        Integer userId = loginRequestPacket.getUserId();
        String userName = loginRequestPacket.getUserName();
        String password = loginRequestPacket.getPassword();
        return 123==userId && "nero".equals(userName) && "abc".equals(password);
    }
}
