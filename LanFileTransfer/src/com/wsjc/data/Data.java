package com.wsjc.data;

import java.io.Serializable;

public class Data implements Serializable {

	/**
	 * All the data uses this model
	 */
	private static final long serialVersionUID = 3479059303506741222L;
	public static final int REQUEST_CONNECT = 0;
	public static final int SEND_FILE = 1;
	public static final int RECEIVE_FILE = 2;
	public static final int REJECT_FILE = 3;
	public static final int ERROR = 4;
	public static final int LOG_OUT = 5;
	public static final int REQUEST_REPLY = 6;
	

	private int type; //用4字节储存
	private String data;
	
	/**
	 * 构造一个无需其他信息的数据包
	 * @param type
	 */
	public Data(int type) {
		this.type = type;
	}
	
	/**
	 * 构造一个需要填入信息的数据包
	 * @param type
	 * @param data It stores the info of a file. The format should be "file name;file length".\n
	 * Or the info of a user. The format should be "host-name"
	 */
	public Data(int type, String data) {
		this.type = type;
		this.data = data;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
