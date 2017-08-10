package com.elephant.common;

import java.io.Serializable;
import javax.swing.ImageIcon;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String group;
	private String addr;
	private int port;
	private ImageIcon hicon;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String groupe) {
		this.group = groupe;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ImageIcon getHicon() {
		return hicon;
	}

	public void setHicon(ImageIcon hicon) {
		this.hicon = hicon;
	}

}
