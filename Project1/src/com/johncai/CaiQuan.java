package com.johncai;

/**
 * 
 * @author WSJohnCai
 * @function 猜拳
 */

import java.util.Scanner;

public class CaiQuan {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		caiquan();
		sum();
	}

	// 游戏次数统计
	static int gameTimes = 0;
	// 玩家胜利次数统计
	static int winTimes = 0;
	// 电脑胜利次数
	static int cmpWins = 0;

	// 进行猜拳
	public static void caiquan() {
		int a, b, c = 1;
		Scanner sc = new Scanner(System.in);
		while (c == 1) {
			System.out.print("请输入你的出拳（0代表石头，1代表剪刀，2代表布）：");
			do {
				a = sc.nextInt();
				if (a < 0 || a > 2) {
					System.out.print("对不起，输入有误，请重新输入：");
				}
			} while (a < 0 || a > 2);
			b = (int) ((Math.random()) * 3);
			System.out.println("电脑出拳为：" + (b == 0 ? "石头" : (b == 1 ? "剪刀" : "布")));
			caipan(a, b);
			gameTimes++;
			System.out.print("继续游戏请输入1，退出请输入0：");
			c = sc.nextInt();
		}
		sc.close();
	}

	// 判断输赢
	public static void caipan(int a, int b) {
		if ((a - b == 2) || (a - b == -1)) {
			System.out.println("你赢了！");
			winTimes++;
		} else if (a == b) {
			System.out.println("平局！");
		} else {
			System.out.println("你输了~");
			cmpWins++;
		}
	}

	// 总记录
	public static void sum() {
		System.out.println("一共进行了" + gameTimes + "局游戏，" + "其中你赢了" + winTimes + "局，" + "电脑赢了" + cmpWins + "局。");
	}
}
