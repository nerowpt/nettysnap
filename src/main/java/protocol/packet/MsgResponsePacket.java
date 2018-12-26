package protocol.packet;

import protocol.constant.Command;

public class MsgResponsePacket extends Packet {
    private String msg;

    @Override
    public Byte getCommand() {
        return Command.MSG_RESPONSE;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
