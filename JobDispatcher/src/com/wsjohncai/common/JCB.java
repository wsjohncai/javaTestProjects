package com.wsjohncai.common;

public class JCB {
	
	public static final int RUNNING = 1;
	public static final int COMMITTED = 2;
	public static final int NOT_EXIST = 0;
	public static final int FINISHED = 4;
	
	private String name;
	private int summit_time;
	private int time_required;
	private int status;
	private int start_time;
	private int stop_time;
	private int turnaround_time;
	private double weigh_turnaround_time;
	
	public JCB(String name, int summit_time,int time_required) {
		this.name = name;
		this.summit_time = summit_time;
		this.time_required = time_required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSummit_time() {
		return summit_time;
	}

	public void setSummit_time(int summit_time) {
		this.summit_time = summit_time;
	}

	public int getTime_required() {
		return time_required;
	}

	public void setTime_required(int time_required) {
		this.time_required = time_required;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStart_time() {
		return start_time;
	}

	public void setStart_time(int start_time) {
		this.start_time = start_time;
	}

	public int getStop_time() {
		return stop_time;
	}

	public void setStop_time(int total_run_time) {
		this.stop_time = total_run_time;
	}

	public int getTurnaround_time() {
		return turnaround_time;
	}

	public void setTurnaround_time(int turnaround_time) {
		this.turnaround_time = turnaround_time;
	}

	public double getWeigh_turnaround_time() {
		return weigh_turnaround_time;
	}

	public void setWeigh_turnaround_time(double d) {
		this.weigh_turnaround_time = d;
	}
	
	
}
