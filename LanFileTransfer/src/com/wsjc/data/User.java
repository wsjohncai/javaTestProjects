package com.wsjc.data;

public class User {
	private String addr;
	private String hostName;
	
	public User(String addr, String hostName) {
		this.addr = addr;
		this.hostName = hostName;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
}
