package com.example;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {
    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println(addr.toString());
            System.out.println(addr.getHostName());
            System.out.println(addr.getHostAddress());
            System.out.println(addr.getCanonicalHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

//        List<Integer> list = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//            list.add(i);
//        }
//
//        StringBuilder listB = new StringBuilder();
//        for(int i = 0; i < list.size(); ){
//            if(list.get(i) == 3 || list.get(i)==4) {
//                list.remove(i);
//                continue;
//            }
//            listB.append(list.get(i)).append(";;;").append("sep");
//            i++;
//        }
//        System.out.println(listB.toString());

//        Scanner sc = new Scanner(System.in);
//        System.out.println("请输入：");
//        String t = "";
//        while(!t.equals("quit")){
//            t = sc.nextLine();
//            String finalT = t;
//            new Thread(() -> {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(finalT.getBytes().length);
//            }).start();
//        }
    }
}
