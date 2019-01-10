package com.example.client;

import com.example.misc.IPConverter;
import com.example.misc.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Scanner;

import static com.example.misc.Constants.*;

public class ClientMain {
    private static final int CHECKINTERVAL = 30;
    static User user;
    static DatagramSocket client;
    static boolean isClientRunning = false;
    static boolean isChatting = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入你的用户名：");
        user = new User();
        String name = sc.nextLine().trim();
        user.setName(name);
        int cmd = -1;
        ClientMain main = new ClientMain();
        main.init();
        while (cmd != 0) {
            System.out.println("1. Login");
            System.out.println("2. Refresh online users");
            System.out.println("3. Send message");
            System.out.println("0. Exit");
            cmd = sc.nextInt();
            sc.nextLine();
            switch (cmd) {
                case 1:
                    main.login();
                    break;
                case 2:
                    refreshUsers();
                    break;
                case 3:
                    main.sendMessage(sc);
                    break;
                case 0:
                    main.shutdown();
                    break;
                default:
            }
        }
        sc.nextLine();
    }

    //发送数据包
    private static void sendPacket(String info) {
        byte[] infob = info.getBytes();
        DatagramPacket p = new DatagramPacket(infob, infob.length);
        try {
            p.setAddress(InetAddress.getByAddress(IPConverter.getByIPv4Address(SERVERIP)));
            p.setPort(SERVERPORT);
            client.send(p);
        } catch (IOException e) {
            System.out.println("Send login info failed.");
            e.printStackTrace();
        }
    }

    //刷新在线用户
    static void refreshUsers() {
        String info = (ONLINE_USERS + SEP_CODE);
        sendPacket(info);
    }

    private void init() {
        new Thread(() -> {
            try {
                isClientRunning = true;
                client = new DatagramSocket();
                RequestHandler handler = RequestHandler.getInstance();
                while (true) {
                    DatagramPacket packet = new DatagramPacket(new byte[DEFAULT_BUFFER_SIZE], DEFAULT_BUFFER_SIZE);
                    client.receive(packet);
                    handler.queue(packet);
                }
            } catch (IOException e) {
                isClientRunning = false;
                if (e instanceof SocketException) {
                    client = null;
                    System.out.println("Client is shut down!");
                    return;
                }
                e.printStackTrace();
            }
        }).start();
    }

    private void shutdown() {
        sendPacket(LOGOUT + SEP_CODE + user.getId() + SEP_VALUE + user.getName());
        if (client != null) {
            System.out.println("Shutting down...");
            client.close();
        }
    }

    //用户登录操作
    private void login() {
        if (user.getId() == null || user.getId().isEmpty()) {
            String info = (LOGIN + ";;;###" + user.getName());
            sendPacket(info);
        } else {
            System.out.println("Already Login.");
        }
    }

    //发送信息
    private void sendMessage(Scanner sc) {
        System.out.println("请输入用户ID发起聊天：");
        String uid = sc.nextLine();
        if (uid.equals(user.getId())) {
            System.out.println("不能和自己聊天");
            return;
        }
        isChatting = true;
        User u = OnlineUsers.hasUser(uid);
        MsgHandler msgHandler = MsgHandler.getInstance();
        if (u != null) {
            System.out.println("请输入你要发送的信息，按Enter发送,输入quit退出");
            String omsg;
            omsg = sc.nextLine();
            while (!omsg.equals("quit") && isChatting) {
                Message msg = new Message();
                msg.setToId(uid);
                msg.setMessage(omsg);
                msg.setMsgId(user.getId() + Calendar.getInstance().getTimeInMillis());
                msgHandler.send(u, msg);
                System.out.println("正在跟用户 " + uid + "聊天: ");
                omsg = sc.nextLine();
            }
        } else {
            System.out.println("用户不存在！");
        }
        isChatting = false;
    }

    static class CheckOnlineTimer extends Thread {
        @Override
        public void run() {
            while (isClientRunning) {
                sendPacket(CHECKONLINE + SEP_CODE +
                        (user.getId() == null ? "" : user.getId()) + SEP_VALUE + user.getName());
                sendPacket(ONLINE_USERS + SEP_CODE);
                try {
                    Thread.sleep(CHECKINTERVAL * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
