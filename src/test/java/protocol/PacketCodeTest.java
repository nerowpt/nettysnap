package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Assert;
import org.junit.Test;
import protocol.packet.LoginRequestPacket;
import protocol.packet.Packet;

public class PacketCodeTest {
    @Test
    public void encode(){
        Serializer serializer = new JsonSerializer();
        LoginRequestPacket loginPacket = new LoginRequestPacket();

        loginPacket.setVersion((byte) 1);
        loginPacket.setUserId(123);
        loginPacket.setUserName("nero");
        loginPacket.setPassword("abc");

        PacketCodeC packetCodeC = PacketCodeC.getInstance();
        ByteBuf byteBuf = packetCodeC.encode(ByteBufAllocator.DEFAULT,loginPacket);
        Packet packet = packetCodeC.decode(byteBuf);

        Assert.assertArrayEquals(serializer.serializer(loginPacket),serializer.serializer(packet));
    }
}
