package com.johncai;

/**
 * 
 * @author WSJohnCai
 * @function 输出输入
 */

import java.io.*;
import java.util.Scanner;

public class Output {
	
	Scanner sc = null;
	
	// 获得数字
	public int getInt() {
		int a = 0;
		String s;
		System.out.print("Please input an integer: ");
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			s = bf.readLine();
			a = Integer.parseInt(s);
		} catch (IOException e) {
		}
		return a;
	}

	public String getString() {
		String s = null;
		System.out.print("Please input a string: ");
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			s = bf.readLine();
		} catch (IOException e) {
		}
		return s;
	}
	
	public float getFloat() {
		float a=0;
		System.out.print("Please input a decimal: ");
		sc = new Scanner(System.in);
		a=sc.nextFloat();
		sc.close();
		return a;
	}
	
	public double getDouble() {
		double a = 0;
		System.out.print("Please input a decimal: ");
		sc = new Scanner(System.in);
		a = sc.nextDouble();
		sc.close();
		return a;
	}
	
	public long getLong() {
		long a = 0;
		System.out.print("Please input a number: ");
		sc = new Scanner(System.in);
		a = sc.nextLong();
		sc.close();
		return a;
	}

	/**
	 * 输出方法 public void pt(int n) { int i, j; for (i = 1; i <= n; i++) { for (j
	 * = 1; j <= i; j++) { System.out.print(j + "ｘ" + i + "=" + j * i + " "); }
	 * System.out.println(" "); } }
	 */

}
