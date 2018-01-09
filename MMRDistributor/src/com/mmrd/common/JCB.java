package com.mmrd.common;

public class JCB {

    public static final int NOT_STORE = -1;

	private String name;
	private int size;
	private int store_start_addr = NOT_STORE;
	
	public JCB(String name, int size) {
		this.name = name;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public int getStore_start_addr() {
		return store_start_addr;
	}

	public void setStore_start_addr(int store_start_addr) {
		this.store_start_addr = store_start_addr;
	}

}
