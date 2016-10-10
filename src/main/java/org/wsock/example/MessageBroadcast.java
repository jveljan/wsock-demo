package org.wsock.example;

/**
 * Created by joco on 10.10.16.
 */
public class MessageBroadcast {
    private String nick;
    private String msg;
    private long timestamp;

    public MessageBroadcast(String nick, String msg) {
        this.nick = nick;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
