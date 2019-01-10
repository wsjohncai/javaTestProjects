package com.example.client;

public class User {
    private String id;
    private String name;
    private String ip;
    private int port;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + "'" +
                ", name='" + name + "'" +
                ", ip='" + ip + "'" +
                ", port=" + port +
                "}";
    }
}
