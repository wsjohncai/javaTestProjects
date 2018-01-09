package com.wsjohncai.common;

public class PCB {
	
	public static final int RUNNING = 1;
	public static final int COMMITTED = 2;
	public static final int NOT_EXIST = 0;
	public static final int FINISHED = 4;
	
	private String name;
	private int summit_time;
	private int time_req_left;
	private int status;
	private int priority;
	private int start_time;
	private int time_req;
	private int stop_time;

	public PCB(String name, int summit_time, int time_req_left, int priority) {
		this.name = name;
		this.summit_time = summit_time;
		this.time_req_left = time_req_left;
		time_req = time_req_left;
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public int getSummit_time() {
		return summit_time;
	}

	public int getTime_req_left() {
		return time_req_left;
	}

	public void setTime_req_left(int time_req_left) {
		this.time_req_left = time_req_left;
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


	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

    public int getTime_req() {
        return time_req;
    }
}
