package com.wsjohncai.common;

public class JCB {
    //状态常量
    public static final int COMMITTED = 0;
    public static final int RUNNING = 1;
    public static final int NOT_EXIST = -1;
    public static final int FINISHED = 2;

    private String name;
    private int time_required;
    private int summit_time;
    private int start_time;
    private int stop_time;
    private double turnaround_time;
    private double weigh_turnaround_time;
    private int status = NOT_EXIST;

    public JCB(String name, int time_required, int summit_time) {
        this.name = name;
        this.time_required = time_required;
        this.summit_time = summit_time;
    }


    public int getSummit_time() {
        return summit_time;
    }

    public int getStop_time() {
        return stop_time;
    }

    public int getTime_required() {
        return time_required;
    }

    public void setStop_time(int stop_time) {
        this.stop_time = stop_time;
    }

    public int getStatus() {
        return status;
    }

    public void setTime_required(int time_required) {
        this.time_required = time_required;
    }

    public double getTurnaround_time() {
        return turnaround_time;
    }

    public void setTurnaround_time(double turnaround_time) {
        this.turnaround_time = turnaround_time;
    }

    public double getWeigh_turnaround_time() {
        return weigh_turnaround_time;
    }

    public void setWeigh_turnaround_time(double weigh_turnaround_time) {
        this.weigh_turnaround_time = weigh_turnaround_time;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
}
