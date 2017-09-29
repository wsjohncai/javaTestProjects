package com.johncai;

public class OutputTest {
	public static void main(String[] args) {
		int type = 18;
		String name = "D:\\Sources\\practices\\java\\WP1\\LanFileTransfer\\src\\com\\wsjc";
		byte[] d1;
		byte[] t1;
		byte[] t2;

		t1 = Integer.toString(type).getBytes();
		t2 = name.getBytes();
		d1 = new byte[204];
		int i = 0;
		for (byte b : t1) {
			d1[i++] = b;
		}
		i = 4;
		for (byte b : t2) {
			d1[i++] = b;
		}
		int len1 = 0, len2 = 0;
		byte[] te1 = new byte[4];
		byte[] te2 = new byte[200];
		for (int j = 0; j < 4; j++) {
			byte b = d1[j];
			if (b != 0) {
				te1[len1++] = b;
			}
		}
		for (int j = 4; j < d1.length; j++) {
			byte b = d1[j];
			if (b != 0) {
				te2[len2++] = b;
			}
		}
		String a = new String(te1, 0, len1);
		String b = new String(te2, 0, len2);
		System.out.println("t1: " + new String(t1));
		System.out.println("t2: " + new String(t2));
		System.out.println("d1: " + new String(d1));
		System.out.println(a);
		System.out.println(b);
	}
}
