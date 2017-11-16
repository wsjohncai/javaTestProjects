package com.johncai;

public class PointerTest {

	private int x,y;
	
	public static void main(String[] args) {
		int a,b;
		PointerTest test = new PointerTest();
		a=6;
		b=7;
		int num = test.count(a, b);
		System.out.println(a+" "+b+" "+num);
		test.getCount();
	}

	private int count(int a,int b) {
		a = 100; b = 100;
		return a+b;
	}
	
	private void getCount() {
		x=10;
		y=20;
		int sum = count(x,y);
		System.out.println("getCount: "+x+" "+y+" "+sum);
	}
	
}
