package com.johncai;
public class ATMBO {
	// ��ǰ�˻��ʺ�
	private String code = "1001";
	// ��ǰ�˻�����
	private int password = 111;
	// ��ǰ�˻����
	private double money = 1000.0;

	/**
	 * ��¼ҵ��
	 *
	 * 1���û�����Ŀ��ź�������Ϊ��¼ҵ�񷽷������������� ��ǰ��ATMUI����õ�ʱ����
	 *
	 * 2����¼ҵ�����Ҫ����֪��ATMUI�����������һ�� int �ķ���ֵ��������ź�������ȷ����¼�ɹ����򷵻�-1��
	 * ������ź����벻��ȷ�����ش����������3���̿�������ˣ� ��ǰ�˻���¼�������Ҫ��Ϊȫ�ֱ������洢������
	 **/

	// ��ǰ�˻���¼�������
	public int cs = 0;

	public int doLogin(String code_input, int password_input) {
		if (code_input.equals(code) && password_input == password) {
			return -1;
		} else {
			cs++;
			return cs;
		}
	}

	// ��ѯ����
	public double doChaxun() {
		return money;
	}

	// ����ȡ���
	public void doQukuan(int money) {
		this.money-=money;
	}

	public int getPassword() {
		return password;
	}

	// �������
	public void doCunkuan(int money) {
		this.money+=money;
	}

	// �����޸����뷽��
	public int doXiugai(int oldin,int newin,int newcin) {
		if(oldin!=password) {
			return -1;
		}else if(newin!=newcin) {
			return -2;
		} else {
			password=newcin;
			return 0;
		}
	}

	// �����˳�����
	public void quit() {

	}
}