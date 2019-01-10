package com.example.server;

import com.example.misc.IPConverter;
import com.example.misc.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Vector;

import static com.example.misc.Constants.*;

public class RequestHandler extends Thread {
    private static RequestHandler handler;
    private Vector<DatagramPacket> packets = new Vector<>();
    private int counter = 1;

    private RequestHandler() {
    }

    static RequestHandler getInstance() {
        if (handler != null) return handler;
        synchronized (RequestHandler.class) {
            if (handler == null) {
                handler = new RequestHandler();
            }
        }
        if (!handler.isAlive())
            handler.start();
        return handler;
    }

    void queue(DatagramPacket p) {
        packets.add(p);
    }

    @Override
    public void run() {
        while (ServerMain.isServerRunning) {
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
            case LOGIN:
                handleLogin(parser, p);
                break;
            case CHECKONLINE:
                handleCheckOnline(parser, p);
                break;
            case COMM_REQUEST:
                handleCommReq(parser, p);
                break;
            case ONLINE_USERS:
                sendUserList(p);
                break;
            case LOGOUT:
                handleLogout(parser, p);
                break;
            default:
        }
    }

    //处理登录请求
    private void handleLogin(InfoParser parser, DatagramPacket p) {
        User u = parser.getSrcUser();
        if (u.getId() == null || u.getId().isEmpty())
            u.setId(counter++ + "");
        u.setIp(p.getAddress().getHostAddress());
        u.setPort(p.getPort());
        System.out.println("Received a LOGIN request from: " + u.getIp() + ":" + u.getPort() + ", assigned id: " + u.getId());
        if (UserManager.hasUser(u.getId()) == null)
            UserManager.addUser(u);
        //返回登录用户的用户ID
        byte[] idb = (LOGIN_FEEDBACK_ID + SEP_CODE + u.getId()).getBytes();
        try {
            DatagramPacket rp = new DatagramPacket(idb, idb.length, p.getSocketAddress());
            ServerMain.server.send(rp);
        } catch (IOException e) {
            Log.info(("Reply LOGIN request to " + p.getAddress().getHostAddress() + " failed."));
            e.printStackTrace();
        }
    }

    //发送用户列表
    private void sendUserList(DatagramPacket p) {
        String listdata = UserManager.userList();
        String list = ONLINE_USERS + SEP_CODE + listdata;
        byte[] bytes = list.getBytes();
        DatagramPacket rp = new DatagramPacket(bytes, bytes.length, p.getSocketAddress());
        try {
            ServerMain.server.send(rp);
        } catch (IOException e) {
            Log.info(("Send list to: " + p.getAddress().getHostAddress() + " failed."));
            e.printStackTrace();
        }
    }

    //处理在线
    private void handleCheckOnline(InfoParser parser, DatagramPacket p) {
//        Log.info(("Received a CHECKONLINE request from: " +
//                p.getAddress().getHostAddress() + ":" + p.getPort());
        User u = parser.getSrcUser();
        if (u.getId().isEmpty()) return;
        User u1 = UserManager.hasUser(u.getId());
        if (u1 != null) u1.setLastCheckIn(u.getLastCheckIn());
        else {
            u.setIp(p.getAddress().getHostAddress());
            u.setPort(p.getPort());
            UserManager.addUser(u);
        }
    }

    private void handleCommReq(InfoParser parser, DatagramPacket p) {
        String[] ids = parser.getMsg().split(SEP_VALUE);
        System.out.println("Received a Comm Request from: " + p.getAddress().getHostAddress() + ":" + p.getPort() +
                " from id: " + ids[0] + " to id: " + ids[1]);
        DatagramPacket sp;
        String srcIp = p.getAddress().getHostAddress();
        int srcPort = p.getPort();
        User dst = UserManager.hasUser(ids[1]);
        //如果用户已下线，那么服务器告知请求端
        if (dst == null) {
            byte[] rs = (COMM_USER_OFF + ";;;" + ids[1]).getBytes();
            sp = new DatagramPacket(rs, rs.length, p.getSocketAddress());
            try {
                ServerMain.server.send(sp);
            } catch (IOException e) {
                Log.info("Reply COMM_USER_OFF to " + p.getAddress().getHostAddress() + " failed.");
                e.printStackTrace();
            }
            return;
        }

        //如果目标用户在线，那么先向目标用户发送请求，然后再告知源用户目标用户地址
        byte[] dstMsg = (COMM_REQUEST + SEP_CODE + ids[0] + SEP_VALUE + srcIp + ":" + srcPort).getBytes();
        byte[] srcMsg = (COMM_REQ_FEEDBACK + SEP_CODE + ids[1] + SEP_VALUE +
                dst.getIp() + ":" + dst.getPort()).getBytes();
        try {
            //向目标用户发送nat穿透请求
            sp = new DatagramPacket(dstMsg, dstMsg.length);
            sp.setAddress(InetAddress.getByAddress(IPConverter.getByIPv4Address(dst.getIp())));
            sp.setPort(dst.getPort());
            ServerMain.server.send(sp);
            //向源用户发送目标用户信息
            sp = new DatagramPacket(srcMsg, srcMsg.length, p.getSocketAddress());
            ServerMain.server.send(sp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout(InfoParser parser, DatagramPacket p) {
        User u = parser.getSrcUser();
        Log.info(("User " + u.getId() + " logout."));
        UserManager.removeUser(u.getId());
    }
}
