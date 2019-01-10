package com.example.server;

public class User {
    private String id;
    private String name;
    private String ip;
    private int port;
    private long lastCheckIn;

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getIp() {
        return ip;
    }

    void setIp(String ip) {
        this.ip = ip;
    }

    int getPort() {
        return port;
    }

    void setPort(int port) {
        this.port = port;
    }

    long getLastCheckIn() {
        return lastCheckIn;
    }

    void setLastCheckIn(long lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", lastCheckIn=" + lastCheckIn +
                '}';
    }
}
