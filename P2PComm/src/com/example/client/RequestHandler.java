package com.example.client;

import com.example.misc.IPConverter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import static com.example.misc.Constants.*;

public class RequestHandler extends Thread {
    static RequestHandler handler;
    private Vector<DatagramPacket> packets = new Vector<>();

    private RequestHandler() {
    }

    static RequestHandler getInstance() {
        if (handler == null) {
            synchronized (RequestHandler.class) {
                if (handler == null) {
                    handler = new RequestHandler();
                }
            }
        }
        return handler;
    }

    void queue(DatagramPacket p) {
        if (!handler.isAlive())
            handler.start();
        packets.add(p);
    }

    @Override
    public void run() {
        while (ClientMain.isClientRunning) {
            if (!packets.isEmpty()) {
                handle(packets.firstElement());
                packets.remove(0);
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handle(DatagramPacket p) {
        String info = new String(p.getData(), 0, p.getLength());
        InfoParser parser = new InfoParser(info);
        switch (parser.getCode()) {
            case LOGIN_FEEDBACK_ID:
                handleLoginFB(parser);
                break;
            case ONLINE_USERS:
                handleUserLists(parser);
                break;
            case COMM_REQUEST:
                handleCommReq(parser);
                break;
            case COMM_REQ_FEEDBACK:
                handleCommReqFB(parser);
                break;
            case COMM_MSG:
                handleCommMsg(parser, p);
                break;
            case COMM_MSG_FEEDBACK:
                handleCommMsgReq(parser);
                break;
            case COMM_USER_OFF:
                handleCommUserOff(parser);
                break;
            default:
        }
    }

    //处理聊天时用户下线
    private void handleCommUserOff(InfoParser parser) {
        String id = parser.getDstId();
        ClientMain.isChatting = false;
        System.out.println("User " + id + " has logout");
        ClientMain.refreshUsers();
    }

    //处理服务器返回的登录返回信息
    private void handleLoginFB(InfoParser parser) {
        String id = parser.getMsg();
//        Log.info(("Get response ID from server: " + id);)
        ClientMain.user.setId(id);
        new ClientMain.CheckOnlineTimer().start();
    }

    //处理服务器返回的在线用户信息
    private void handleUserLists(InfoParser parser) {
        OnlineUsers.updateList(parser.getUserList());
        if (ClientMain.isChatting) return;
        System.out.println("当前在线用户：");
        System.out.println("----UserID--------Username----");
        for (User u : OnlineUsers.onlineUsers) {
            System.out.println("  " + u.getId() + "    " + u.getName());
        }
        System.out.println();
    }

    //处理服务器发送的nat探测请求，在未通信之前打开通道
    private void handleCommReq(InfoParser parser) {
        String addr = parser.getDstAddress();
        String ip = addr.split(":")[0];
        String port = addr.split(":")[1];
        //处理
        User u = OnlineUsers.hasUser(parser.getSrcId());
        if (u != null) {
            u.setIp(ip);
            u.setPort(Integer.parseInt(port));
        }
//        Log.info(("Sending a nat probe to " + addr);)
        byte[] toSend = (NATPROBE + SEP_CODE).getBytes();
        DatagramPacket p = new DatagramPacket(toSend, toSend.length);
        try {
            p.setAddress(InetAddress.getByAddress(IPConverter.getByIPv4Address(ip)));
            p.setPort(Integer.parseInt(port));
            ClientMain.client.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //接收服务器发送的收信方信息，然后更新收信方地址，发送消息
    private void handleCommReqFB(InfoParser parser) {
        String addr = parser.getDstAddress();
        String ip = addr.split(":")[0];
        String port = addr.split(":")[1];
//        System.out.println("Server reply comm request, to user: " + parser.getDstId() +
//                "(address: " + ip + ":" + port + ").");
        //处理
        User u = OnlineUsers.hasUser(parser.getDstId());
        if (u != null) {
            u.setIp(ip);
            u.setPort(Integer.parseInt(port));
            MsgHandler.getInstance().sendAll();
        }
    }

    //接收到聊天信息后进行处理
    private void handleCommMsg(InfoParser parser, DatagramPacket p) {
        String srcId = parser.getSrcId();
        String msgId = parser.getMsgId();
        String msg = parser.getCommMsg();
        byte[] rb = (COMM_MSG_FEEDBACK + SEP_CODE + msgId).getBytes();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        String pi = "用户 " + srcId + "   " + format.format(date) + ": \n  " + msg;
        System.out.println(pi);
        DatagramPacket sp = new DatagramPacket(rb, rb.length, p.getSocketAddress());
        try {
            ClientMain.client.send(sp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //接受到聊天信息的确认信息的处理
    private void handleCommMsgReq(InfoParser parser) {
        String msgId = parser.getMsg();
        MsgHandler.getInstance().deleteMsg(msgId);
    }
}
