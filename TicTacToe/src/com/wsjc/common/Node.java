package com.wsjc.common;

public class Node {
	private Node parent;
	private int value;
	private int type;
	private int poi;
	private boolean isModified = false;
	
	public Node(Node parent, int type, int poi) {
		this.parent = parent;
		this.type = type;
		this.poi = poi;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getPoi() {
		return poi;
	}
	
	public void setPoi(int poi) {
		this.poi = poi;
	}
	
	public int getValue() {
		return value;
	}

	public Node getParent() {
		return parent;
	}
	
	public int getType() {
		return type;
	}

	public boolean isModified() {
		return isModified;
	}

	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}
	
}
