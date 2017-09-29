package com.johncai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATMUI {
	// 主方法
	public static void main(String[] args) {
		ATMUI ui = new ATMUI();
		// 初始化业务对象
		ui.initBO();
		// 初始化主窗口
		ui.initFrame();
		// 现实登录界面
		ui.showLogin();
	}

	/**
	 * 初始化业务对象
	 *
	 * 业务对象，每一个用户对应一个新的业务对象并保存该用户数据 因此，启动程序和每次退出都要创建一个新的业务对象给下一个 用户使用
	 */
	public ATMBO bo = null;

	public void initBO() {
		bo = new ATMBO();
	}

	/**
	 * 初始化主窗口
	 */
	// 界面宽与高
	public int width = 960;
	public int height = 720;

	// 界面窗口
	public JFrame jFrame = null;
	// 层叠容器
	public JLayeredPane layeredPane = null;

	// 背景层
	public JPanel backLayer = null;
	// 前景层
	public JPanel frontLayer = null;
	// 前景层布局器
	public CardLayout cardLayout = null;

	public void initFrame() {
		// --初始化窗口与层叠容器
		// 创建窗口对象，窗口标题为“ATM触摸系统”
		jFrame = new JFrame("ATM触摸系统");

		// 创建容器对象
		layeredPane = new JLayeredPane();
		// 设置层叠容器大小
		layeredPane.setPreferredSize(new Dimension(width, height));

		// 把层叠容器添加到窗口中
		jFrame.add(layeredPane);
		// 设置窗口不能放大缩小
		jFrame.setResizable(false);
		// 设置窗口大小适应内容大小
		jFrame.pack();
		// 设置窗口可见（默认不可见）
		jFrame.setVisible(true);
		// 设置窗口关闭时，程序关闭
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// --在层叠容器中添加背景层
		// 创建背景层对象
		backLayer = new JPanel();
		// 设置背景层布局器FlowLayout的水平间距为0（默认为5）
		((FlowLayout) backLayer.getLayout()).setHgap(0);
		// 设置背景层布局器FlowLayout的垂直间距为0（默认为5）
		((FlowLayout) backLayer.getLayout()).setVgap(0);
		// 设置背景层大小（同窗口）
		backLayer.setSize(width, height);
		// 设置背景层位置（相对于窗口左上角）
		backLayer.setLocation(0, 0);

		// 创建背景图像
		JLabel bg = new JLabel(new ImageIcon("images/bg.jpg"));
		// 在背景层中添加背景图
		backLayer.add(bg);

		// 吧背景层添加到层叠容器的较低层
		layeredPane.add(backLayer, new Integer(0));

		// --在层叠容器中增加前景层
		frontLayer = new JPanel();
		// 创建CardLayout布局器对象，水平、垂直间距为0
		cardLayout = new CardLayout(0, 0);
		// 将前景层的布局器设置为CardLayout
		frontLayer.setLayout(cardLayout);
		// 把前景层色设置成透明（才能看到背景层）
		frontLayer.setOpaque(false);
		// 设置前景层大小（同窗口）
		frontLayer.setSize(width, height);
		// 设置前景层位置（相对于窗口左上角）
		frontLayer.setLocation(0, 0);

		// 把前景层添加到层叠容器的较顶层
		layeredPane.add(frontLayer, new Integer(1));
	}

	/**
	 * 登录界面 CardLayout作用是将各个创建的界面分层，在调用相应层时 把对应层调到最顶层即可；另外，每个层面第一次调用时，
	 * 则初始化，后面的调用就可以直接把已初始化的层面调出并 把一些必要组建重置即可。
	 */
	// 登录层容器
	public JPanel loginPane = null;
	// 登录卡号输入框
	public JTextField loginCodeInput = null;
	// 登录密码提示框
	public JPasswordField loginPassInput = null;
	// 登录提示信息
	public JLabel loginTipsLabel = null;

	public void showLogin() {
		if (loginPane == null) {
			// --登陆层未初始化时

			// 创建登录层容器对象
			loginPane = new JPanel();
			// 把登录层的背景颜色设成透明
			loginPane.setOpaque(false);
			// --往登录层中添加组件
			// 创建一个垂直Box容器
			Box loginBox = Box.createVerticalBox();
			// 在垂直容器中添加180高度的距离
			loginBox.add(Box.createVerticalStrut(180));

			// 创建一个欢迎信息容器
			JPanel welcome_panel = new JPanel();
			// 把欢迎信息容器设置成背景色透明
			welcome_panel.setOpaque(false);
			// 创建欢迎信息“欢迎使用海阁银行”
			JLabel welcome_label = new JLabel("欢迎使用海阁银行");
			// 设置信息字体
			welcome_label.setForeground(Color.WHITE);
			welcome_label.setFont(new Font("微软雅黑", Font.PLAIN, 30));
			// 把欢迎信息添加到欢迎容器
			welcome_panel.add(welcome_label);
			// 把欢迎信息容器添加到垂直Box容器
			loginBox.add(welcome_panel);
			// 在垂直Box中添加30高度的距离
			loginBox.add(Box.createVerticalStrut(30));

			// 创建一个卡号输入容器
			JPanel code_panel = new JPanel();
			// 把卡号输入容器背景设成透明
			code_panel.setOpaque(false);
			// 创建提示“请输入卡号”
			JLabel code_label = new JLabel("请输入卡号：");
			// 设置信息字体
			code_label.setForeground(Color.WHITE);
			code_label.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			// 把提示输入卡号信息添加到卡号输入容器
			code_panel.add(code_label);
			// 创建卡号输入框
			loginCodeInput = new JTextField(10);
			// 设置卡号输入框字体
			loginCodeInput.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			// 把卡号输入框添加到卡号输入容器
			code_panel.add(loginCodeInput);
			// 把卡号输入容器添加到垂直Box
			loginBox.add(code_panel);

			// 增加20高度垂直Box距离
			loginBox.add(Box.createVerticalStrut(20));

			// 创建密码输入容器
			JPanel pass_panel = new JPanel();
			// 讲密码输入容器设成透明
			pass_panel.setOpaque(false);
			// 创建提示文本
			JLabel pass_label = new JLabel("请输入密码：");
			// 设置提示信息字体
			pass_label.setForeground(Color.WHITE);
			pass_label.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			// 将密码提示信息放入密码输入框
			pass_panel.add(pass_label);
			// 创建密码输入框
			loginPassInput = new JPasswordField(10);
			// 设置密码输入框字体
			loginPassInput.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			// 将密码输入框放入密码输入容器
			pass_panel.add(loginPassInput);
			// 把密码输入容器放入垂直Box容器
			loginBox.add(pass_panel);

			// 在垂直Box容器添加30高度的距离
			loginBox.add(Box.createVerticalStrut(30));

			// 创建按钮容器
			JPanel btn_panel = new JPanel();
			// 把按钮背景设置成透明
			btn_panel.setOpaque(false);
			// 创建登录按钮并设置字体
			JButton login_btn = new JButton("登 录");
			login_btn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			// 把按钮添加到按钮容器
			btn_panel.add(login_btn);
			// 创建重置按钮并设置字体
			JButton reset_btn = new JButton("重置");
			reset_btn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			// 把重置按钮添加到按钮容器
			btn_panel.add(reset_btn);
			// 把按钮容器添加到垂直Box容器
			loginBox.add(btn_panel);

			// 在垂直Box中添加10高度的距离
			loginBox.add(Box.createVerticalStrut(10));

			// 创建登录提示信息容器
			JPanel tips_panel = new JPanel();
			// 把登录提示信息容器的背景设置成透明
			tips_panel.setOpaque(false);
			// 创建登录提示信息对象并设置字体
			loginTipsLabel = new JLabel(" ");
			loginTipsLabel.setForeground(new Color(238, 32, 32));
			loginTipsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			// 把登录提示信息添加到提示容器框
			tips_panel.add(loginTipsLabel);
			// 把提示信息容器添加到垂直Box容器
			loginBox.add(tips_panel);

			// 把垂直Box容器添加到登录层容器中
			loginPane.add(loginBox);

			// --显示登录层
			// 把登录层添加到前景层中
			// frontLayer.add("loginPane",loginPane);
			frontLayer.add(loginPane, "loginPane");
			// 使登录层在前景层容器中置于顶层显示
			cardLayout.show(frontLayer, "loginPane");
			// 刷新前景层使其可视化
			frontLayer.validate();
			// 使卡号输入框获取焦点
			loginCodeInput.requestFocus();

			// --监听重置按钮
			reset_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					// 清空卡号输入框、密码输入框和提示信息
					loginCodeInput.setText("");
					loginPassInput.setText("");
					loginTipsLabel.setText("");
					// 使卡号输入框获取焦点
					loginCodeInput.requestFocus();
				}
			});

			// --监听登录按钮事件
			login_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					// 获取用户输入的卡号和密码
					String code_str = loginCodeInput.getText();
					String pass_str = new String(loginPassInput.getPassword());
					if ("".equals(code_str)) {
						loginTipsLabel.setText("卡号不能为空！");
						loginCodeInput.requestFocus();
					} else if ("".equals(pass_str)) {
						loginTipsLabel.setText("密码不能为空！");
						loginPassInput.requestFocus();
					} else {
						// 若卡号和密码都不为空，调用业务对象的登录业务方法并返回结果
						int login_rtn = bo.doLogin(code_str, Integer.valueOf(pass_str));
						if (login_rtn == -1) {
							// 返回-1，表示登录成功，显示主菜单界面（未实现）
							showMenu();
						} else if (login_rtn == 3) {
							// 返回3，表示错误次数已超过3次，显示吞卡提示界面（未实现）
							showTunka();
						} else {
							loginCodeInput.setText("");
							loginPassInput.setText("");
							loginTipsLabel.setText("卡号或密码错误，请重新输入，您还有" + (3 - login_rtn) + "次机会！");
							loginCodeInput.requestFocus();
						}
					}
				}
			});
		} else {
			// --登录层已初始化
			// --登录层在前景层容器中置于顶层
			cardLayout.show(frontLayer, "loginPane");
			// 重置
			loginCodeInput.setText("");
			loginPassInput.setText("");
			loginTipsLabel.setText("");
			loginCodeInput.requestFocus();
		}
	}

	public JPanel tunkaPane = null;

	// 吞卡提示界面
	public void showTunka() {
		if (tunkaPane == null) {
			// 吞卡界面未初始化使
			tunkaPane = new JPanel();
			tunkaPane.setOpaque(false);

			// 往吞卡提示界面层容器添加组件
			Box tunkaBox = Box.createVerticalBox();

			tunkaBox.add(Box.createVerticalStrut(180));

			JPanel tunka_panel = new JPanel();
			tunka_panel.setOpaque(false);
			JLabel tunka_label = new JLabel("您已超过3次输入机会，系统吞卡，请联系客服！");
			tunka_label.setForeground(Color.WHITE);
			tunka_label.setFont(new Font("微软雅黑", Font.PLAIN, 30));
			tunka_panel.add(tunka_label);
			tunkaBox.add(tunka_panel);

			tunkaBox.add(Box.createVerticalStrut(30));

			JPanel btn_panel = new JPanel();
			btn_panel.setOpaque(false);
			JButton tunka_btn = new JButton("确 定");
			tunka_btn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			btn_panel.add(tunka_btn);
			tunkaBox.add(btn_panel);

			tunkaPane.add(tunkaBox);

			// 显示吞卡提示界面
			frontLayer.add(tunkaPane, "tunkaPane");
			cardLayout.show(frontLayer, "tunkaPane");
			frontLayer.validate();

			// 坚挺确定按钮事件
			tunka_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					// 退出
					quit();
				}
			});
		} else {
			// --吞卡提示界面层已初始化
			cardLayout.show(frontLayer, "tunkaPane");
		}
	}

	// 主菜单界面

	// 主菜单界面容器
	public JPanel menuPane = null;

	public void showMenu() {
		if (menuPane == null) {
			// 主菜单界面未初始化时
			menuPane = new JPanel();
			menuPane.setOpaque(false);
			// 将主菜单界面层设置为BorderLayout
			menuPane.setLayout(new BorderLayout());

			// 往主菜单界面层容器添加组件
			// 头部信息提示块，位于BorderLayout北区
			Box tipsBox = Box.createVerticalBox();
			menuPane.add(tipsBox, BorderLayout.NORTH);

			tipsBox.add(Box.createVerticalStrut(150));

			JLabel tips_label = new JLabel("请选择您需要的服务");
			tips_label.setForeground(Color.WHITE);
			tips_label.setFont(new Font("微软雅黑", Font.PLAIN, 30));
			tips_label.setAlignmentX(Component.CENTER_ALIGNMENT);
			tipsBox.add(tips_label);

			// 左栏按钮块，位于BorderLayout西区
			Box menuLeft = Box.createVerticalBox();
			menuPane.add(menuLeft, BorderLayout.WEST);

			menuLeft.add(Box.createVerticalStrut(50));

			JButton chaxun_btn = new JButton("查询余额");
			chaxun_btn.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			menuLeft.add(chaxun_btn);
			menuLeft.add(Box.createVerticalStrut(100));

			JButton cunkuan_btn = new JButton("存  款");
			cunkuan_btn.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			menuLeft.add(cunkuan_btn);
			menuLeft.add(Box.createVerticalStrut(100));

			JButton qukuan_btn = new JButton("取  款");
			qukuan_btn.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			menuLeft.add(qukuan_btn);

			// 右侧按钮栏，位于BorderLayout东区
			Box menuRight = Box.createVerticalBox();
			menuPane.add(menuRight, BorderLayout.EAST);

			menuRight.add(Box.createVerticalStrut(50));
			menuRight.setAlignmentX(Component.RIGHT_ALIGNMENT);

			JButton xiugai_btn = new JButton("修  改");
			xiugai_btn.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			menuRight.add(xiugai_btn);

			menuRight.add(Box.createVerticalStrut(100));

			JButton quit_btn = new JButton("退  出");
			quit_btn.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			menuRight.add(quit_btn);

			// 显示主菜单界面
			frontLayer.add(menuPane, "menuPane");
			cardLayout.show(frontLayer, "menuPane");
			frontLayer.validate();

			// 监听按钮事件
			chaxun_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showChaxun();
				}
			});

			cunkuan_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showCunkuan();
				}
			});

			qukuan_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showQukuan();
				}
			});

			xiugai_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showXiugai();
				}
			});

			quit_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					quit();
				}
			});
		} else {
			// 主菜单已初始化
			cardLayout.show(frontLayer, "menuPane");
		}
	}

	// 查询界面
	public JPanel showyuePane = null;
	JLabel yue_label = null;

	public void showChaxun() {
		// 查询余额界面未初始化
		if (showyuePane == null) {
			// 创建余额层和组件容器
			showyuePane = new JPanel();
			showyuePane.setOpaque(false);
			Box showyueBox = Box.createVerticalBox();
			showyuePane.add(showyueBox);

			showyueBox.add(Box.createVerticalStrut(150));

			// 创建文字行
			yue_label = new JLabel();
			yue_label.setForeground(Color.white);
			yue_label.setFont(new Font("微软雅黑", Font.PLAIN, 30));
			yue_label.setAlignmentX(Component.CENTER_ALIGNMENT);
			yue_label.setText("您的账户余额为：" + bo.doChaxun() + "元");
			showyueBox.add(yue_label);
			showyueBox.add(Box.createVerticalStrut(50));

			// 创建返回按钮
			JButton fanhui_btn = new JButton("返回");
			fanhui_btn.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			fanhui_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			showyueBox.add(fanhui_btn);

			// 显示余额
			frontLayer.add(showyuePane, "showyuePane");
			cardLayout.show(frontLayer, "showyuePane");
			frontLayer.validate();

			// 监听返回按钮
			fanhui_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});
		} else {
			// 显示余额的界面已经初始化
			yue_label.setText("您的账户余额为：" + bo.doChaxun() + "元");
			cardLayout.show(frontLayer, "showyuePane");
		}
	}

	// 存款界面
	public JTextField ck_textfield = null;
	public JPanel ckPane = null;
	public JLabel ckTips = null;

	public void showCunkuan() {
		// 系统未初始化
		if (ckPane == null) {
			// 创建存款页面层和垂直Box容器
			ckPane = new JPanel();
			ckPane.setOpaque(false);
			Box ckBox = Box.createVerticalBox();
			ckPane.add(ckBox);
			ckBox.add(Box.createVerticalStrut(150));

			// 创建输入行容器和组件
			JPanel ckinPane = new JPanel();
			ckinPane.setOpaque(false);
			JLabel ck_label = new JLabel("请输入存款金额：");
			ck_label.setForeground(Color.white);
			ck_label.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			ck_textfield = new JTextField(10);
			ck_textfield.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			ckinPane.add(ck_label);
			ckinPane.add(ck_textfield);
			ckBox.add(ckinPane);
			ckBox.add(Box.createVerticalStrut(30));

			// 创建按钮容器和按钮
			JButton ck_yes = new JButton("确定");
			JButton ck_cancel = new JButton("返回");
			JPanel ck_button_pane = new JPanel();
			ck_button_pane.setOpaque(false);
			ck_yes.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			ck_cancel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			ck_button_pane.add(ck_yes);
			ck_button_pane.add(ck_cancel);
			ckBox.add(ck_button_pane);
			ckBox.add(Box.createVerticalStrut(30));

			// 创建提示信息
			ckTips = new JLabel(" ");
			ckTips.setForeground(Color.red);
			ckTips.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			ckTips.setAlignmentX(Component.CENTER_ALIGNMENT);
			ckBox.add(ckTips);

			// 监听确认按钮事件
			ck_yes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String ckin = ck_textfield.getText();
					int ckmoney = 0;
					// 如果输入为空，提示信息
					if (ckin.equals("")) {
						ckTips.setText("输入不能为空！");
						ck_textfield.requestFocus();
					} else if (((ckmoney = Integer.parseInt(ckin)) % 100 != 0) || (ckmoney == 0)) {
						// 如果输入不为整百
						ckTips.setText("请存入整百金额！");
						ck_textfield.setText("");
						ck_textfield.requestFocus();
					} else {
						// 输入正确,调用函数
						ck_textfield.setText("");
						bo.doCunkuan(ckmoney);
						showCunkuanSuccess();
					}
				}
			});

			// 监听返回按钮事件
			ck_cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});

			ckPane.add(ckBox);
			// 显示存款界面
			frontLayer.add(ckPane, "ckPane");
			cardLayout.show(frontLayer, "ckPane");
			frontLayer.validate();
			ck_textfield.requestFocus();
		} else {
			// 已初始化
			ck_textfield.setText("");
			ckTips.setText(" ");
			cardLayout.show(frontLayer, "ckPane");
			ck_textfield.requestFocus();
		}
	}

	// 存款成功提示界面
	public JPanel cksPane = null;

	public void showCunkuanSuccess() {
		// 界面未初始化
		if (cksPane == null) {
			// 创建层和组件容器
			cksPane = new JPanel();
			cksPane.setOpaque(false);
			Box cksBox = Box.createVerticalBox();
			cksPane.add(cksBox);

			cksBox.add(Box.createVerticalStrut(150));

			// 创建文字行
			JLabel cks_Lable = new JLabel();
			cks_Lable.setForeground(Color.white);
			cks_Lable.setFont(new Font("微软雅黑", Font.PLAIN, 30));
			cks_Lable.setAlignmentX(Component.CENTER_ALIGNMENT);
			cks_Lable.setText("存款成功！");
			cksBox.add(cks_Lable);
			cksBox.add(Box.createVerticalStrut(50));

			// 创建返回按钮
			JButton fanhui_btn = new JButton("返回");
			fanhui_btn.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			fanhui_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			cksBox.add(fanhui_btn);

			// 显示信息
			frontLayer.add(cksPane, "cksPane");
			cardLayout.show(frontLayer, "cksPane");
			frontLayer.validate();

			// 监听返回按钮
			fanhui_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});
		} else {
			// 显示界面已经初始化
			cardLayout.show(frontLayer, "cksPane");
		}
	}

	// 取款方法实现
	public JTextField qk_textfield = null;
	public JPanel qkPane = null;
	public JLabel qkTips = null;

	public void showQukuan() {
		// 系统未初始化
		if (qkPane == null) {
			// 创建存款页面层和垂直Box容器
			qkPane = new JPanel();
			qkPane.setOpaque(false);
			Box qkBox = Box.createVerticalBox();
			qkPane.add(qkBox);
			qkBox.add(Box.createVerticalStrut(150));

			// 创建输入行容器和组件
			JPanel qkinPane = new JPanel();
			qkinPane.setOpaque(false);
			JLabel qk_label = new JLabel("请输入取款金额：");
			qk_label.setForeground(Color.white);
			qk_label.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			qk_textfield = new JTextField(10);
			qk_textfield.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			qkinPane.add(qk_label);
			qkinPane.add(qk_textfield);
			qkBox.add(qkinPane);
			qkBox.add(Box.createVerticalStrut(30));

			// 创建按钮容器和按钮
			JButton qk_yes = new JButton("确定");
			JButton qk_cancel = new JButton("返回");
			JPanel qk_button_pane = new JPanel();
			qk_button_pane.setOpaque(false);
			qk_yes.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			qk_cancel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			qk_button_pane.add(qk_yes);
			qk_button_pane.add(qk_cancel);
			qkBox.add(qk_button_pane);
			qkBox.add(Box.createVerticalStrut(30));

			// 创建提示信息
			qkTips = new JLabel(" ");
			qkTips.setForeground(Color.red);
			qkTips.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			qkTips.setAlignmentX(Component.CENTER_ALIGNMENT);
			qkBox.add(qkTips);

			// 监听确认按钮事件
			qk_yes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String qkin = qk_textfield.getText();
					int qkmoney = 0;
					// 如果输入为空，提示信息
					if (qkin.equals("")) {
						qkTips.setText("输入不能为空！");
						qk_textfield.requestFocus();
					} else if (((qkmoney = Integer.parseInt(qkin)) % 100 != 0) || (qkmoney == 0)) {
						// 如果输入不为整百
						qkTips.setText("请输入整百金额！");
						qk_textfield.setText("");
						qk_textfield.requestFocus();
					} else if (qkmoney > bo.doChaxun()) {
						qk_textfield.setText("");
						qkTips.setText("您的余额不足！");
						qk_textfield.requestFocus();
					} else {
						// 输入正确,调用函数
						qk_textfield.setText("");
						bo.doQukuan(qkmoney);
						showQukuanSuccess();
					}
				}
			});

			// 监听返回按钮事件
			qk_cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});

			qkPane.add(qkBox);
			// 显示存款界面
			frontLayer.add(qkPane, "qkPane");
			cardLayout.show(frontLayer, "qkPane");
			frontLayer.validate();
			qk_textfield.requestFocus();
		} else {
			// 已初始化
			qk_textfield.setText("");
			qkTips.setText(" ");
			cardLayout.show(frontLayer, "qkPane");
			qk_textfield.requestFocus();
		}
	}

	// 取款成功界面
	JPanel qksPane = null;

	public void showQukuanSuccess() {
		// 界面未初始化
		if (qksPane == null) {
			// 创建层和组件容器
			qksPane = new JPanel();
			qksPane.setOpaque(false);
			Box qksBox = Box.createVerticalBox();
			qksPane.add(qksBox);

			qksBox.add(Box.createVerticalStrut(150));

			// 创建文字行
			JLabel qks_Lable = new JLabel();
			qks_Lable.setForeground(Color.white);
			qks_Lable.setFont(new Font("微软雅黑", Font.PLAIN, 30));
			qks_Lable.setAlignmentX(Component.CENTER_ALIGNMENT);
			qks_Lable.setText("取款成功！");
			qksBox.add(qks_Lable);
			qksBox.add(Box.createVerticalStrut(50));

			// 创建返回按钮
			JButton fanhui_btn = new JButton("返回");
			fanhui_btn.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			fanhui_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			qksBox.add(fanhui_btn);

			// 显示信息
			frontLayer.add(qksPane, "qksPane");
			cardLayout.show(frontLayer, "qksPane");
			frontLayer.validate();

			// 监听返回按钮
			fanhui_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});
		} else {
			// 显示界面已经初始化
			cardLayout.show(frontLayer, "qksPane");
		}
	}

	// 密码修改界面
	JPanel xgPane = null;
	JLabel xgTips = null;
	JPasswordField oldPass = null, newPass = null, newcPass = null;

	public void showXiugai() {
		if (xgPane == null) {
			// 创建修改密码层和Box容器
			xgPane = new JPanel();
			xgPane.setOpaque(false);
			Box xgBox = Box.createVerticalBox();
			xgPane.add(xgBox);
			xgBox.add(Box.createVerticalStrut(150));

			// 设置旧密码行
			JPanel oldPassPane = new JPanel();
			oldPassPane.setOpaque(false);
			JLabel xg_old_label = new JLabel("请输入旧密码：");
			xg_old_label.setForeground(Color.white);
			xg_old_label.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			oldPass = new JPasswordField(10);
			oldPass.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			oldPassPane.add(xg_old_label);
			oldPassPane.add(oldPass);
			xgBox.add(oldPassPane);
			xgBox.add(Box.createVerticalStrut(15));

			// 设置新密码行
			JPanel newPassPane = new JPanel();
			newPassPane.setOpaque(false);
			JLabel xg_new_label = new JLabel("请输入新密码：");
			xg_new_label.setForeground(Color.white);
			xg_new_label.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			newPass = new JPasswordField(10);
			newPass.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			newPassPane.add(xg_new_label);
			newPassPane.add(newPass);
			xgBox.add(newPassPane);
			xgBox.add(Box.createVerticalStrut(15));

			// 设置确认新密码行
			JPanel newcPassPane = new JPanel();
			newcPassPane.setOpaque(false);
			JLabel xg_newc_label = new JLabel("请确认新密码：");
			xg_newc_label.setForeground(Color.white);
			xg_newc_label.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			newcPass = new JPasswordField(10);
			newcPass.setFont(new Font("微软雅黑", Font.PLAIN, 25));
			newcPassPane.add(xg_newc_label);
			newcPassPane.add(newcPass);
			xgBox.add(newcPassPane);
			xgBox.add(Box.createVerticalStrut(30));

			// 设置按钮
			JButton xg_yes = new JButton("确认");
			JButton xg_cancel = new JButton("返回");
			JPanel xg_btn_pane = new JPanel();
			xg_btn_pane.setOpaque(false);
			xg_yes.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			xg_cancel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			xg_btn_pane.add(xg_yes);
			xg_btn_pane.add(xg_cancel);
			xgBox.add(xg_btn_pane);
			xgBox.add(Box.createVerticalStrut(15));

			// 设置提示信息
			xgTips = new JLabel(" ");
			xgTips.setForeground(Color.red);
			xgTips.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			xgTips.setAlignmentX(Component.CENTER_ALIGNMENT);
			xgBox.add(xgTips);

			// 设置确认按钮监听
			xg_yes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String oldins, newins, newcins;
					oldins = new String(oldPass.getPassword());
					newins = new String(newPass.getPassword());
					newcins = new String(newcPass.getPassword());
					if (oldins.equals("") || newins.equals("") || newcins.equals("")) {
						xgTips.setText("输入不能为空！");
						if (oldins.equals(""))
							oldPass.requestFocus();
						else if (newins.equals(""))
							newPass.requestFocus();
						else
							newcPass.requestFocus();
					} else {
						int rtn = bo.doXiugai(Integer.valueOf(oldins), Integer.valueOf(newins),
								Integer.valueOf(newcins));
						if (rtn == -1) {
							xgTips.setText("旧密码错误！");
							oldPass.setText("");
							newPass.setText("");
							newcPass.setText("");
							oldPass.requestFocus();
						} else if (rtn == -2) {
							xgTips.setText("两次密码不一致！");
							newPass.setText("");
							newcPass.setText("");
							newPass.requestFocus();
						} else {
							showXiugaiSuccess();
						}
					}
				}
			});

			// 设置返回按钮监听
			xg_cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});

			// 显示界面
			frontLayer.add(xgPane, "xgPane");
			cardLayout.show(frontLayer, "xgPane");
			frontLayer.validate();
			oldPass.requestFocus();
		} else {
			oldPass.setText("");
			newPass.setText("");
			newcPass.setText("");
			xgTips.setText(" ");
			cardLayout.show(frontLayer, "xgPane");
			oldPass.requestFocus();
		}
	}

	// 密码修改成功界面
	JPanel xgsPane = null;
	public void showXiugaiSuccess() {
		// 界面未初始化
		if (xgsPane == null) {
			// 创建层和组件容器
			xgsPane = new JPanel();
			xgsPane.setOpaque(false);
			Box xgsBox = Box.createVerticalBox();
			xgsPane.add(xgsBox);

			xgsBox.add(Box.createVerticalStrut(150));

			// 创建文字行
			JLabel xgs_Lable = new JLabel();
			xgs_Lable.setForeground(Color.white);
			xgs_Lable.setFont(new Font("微软雅黑", Font.PLAIN, 30));
			xgs_Lable.setAlignmentX(Component.CENTER_ALIGNMENT);
			xgs_Lable.setText("修改密码成功！");
			xgsBox.add(xgs_Lable);
			xgsBox.add(Box.createVerticalStrut(50));

			// 创建返回按钮
			JButton fanhui_btn = new JButton("返回");
			fanhui_btn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
			fanhui_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			xgsBox.add(fanhui_btn);

			// 显示信息
			frontLayer.add(xgsPane, "xgsPane");
			cardLayout.show(frontLayer, "xgsPane");
			frontLayer.validate();

			// 监听返回按钮
			fanhui_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});
		} else {
			// 显示界面已经初始化
			cardLayout.show(frontLayer, "xgsPane");
		}
	}

	// 退卡
	public void quit() {
		// 重新初始化业务对象
		initBO();
		// 重新显示登录界面
		showLogin();
	}
}
