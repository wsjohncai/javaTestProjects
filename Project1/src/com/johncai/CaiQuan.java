package com.johncai;

/**
 * 
 * @author WSJohnCai
 * @function ��ȭ
 */

import java.util.Scanner;

public class CaiQuan {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		caiquan();
		sum();
	}

	// ��Ϸ����ͳ��
	static int gameTimes = 0;
	// ���ʤ������ͳ��
	static int winTimes = 0;
	// ����ʤ������
	static int cmpWins = 0;

	// ���в�ȭ
	public static void caiquan() {
		int a, b, c = 1;
		Scanner sc = new Scanner(System.in);
		while (c == 1) {
			System.out.print("��������ĳ�ȭ��0����ʯͷ��1���������2��������");
			do {
				a = sc.nextInt();
				if (a < 0 || a > 2) {
					System.out.print("�Բ��������������������룺");
				}
			} while (a < 0 || a > 2);
			b = (int) ((Math.random()) * 3);
			System.out.println("���Գ�ȭΪ��" + (b == 0 ? "ʯͷ" : (b == 1 ? "����" : "��")));
			caipan(a, b);
			gameTimes++;
			System.out.print("������Ϸ������1���˳�������0��");
			c = sc.nextInt();
		}
		sc.close();
	}

	// �ж���Ӯ
	public static void caipan(int a, int b) {
		if ((a - b == 2) || (a - b == -1)) {
			System.out.println("��Ӯ�ˣ�");
			winTimes++;
		} else if (a == b) {
			System.out.println("ƽ�֣�");
		} else {
			System.out.println("������~");
			cmpWins++;
		}
	}

	// �ܼ�¼
	public static void sum() {
		System.out.println("һ��������" + gameTimes + "����Ϸ��" + "������Ӯ��" + winTimes + "�֣�" + "����Ӯ��" + cmpWins + "�֡�");
	}
}
