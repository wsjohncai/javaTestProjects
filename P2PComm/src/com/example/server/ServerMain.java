package com.example.server;

import com.example.misc.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;

import static com.example.misc.Constants.DEFAULT_BUFFER_SIZE;
import static com.example.misc.Constants.SERVERPORT;

public class ServerMain {

    static DatagramSocket server;
    static boolean isServerRunning = false;

    public static void main(String[] args) {
        int cmd = 0;
        Scanner sc = new Scanner(System.in);
        ServerMain main = new ServerMain();
        while (cmd != 4) {
            System.out.println("1. Start server");
            System.out.println("2. Shutdown server");
            System.out.println("3. All Online Users");
            System.out.println("4. exit");
            cmd = sc.nextInt();
            switch (cmd) {
                case 1:
                    main.start();
                    break;
                case 2:
                    main.shutdown();
                    break;
                case 3:
                    main.showAllUsers();
                    break;
                case 4:
                    main.shutdown();
                    break;
            }
        }
    }

    //关闭服务器
    private void shutdown() {
        if (server != null) {
            Log.info("Shutting down");
            server.close();
            isServerRunning = false;
        }
    }

    //显示所有在线用户
    private void showAllUsers(){
        System.out.println("当前在线用户：");
        System.out.println("----UserID----Username----Address----LastCheckIn");
        for (User u : UserManager.users) {
            System.out.println("  " + u.getId() + "   " + u.getName() + "   " +
            u.getIp() + ":" + u.getPort() + "   " + u.getLastCheckIn());
        }
        System.out.println();
    }

    //服务器开始监听
    private void start() {
        if (server != null) return;
        new Thread(() -> {
            try {
                Log.info("Server is starting, listening on port: " + SERVERPORT);
                server = new DatagramSocket(SERVERPORT);
                isServerRunning = true;
                RequestHandler handler = RequestHandler.getInstance();
                while (true) {
                    DatagramPacket packet = new DatagramPacket(new byte[DEFAULT_BUFFER_SIZE], DEFAULT_BUFFER_SIZE);
                    server.receive(packet);
                    handler.queue(packet);
                }
            } catch (IOException e) {
                isServerRunning = false;
                if (e instanceof SocketException) {
                    server = null;
                    Log.info("Server is shut down!");
                    return;
                }
                e.printStackTrace();
            }
        }).start();
    }

}
