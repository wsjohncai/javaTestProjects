package com.wsjohncai.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DesNode {

	private int attr;
	private int pheotype;
	private int desicion;
	private DesNode parent, siblings, child;
	
	public DesNode(int attr, int pheotype,DesNode parent) {
		super();
		this.attr = attr;
		this.pheotype = pheotype;
		this.parent = parent;
	}

	public int getDesicion() {
		return desicion;
	}

	public void setDecision(int desicion) {
		this.desicion = desicion;
	}

	public int getAttr() {
		return attr;
	}

	public void setAttr(int attr) {
		this.attr = attr;
	}

	public int getPheotype() {
		return pheotype;
	}

	public void setPheotype(int pheotype) {
		this.pheotype = pheotype;
	}

	public DesNode getParent() {
		return parent;
	}

	public DesNode getSiblings() {
		return siblings;
	}

	public void setSiblings(DesNode siblings) {
		this.siblings = siblings;
	}

	public DesNode getChild() {
		return child;
	}

	public void setChild(DesNode child) {
		this.child = child;
	}
	
}
