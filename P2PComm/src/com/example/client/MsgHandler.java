package com.example.client;

import com.example.misc.IPConverter;
import com.example.misc.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Vector;

import static com.example.misc.Constants.*;

public class MsgHandler extends Thread {
    static MsgHandler handler;
    private Vector<MsgUnit> messages = new Vector<>();

    private MsgHandler() {
    }

    static MsgHandler getInstance() {
        if (handler == null) {
            synchronized (RequestHandler.class) {
                if (handler == null) {
                    handler = new MsgHandler();
                }
            }
        }
        return handler;
    }

    @Override
    public void run() {
        while (messages.size() > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendAll();
        }
    }

    void send(User u, Message msg) {
        if (u.getIp() != null && !u.getIp().isEmpty() && u.getPort() != 0) {
            try {
                sendOne(u, msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sendReq(u.getId());
        }
        messages.add(new MsgUnit(u, msg));
        if (handler.isAlive())
            handler.start();
    }

    private void sendReq(String id) {
        byte[] buf = (COMM_REQUEST + SEP_CODE + ClientMain.user.getId() + SEP_VALUE + id).getBytes();
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        try {
            p.setAddress(InetAddress.getByAddress(IPConverter.getByIPv4Address(SERVERIP)));
            p.setPort(SERVERPORT);
            ClientMain.client.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendOne(User u, Message msg) throws IOException {
//        Log.info(("Sending a message to user: " + u.getId() + " at " + u.getIp() + ":" + u.getPort());)
        byte[] msgb = (COMM_MSG + SEP_CODE + msg.getMsgId() + SEP_VALUE + ClientMain.user.getId() +
                SEP_VALUE + msg.getToId() + SEP_VALUE + msg.getMessage()).getBytes();
        DatagramPacket p = new DatagramPacket(msgb, msgb.length);
        p.setAddress(InetAddress.getByAddress(IPConverter.getByIPv4Address(u.getIp())));
        p.setPort(u.getPort());
        ClientMain.client.send(p);
    }

    //遍历消息队列并逐个发送
    void sendAll() {
        for (int i = 0; i < messages.size(); ) {
            MsgUnit mu = messages.get(i);
            User u = mu.getUser();
            if (mu.getTimeout() <= 0) {
                messages.remove(i);
                u.setIp(null);
                u.setPort(0);
                sendReq(u.getId());
                continue;
            }
            mu.setTimeout(mu.getTimeout() - 1);
            Message msg = mu.getMsg();
            i++;
            if (u.getIp() == null || u.getIp().isEmpty() || u.getPort() == 0)
                continue;
            try {
                sendOne(u, msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void deleteMsg(String msgId) {
        for (int i = 0; i < messages.size(); i++) {
            MsgUnit mu = messages.get(i);
            if (mu.getMsgId().equals(msgId)) {
                messages.remove(i);
            }
        }
    }
}
