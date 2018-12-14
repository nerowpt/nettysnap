import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteBufTest {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(9,150);
        print("allocate ByteBuf(9, 100)",buf);
        buf.writeBytes(new byte[]{1,2,3,4});
        print("writeBytes(1,2,3,4)",buf);
        buf.writeInt(12);
        print("writeInt(12)", buf);
        buf.writeBytes(new byte[]{5});
        print("writeBytes(5)", buf);
        buf.writeBytes(new byte[]{6});
        print("writeBytes(6)", buf);
        buf.writeBytes(new byte[64]);
        print("writeBytes(64)", buf);

        System.out.println("getByte(3) return: " + buf.getByte(3));
        System.out.println("getShort(3) return: " + buf.getShort(3));
        System.out.println("getInt(3) return: " + buf.getInt(3));
        print("getByte()", buf);

        buf.setByte(buf.readableBytes() + 1, 0);
        print("setByte()", buf);

        byte[] dst = new byte[buf.readableBytes()];
        ByteBuf b = buf.readBytes(dst);
        print("readBytes(" + dst.length + ")", buf);
    }

    private static void print(String action, ByteBuf buf) {
        System.out.println("after ===========" + action + "============");
        System.out.println("capacity(): " + buf.capacity());
        System.out.println("maxCapacity(): " + buf.maxCapacity());
        System.out.println("readerIndex(): " + buf.readerIndex());
        System.out.println("readableBytes(): " + buf.readableBytes());
        System.out.println("isReadable(): " + buf.isReadable());
        System.out.println("writerIndex(): " + buf.writerIndex());
        System.out.println("writableBytes(): " + buf.writableBytes());
        System.out.println("isWritable(): " + buf.isWritable());
        System.out.println("maxWritableBytes(): " + buf.maxWritableBytes());
        System.out.println();
    }
}
