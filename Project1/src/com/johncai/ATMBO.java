package com.johncai;
public class ATMBO {
	// 当前账户帐号
	private String code = "1001";
	// 当前账户密码
	private int password = 111;
	// 当前账户金额
	private double money = 1000.0;

	/**
	 * 登录业务
	 *
	 * 1、用户输入的卡号和密码作为登录业务方法的两个参数， 由前面ATMUI类调用的时候传入
	 *
	 * 2、登录业务处理后，要“告知”ATMUI类结果，因此设一个 int 的返回值，如果卡号和密码正确，登录成功，则返回-1；
	 * 如果卡号和密码不正确，返回错误次数（超3次吞卡）。因此， 当前账户登录错误次数要作为全局变量“存储”起来
	 **/

	// 当前账户登录错误次数
	public int cs = 0;

	public int doLogin(String code_input, int password_input) {
		if (code_input.equals(code) && password_input == password) {
			return -1;
		} else {
			cs++;
			return cs;
		}
	}

	// 查询余额方法
	public double doChaxun() {
		return money;
	}

	// 定义取款方法
	public void doQukuan(int money) {
		this.money-=money;
	}

	public int getPassword() {
		return password;
	}

	// 定义存款方法
	public void doCunkuan(int money) {
		this.money+=money;
	}

	// 定义修改密码方法
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

	// 定义退出方法
	public void quit() {

	}
}