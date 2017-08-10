package com.johncai;

import java.util.Calendar;
import java.util.Scanner;

/**
 * 
 * @author WSJohnCai function 用于数组和排序的练习
 */

public class ArrayTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayTest at = new ArrayTest();
		int[] array = new int[300000];
		at.creatArray(array);
		// at.printArray(array);
		Calendar cal = Calendar.getInstance();
		System.out.println(cal.getTime());
		// at.insertSort(array);
		// at.bubbleSort(array);
		// at.selectSort(array);
		at.quickSort(array, 0, at.len - 1);
		// at.printArray(array);
		cal = Calendar.getInstance();
		System.out.println(cal.getTime());
	}

	public int len = 0, range = 0;

	// 创建一个整型数组
	public void creatArray(int[] intarray) {
		int choice;
		len = 0;
		range = 0;
		System.out.print(
				"Choose how to creat an array:\n" + "1.Creat by input yourself   " + "2.System gives random numbers\n");
		Scanner sc = new Scanner(System.in);
		choice = sc.nextInt();
		while (true) {
			if (choice < 1 || choice > 2) {
				System.out.print("Input error, please input an Integer: ");
				choice = sc.nextInt();
			} else
				break;
		}
		switch (choice) {
		case 2:
			System.out.print("Please input the range and " + "the amouts of numbers in the array: ");
			range = sc.nextInt();
			len = sc.nextInt();
			for (int i = 0; i < len; i++) {
				intarray[i] = (int) (Math.random() * range + 1);
				// intarray[i]=len-i;
			}
			break;
		case 1:
			int i = 0;
			System.out.print("Please input your numbers, end with 'q': ");
			while (true) {
				String in = sc.next();
				if (in.equalsIgnoreCase("q"))
					break;
				else {
					intarray[i++] = Integer.parseInt(in);
					len++;
				}
			}
		}
		sc.close();
	}

	// 打印数组元素，每行15个元素
	public void printArray(int[] array) {
		for (int i = 0; i < len; i++) {
			System.out.print(array[i] + " ");
			if ((i + 1) % 15 == 0)
				System.out.println("");
		}
		System.out.println(" ");
	}

	// 泡泡排序法
	public void bubbleSort(int[] array) {
		for (int i = 0, j; i < len - 1; i++) {
			for (j = i + 1; j < len; j++) {
				if (array[i] > array[j]) {
					int temp;
					temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
	}

	// 选择排序法
	public void selectSort(int[] array) {
		for (int i = 0, j, k; i < len - 1; i++) {
			k = i;
			for (j = i + 1; j < len; j++) {
				if (array[j] < array[k])
					k = j;
			}
			if (k != i) {
				int temp;
				temp = array[k];
				array[k] = array[i];
				array[i] = temp;
			}
		}
	}

	// 插入排序法
	public void insertSort(int[] array) {
		for (int i = 1, insert, insertVal; i < len; i++) {
			insert = i - 1;
			insertVal = array[i];
			while (insert > 0 && (array[insert] > insertVal)) {
				array[insert + 1] = array[insert];
				insert--;
			}
			array[insert + 1] = insertVal;
		}
	}

	public void quickSort(int[] array, int left, int right) {

		if (left < right) {
			// 将基准值赋予pivot；并分别将左右游标赋初值
			int privot = array[left];
			int i = left, j = right;

			// 当左右游标不相等，进行分组
			while (i < j) {
				while (j > i && array[j] > privot)
					j--;

				if (i < j)
					array[i++] = array[j];

				while (i < j && array[i] < privot)
					i++;

				if (i < j)
					array[j--] = array[i];
			}
			array[i] = privot;
			// printArray(array);

			quickSort(array, left, i - 1);
			quickSort(array, i + 1, right);
		}
	}

}
