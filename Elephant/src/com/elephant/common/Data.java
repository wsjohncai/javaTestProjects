package com.elephant.common;

import java.io.Serializable;

public class Data implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int REQUEST = 0;
	public static final int MESSAGE = 2;
	public static final int ANSWER = 1;
	public static final int SHUTDOWN = 3;
	public static final int SENDFILE_REQUEST = 4;
	public static final int RECEIVEFILE_CONFIRM = 5;
	public static final int INFOCHANGE = 6;
	public static final int MANUALINTERUPTED = 7;
	public static final int ENDCON = 8;
	private User user;
	private int dataType;
	private String text;

	public Data(int DataType, User u) {
		this.dataType = DataType;
		user = u;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public int getDataType() {
		return dataType;
	}

}
