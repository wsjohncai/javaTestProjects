package com.example.misc;

public class IPConverter {
    public static byte[] getByIPv4Address(String ip) {
        if (ip.matches("((25[0-5]|2[0-4][0-9]|" +
                "1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}" +
                "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])")) {
            String[] sp = ip.split("\\.");
            byte[] rs = new byte[4];
            rs[0] = (byte) Integer.parseInt(sp[0]);
            rs[1] = (byte) Integer.parseInt(sp[1]);
            rs[2] = (byte) Integer.parseInt(sp[2]);
            rs[3] = (byte) Integer.parseInt(sp[3]);
            return rs;
        }
        throw new IllegalArgumentException();
    }
}
