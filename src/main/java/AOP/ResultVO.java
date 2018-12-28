package AOP;

public class ResultVO {
    String msg;

    public boolean isLogMonitor() {
        return logMonitor;
    }

    public void setLogMonitor(boolean logMonitor) {
        this.logMonitor = logMonitor;
    }

    boolean logMonitor;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
