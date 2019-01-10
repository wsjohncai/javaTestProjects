package com.example.client;

import com.example.misc.Message;

public class MsgUnit {
    private User user;
    private Message msg;
    private String msgId;
    private int timeout = 8;

    MsgUnit(User user, Message msg) {
        this.user = user;
        this.msg = msg;
        this.msgId = msg.getMsgId();
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public User getUser() {
        return user;
    }

    public Message getMsg() {
        return msg;
    }

    public String getMsgId() {
        return msgId;
    }
}
