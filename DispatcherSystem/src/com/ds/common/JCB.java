package com.ds.common;

public class JCB {
    //状态常量
    public static final int COMMITTED = 0;
    public static final int RUNNING = 1;
    public static final int NOT_EXIST = -1;
    public static final int FINISHED = 2;

    private String name; //名称
    private int svc_time; //要求运行时间
    private int left_svc_time; //剩余运行时间
    private String summit_time; //提交时间
    private String start_time; //开始时间
    private String stop_time; //停止时间
    private int priority; //优先级
    private int status = NOT_EXIST; //状态变量

    public JCB(String name, int svc_time, String summit_time, int priority) {
        this.name = name;
        this.svc_time = svc_time;
        this.priority = priority;
        this.summit_time = summit_time;
        left_svc_time = svc_time;
    }

    public int getPriority() {
        return priority;
    }

    public int getLeft_svc_time() {
        return left_svc_time;
    }

    public void setLeft_svc_time(int left_svc_time) {
        this.left_svc_time = left_svc_time;
    }

    public String getSummit_time() {
        return summit_time;
    }

    public String getStop_time() {
        return stop_time;
    }

    public int getSvc_time() {
        return svc_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public int getStatus() {
        return status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
}
