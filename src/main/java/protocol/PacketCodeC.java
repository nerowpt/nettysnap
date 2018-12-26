package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import protocol.packet.*;
import protocol.constant.Command;

import java.util.HashMap;
import java.util.Map;

public class PacketCodeC {
    private static volatile PacketCodeC instance;

    private PacketCodeC() {}

    public static PacketCodeC getInstance() {
        if (instance == null) {
            synchronized (PacketCodeC.class) {
                return new PacketCodeC();
            }
        }
        return instance;
    }

    private static final int MAGIC_NUMBER = 0x12345678;
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        serializerMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MSG_REQUEST, MsgRequestPacket.class);
        packetTypeMap.put(Command.MSG_RESPONSE, MsgResponsePacket.class);
        Serializer jsonSerializer = new JsonSerializer();
        serializerMap.put(jsonSerializer.getSerializerAlgorithm(), jsonSerializer);
    }

    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        byte[] bytes = Serializer.DEFAULT.serializer(packet);
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        byteBuf.skipBytes(4);
        byteBuf.skipBytes(1);
        Byte serializerAlgorithm = byteBuf.readByte();
        Byte command = byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializerAlgorithm);

        if (requestType!=null&&serializer!=null) {
            return serializer.deserializer(bytes,requestType);
        }
        return null;
    }

    private Serializer getSerializer(Byte serializerAlgorithm) {
        return serializerMap.get(serializerAlgorithm);
    }

    private Class<? extends Packet> getRequestType(Byte command) {
        return packetTypeMap.get(command);
    }
}
