package com.johncai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATMUI {
	// ������
	public static void main(String[] args) {
		ATMUI ui = new ATMUI();
		// ��ʼ��ҵ�����
		ui.initBO();
		// ��ʼ��������
		ui.initFrame();
		// ��ʵ��¼����
		ui.showLogin();
	}

	/**
	 * ��ʼ��ҵ�����
	 *
	 * ҵ�����ÿһ���û���Ӧһ���µ�ҵ����󲢱�����û����� ��ˣ����������ÿ���˳���Ҫ����һ���µ�ҵ��������һ�� �û�ʹ��
	 */
	public ATMBO bo = null;

	public void initBO() {
		bo = new ATMBO();
	}

	/**
	 * ��ʼ��������
	 */
	// ��������
	public int width = 960;
	public int height = 720;

	// ���洰��
	public JFrame jFrame = null;
	// �������
	public JLayeredPane layeredPane = null;

	// ������
	public JPanel backLayer = null;
	// ǰ����
	public JPanel frontLayer = null;
	// ǰ���㲼����
	public CardLayout cardLayout = null;

	public void initFrame() {
		// --��ʼ��������������
		// �������ڶ��󣬴��ڱ���Ϊ��ATM����ϵͳ��
		jFrame = new JFrame("ATM����ϵͳ");

		// ������������
		layeredPane = new JLayeredPane();
		// ���ò��������С
		layeredPane.setPreferredSize(new Dimension(width, height));

		// �Ѳ��������ӵ�������
		jFrame.add(layeredPane);
		// ���ô��ڲ��ܷŴ���С
		jFrame.setResizable(false);
		// ���ô��ڴ�С��Ӧ���ݴ�С
		jFrame.pack();
		// ���ô��ڿɼ���Ĭ�ϲ��ɼ���
		jFrame.setVisible(true);
		// ���ô��ڹر�ʱ������ر�
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// --�ڲ����������ӱ�����
		// �������������
		backLayer = new JPanel();
		// ���ñ����㲼����FlowLayout��ˮƽ���Ϊ0��Ĭ��Ϊ5��
		((FlowLayout) backLayer.getLayout()).setHgap(0);
		// ���ñ����㲼����FlowLayout�Ĵ�ֱ���Ϊ0��Ĭ��Ϊ5��
		((FlowLayout) backLayer.getLayout()).setVgap(0);
		// ���ñ������С��ͬ���ڣ�
		backLayer.setSize(width, height);
		// ���ñ�����λ�ã�����ڴ������Ͻǣ�
		backLayer.setLocation(0, 0);

		// ��������ͼ��
		JLabel bg = new JLabel(new ImageIcon("images/bg.jpg"));
		// �ڱ���������ӱ���ͼ
		backLayer.add(bg);

		// �ɱ�������ӵ���������ĽϵͲ�
		layeredPane.add(backLayer, new Integer(0));

		// --�ڲ������������ǰ����
		frontLayer = new JPanel();
		// ����CardLayout����������ˮƽ����ֱ���Ϊ0
		cardLayout = new CardLayout(0, 0);
		// ��ǰ����Ĳ���������ΪCardLayout
		frontLayer.setLayout(cardLayout);
		// ��ǰ����ɫ���ó�͸�������ܿ��������㣩
		frontLayer.setOpaque(false);
		// ����ǰ�����С��ͬ���ڣ�
		frontLayer.setSize(width, height);
		// ����ǰ����λ�ã�����ڴ������Ͻǣ�
		frontLayer.setLocation(0, 0);

		// ��ǰ������ӵ���������Ľ϶���
		layeredPane.add(frontLayer, new Integer(1));
	}

	/**
	 * ��¼���� CardLayout�����ǽ����������Ľ���ֲ㣬�ڵ�����Ӧ��ʱ �Ѷ�Ӧ�������㼴�ɣ����⣬ÿ�������һ�ε���ʱ��
	 * ���ʼ��������ĵ��þͿ���ֱ�Ӱ��ѳ�ʼ���Ĳ�������� ��һЩ��Ҫ�齨���ü��ɡ�
	 */
	// ��¼������
	public JPanel loginPane = null;
	// ��¼���������
	public JTextField loginCodeInput = null;
	// ��¼������ʾ��
	public JPasswordField loginPassInput = null;
	// ��¼��ʾ��Ϣ
	public JLabel loginTipsLabel = null;

	public void showLogin() {
		if (loginPane == null) {
			// --��½��δ��ʼ��ʱ

			// ������¼����������
			loginPane = new JPanel();
			// �ѵ�¼��ı�����ɫ���͸��
			loginPane.setOpaque(false);
			// --����¼����������
			// ����һ����ֱBox����
			Box loginBox = Box.createVerticalBox();
			// �ڴ�ֱ���������180�߶ȵľ���
			loginBox.add(Box.createVerticalStrut(180));

			// ����һ����ӭ��Ϣ����
			JPanel welcome_panel = new JPanel();
			// �ѻ�ӭ��Ϣ�������óɱ���ɫ͸��
			welcome_panel.setOpaque(false);
			// ������ӭ��Ϣ����ӭʹ�ú������С�
			JLabel welcome_label = new JLabel("��ӭʹ�ú�������");
			// ������Ϣ����
			welcome_label.setForeground(Color.WHITE);
			welcome_label.setFont(new Font("΢���ź�", Font.PLAIN, 30));
			// �ѻ�ӭ��Ϣ��ӵ���ӭ����
			welcome_panel.add(welcome_label);
			// �ѻ�ӭ��Ϣ������ӵ���ֱBox����
			loginBox.add(welcome_panel);
			// �ڴ�ֱBox�����30�߶ȵľ���
			loginBox.add(Box.createVerticalStrut(30));

			// ����һ��������������
			JPanel code_panel = new JPanel();
			// �ѿ������������������͸��
			code_panel.setOpaque(false);
			// ������ʾ�������뿨�š�
			JLabel code_label = new JLabel("�����뿨�ţ�");
			// ������Ϣ����
			code_label.setForeground(Color.WHITE);
			code_label.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			// ����ʾ���뿨����Ϣ��ӵ�������������
			code_panel.add(code_label);
			// �������������
			loginCodeInput = new JTextField(10);
			// ���ÿ������������
			loginCodeInput.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			// �ѿ����������ӵ�������������
			code_panel.add(loginCodeInput);
			// �ѿ�������������ӵ���ֱBox
			loginBox.add(code_panel);

			// ����20�߶ȴ�ֱBox����
			loginBox.add(Box.createVerticalStrut(20));

			// ����������������
			JPanel pass_panel = new JPanel();
			// �����������������͸��
			pass_panel.setOpaque(false);
			// ������ʾ�ı�
			JLabel pass_label = new JLabel("���������룺");
			// ������ʾ��Ϣ����
			pass_label.setForeground(Color.WHITE);
			pass_label.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			// ��������ʾ��Ϣ�������������
			pass_panel.add(pass_label);
			// �������������
			loginPassInput = new JPasswordField(10);
			// �����������������
			loginPassInput.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			// ��������������������������
			pass_panel.add(loginPassInput);
			// �����������������봹ֱBox����
			loginBox.add(pass_panel);

			// �ڴ�ֱBox�������30�߶ȵľ���
			loginBox.add(Box.createVerticalStrut(30));

			// ������ť����
			JPanel btn_panel = new JPanel();
			// �Ѱ�ť�������ó�͸��
			btn_panel.setOpaque(false);
			// ������¼��ť����������
			JButton login_btn = new JButton("�� ¼");
			login_btn.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			// �Ѱ�ť��ӵ���ť����
			btn_panel.add(login_btn);
			// �������ð�ť����������
			JButton reset_btn = new JButton("����");
			reset_btn.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			// �����ð�ť��ӵ���ť����
			btn_panel.add(reset_btn);
			// �Ѱ�ť������ӵ���ֱBox����
			loginBox.add(btn_panel);

			// �ڴ�ֱBox�����10�߶ȵľ���
			loginBox.add(Box.createVerticalStrut(10));

			// ������¼��ʾ��Ϣ����
			JPanel tips_panel = new JPanel();
			// �ѵ�¼��ʾ��Ϣ�����ı������ó�͸��
			tips_panel.setOpaque(false);
			// ������¼��ʾ��Ϣ������������
			loginTipsLabel = new JLabel(" ");
			loginTipsLabel.setForeground(new Color(238, 32, 32));
			loginTipsLabel.setFont(new Font("΢���ź�", Font.PLAIN, 20));
			// �ѵ�¼��ʾ��Ϣ��ӵ���ʾ������
			tips_panel.add(loginTipsLabel);
			// ����ʾ��Ϣ������ӵ���ֱBox����
			loginBox.add(tips_panel);

			// �Ѵ�ֱBox������ӵ���¼��������
			loginPane.add(loginBox);

			// --��ʾ��¼��
			// �ѵ�¼����ӵ�ǰ������
			// frontLayer.add("loginPane",loginPane);
			frontLayer.add(loginPane, "loginPane");
			// ʹ��¼����ǰ�������������ڶ�����ʾ
			cardLayout.show(frontLayer, "loginPane");
			// ˢ��ǰ����ʹ����ӻ�
			frontLayer.validate();
			// ʹ����������ȡ����
			loginCodeInput.requestFocus();

			// --�������ð�ť
			reset_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					// ��տ��������������������ʾ��Ϣ
					loginCodeInput.setText("");
					loginPassInput.setText("");
					loginTipsLabel.setText("");
					// ʹ����������ȡ����
					loginCodeInput.requestFocus();
				}
			});

			// --������¼��ť�¼�
			login_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					// ��ȡ�û�����Ŀ��ź�����
					String code_str = loginCodeInput.getText();
					String pass_str = new String(loginPassInput.getPassword());
					if ("".equals(code_str)) {
						loginTipsLabel.setText("���Ų���Ϊ�գ�");
						loginCodeInput.requestFocus();
					} else if ("".equals(pass_str)) {
						loginTipsLabel.setText("���벻��Ϊ�գ�");
						loginPassInput.requestFocus();
					} else {
						// �����ź����붼��Ϊ�գ�����ҵ�����ĵ�¼ҵ�񷽷������ؽ��
						int login_rtn = bo.doLogin(code_str, Integer.valueOf(pass_str));
						if (login_rtn == -1) {
							// ����-1����ʾ��¼�ɹ�����ʾ���˵����棨δʵ�֣�
							showMenu();
						} else if (login_rtn == 3) {
							// ����3����ʾ��������ѳ���3�Σ���ʾ�̿���ʾ���棨δʵ�֣�
							showTunka();
						} else {
							loginCodeInput.setText("");
							loginPassInput.setText("");
							loginTipsLabel.setText("���Ż�����������������룬������" + (3 - login_rtn) + "�λ��ᣡ");
							loginCodeInput.requestFocus();
						}
					}
				}
			});
		} else {
			// --��¼���ѳ�ʼ��
			// --��¼����ǰ�������������ڶ���
			cardLayout.show(frontLayer, "loginPane");
			// ����
			loginCodeInput.setText("");
			loginPassInput.setText("");
			loginTipsLabel.setText("");
			loginCodeInput.requestFocus();
		}
	}

	public JPanel tunkaPane = null;

	// �̿���ʾ����
	public void showTunka() {
		if (tunkaPane == null) {
			// �̿�����δ��ʼ��ʹ
			tunkaPane = new JPanel();
			tunkaPane.setOpaque(false);

			// ���̿���ʾ���������������
			Box tunkaBox = Box.createVerticalBox();

			tunkaBox.add(Box.createVerticalStrut(180));

			JPanel tunka_panel = new JPanel();
			tunka_panel.setOpaque(false);
			JLabel tunka_label = new JLabel("���ѳ���3��������ᣬϵͳ�̿�������ϵ�ͷ���");
			tunka_label.setForeground(Color.WHITE);
			tunka_label.setFont(new Font("΢���ź�", Font.PLAIN, 30));
			tunka_panel.add(tunka_label);
			tunkaBox.add(tunka_panel);

			tunkaBox.add(Box.createVerticalStrut(30));

			JPanel btn_panel = new JPanel();
			btn_panel.setOpaque(false);
			JButton tunka_btn = new JButton("ȷ ��");
			tunka_btn.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			btn_panel.add(tunka_btn);
			tunkaBox.add(btn_panel);

			tunkaPane.add(tunkaBox);

			// ��ʾ�̿���ʾ����
			frontLayer.add(tunkaPane, "tunkaPane");
			cardLayout.show(frontLayer, "tunkaPane");
			frontLayer.validate();

			// ��ͦȷ����ť�¼�
			tunka_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					// �˳�
					quit();
				}
			});
		} else {
			// --�̿���ʾ������ѳ�ʼ��
			cardLayout.show(frontLayer, "tunkaPane");
		}
	}

	// ���˵�����

	// ���˵���������
	public JPanel menuPane = null;

	public void showMenu() {
		if (menuPane == null) {
			// ���˵�����δ��ʼ��ʱ
			menuPane = new JPanel();
			menuPane.setOpaque(false);
			// �����˵����������ΪBorderLayout
			menuPane.setLayout(new BorderLayout());

			// �����˵����������������
			// ͷ����Ϣ��ʾ�飬λ��BorderLayout����
			Box tipsBox = Box.createVerticalBox();
			menuPane.add(tipsBox, BorderLayout.NORTH);

			tipsBox.add(Box.createVerticalStrut(150));

			JLabel tips_label = new JLabel("��ѡ������Ҫ�ķ���");
			tips_label.setForeground(Color.WHITE);
			tips_label.setFont(new Font("΢���ź�", Font.PLAIN, 30));
			tips_label.setAlignmentX(Component.CENTER_ALIGNMENT);
			tipsBox.add(tips_label);

			// ������ť�飬λ��BorderLayout����
			Box menuLeft = Box.createVerticalBox();
			menuPane.add(menuLeft, BorderLayout.WEST);

			menuLeft.add(Box.createVerticalStrut(50));

			JButton chaxun_btn = new JButton("��ѯ���");
			chaxun_btn.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			menuLeft.add(chaxun_btn);
			menuLeft.add(Box.createVerticalStrut(100));

			JButton cunkuan_btn = new JButton("��  ��");
			cunkuan_btn.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			menuLeft.add(cunkuan_btn);
			menuLeft.add(Box.createVerticalStrut(100));

			JButton qukuan_btn = new JButton("ȡ  ��");
			qukuan_btn.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			menuLeft.add(qukuan_btn);

			// �Ҳఴť����λ��BorderLayout����
			Box menuRight = Box.createVerticalBox();
			menuPane.add(menuRight, BorderLayout.EAST);

			menuRight.add(Box.createVerticalStrut(50));
			menuRight.setAlignmentX(Component.RIGHT_ALIGNMENT);

			JButton xiugai_btn = new JButton("��  ��");
			xiugai_btn.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			menuRight.add(xiugai_btn);

			menuRight.add(Box.createVerticalStrut(100));

			JButton quit_btn = new JButton("��  ��");
			quit_btn.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			menuRight.add(quit_btn);

			// ��ʾ���˵�����
			frontLayer.add(menuPane, "menuPane");
			cardLayout.show(frontLayer, "menuPane");
			frontLayer.validate();

			// ������ť�¼�
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
			// ���˵��ѳ�ʼ��
			cardLayout.show(frontLayer, "menuPane");
		}
	}

	// ��ѯ����
	public JPanel showyuePane = null;
	JLabel yue_label = null;

	public void showChaxun() {
		// ��ѯ������δ��ʼ��
		if (showyuePane == null) {
			// ����������������
			showyuePane = new JPanel();
			showyuePane.setOpaque(false);
			Box showyueBox = Box.createVerticalBox();
			showyuePane.add(showyueBox);

			showyueBox.add(Box.createVerticalStrut(150));

			// ����������
			yue_label = new JLabel();
			yue_label.setForeground(Color.white);
			yue_label.setFont(new Font("΢���ź�", Font.PLAIN, 30));
			yue_label.setAlignmentX(Component.CENTER_ALIGNMENT);
			yue_label.setText("�����˻����Ϊ��" + bo.doChaxun() + "Ԫ");
			showyueBox.add(yue_label);
			showyueBox.add(Box.createVerticalStrut(50));

			// �������ذ�ť
			JButton fanhui_btn = new JButton("����");
			fanhui_btn.setFont(new Font("΢���ź�", Font.PLAIN, 20));
			fanhui_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			showyueBox.add(fanhui_btn);

			// ��ʾ���
			frontLayer.add(showyuePane, "showyuePane");
			cardLayout.show(frontLayer, "showyuePane");
			frontLayer.validate();

			// �������ذ�ť
			fanhui_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});
		} else {
			// ��ʾ���Ľ����Ѿ���ʼ��
			yue_label.setText("�����˻����Ϊ��" + bo.doChaxun() + "Ԫ");
			cardLayout.show(frontLayer, "showyuePane");
		}
	}

	// ������
	public JTextField ck_textfield = null;
	public JPanel ckPane = null;
	public JLabel ckTips = null;

	public void showCunkuan() {
		// ϵͳδ��ʼ��
		if (ckPane == null) {
			// �������ҳ���ʹ�ֱBox����
			ckPane = new JPanel();
			ckPane.setOpaque(false);
			Box ckBox = Box.createVerticalBox();
			ckPane.add(ckBox);
			ckBox.add(Box.createVerticalStrut(150));

			// �������������������
			JPanel ckinPane = new JPanel();
			ckinPane.setOpaque(false);
			JLabel ck_label = new JLabel("���������");
			ck_label.setForeground(Color.white);
			ck_label.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			ck_textfield = new JTextField(10);
			ck_textfield.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			ckinPane.add(ck_label);
			ckinPane.add(ck_textfield);
			ckBox.add(ckinPane);
			ckBox.add(Box.createVerticalStrut(30));

			// ������ť�����Ͱ�ť
			JButton ck_yes = new JButton("ȷ��");
			JButton ck_cancel = new JButton("����");
			JPanel ck_button_pane = new JPanel();
			ck_button_pane.setOpaque(false);
			ck_yes.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			ck_cancel.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			ck_button_pane.add(ck_yes);
			ck_button_pane.add(ck_cancel);
			ckBox.add(ck_button_pane);
			ckBox.add(Box.createVerticalStrut(30));

			// ������ʾ��Ϣ
			ckTips = new JLabel(" ");
			ckTips.setForeground(Color.red);
			ckTips.setFont(new Font("΢���ź�", Font.PLAIN, 20));
			ckTips.setAlignmentX(Component.CENTER_ALIGNMENT);
			ckBox.add(ckTips);

			// ����ȷ�ϰ�ť�¼�
			ck_yes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String ckin = ck_textfield.getText();
					int ckmoney = 0;
					// �������Ϊ�գ���ʾ��Ϣ
					if (ckin.equals("")) {
						ckTips.setText("���벻��Ϊ�գ�");
						ck_textfield.requestFocus();
					} else if (((ckmoney = Integer.parseInt(ckin)) % 100 != 0) || (ckmoney == 0)) {
						// ������벻Ϊ����
						ckTips.setText("��������ٽ�");
						ck_textfield.setText("");
						ck_textfield.requestFocus();
					} else {
						// ������ȷ,���ú���
						ck_textfield.setText("");
						bo.doCunkuan(ckmoney);
						showCunkuanSuccess();
					}
				}
			});

			// �������ذ�ť�¼�
			ck_cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});

			ckPane.add(ckBox);
			// ��ʾ������
			frontLayer.add(ckPane, "ckPane");
			cardLayout.show(frontLayer, "ckPane");
			frontLayer.validate();
			ck_textfield.requestFocus();
		} else {
			// �ѳ�ʼ��
			ck_textfield.setText("");
			ckTips.setText(" ");
			cardLayout.show(frontLayer, "ckPane");
			ck_textfield.requestFocus();
		}
	}

	// ���ɹ���ʾ����
	public JPanel cksPane = null;

	public void showCunkuanSuccess() {
		// ����δ��ʼ��
		if (cksPane == null) {
			// ��������������
			cksPane = new JPanel();
			cksPane.setOpaque(false);
			Box cksBox = Box.createVerticalBox();
			cksPane.add(cksBox);

			cksBox.add(Box.createVerticalStrut(150));

			// ����������
			JLabel cks_Lable = new JLabel();
			cks_Lable.setForeground(Color.white);
			cks_Lable.setFont(new Font("΢���ź�", Font.PLAIN, 30));
			cks_Lable.setAlignmentX(Component.CENTER_ALIGNMENT);
			cks_Lable.setText("���ɹ���");
			cksBox.add(cks_Lable);
			cksBox.add(Box.createVerticalStrut(50));

			// �������ذ�ť
			JButton fanhui_btn = new JButton("����");
			fanhui_btn.setFont(new Font("΢���ź�", Font.PLAIN, 20));
			fanhui_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			cksBox.add(fanhui_btn);

			// ��ʾ��Ϣ
			frontLayer.add(cksPane, "cksPane");
			cardLayout.show(frontLayer, "cksPane");
			frontLayer.validate();

			// �������ذ�ť
			fanhui_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});
		} else {
			// ��ʾ�����Ѿ���ʼ��
			cardLayout.show(frontLayer, "cksPane");
		}
	}

	// ȡ���ʵ��
	public JTextField qk_textfield = null;
	public JPanel qkPane = null;
	public JLabel qkTips = null;

	public void showQukuan() {
		// ϵͳδ��ʼ��
		if (qkPane == null) {
			// �������ҳ���ʹ�ֱBox����
			qkPane = new JPanel();
			qkPane.setOpaque(false);
			Box qkBox = Box.createVerticalBox();
			qkPane.add(qkBox);
			qkBox.add(Box.createVerticalStrut(150));

			// �������������������
			JPanel qkinPane = new JPanel();
			qkinPane.setOpaque(false);
			JLabel qk_label = new JLabel("������ȡ���");
			qk_label.setForeground(Color.white);
			qk_label.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			qk_textfield = new JTextField(10);
			qk_textfield.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			qkinPane.add(qk_label);
			qkinPane.add(qk_textfield);
			qkBox.add(qkinPane);
			qkBox.add(Box.createVerticalStrut(30));

			// ������ť�����Ͱ�ť
			JButton qk_yes = new JButton("ȷ��");
			JButton qk_cancel = new JButton("����");
			JPanel qk_button_pane = new JPanel();
			qk_button_pane.setOpaque(false);
			qk_yes.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			qk_cancel.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			qk_button_pane.add(qk_yes);
			qk_button_pane.add(qk_cancel);
			qkBox.add(qk_button_pane);
			qkBox.add(Box.createVerticalStrut(30));

			// ������ʾ��Ϣ
			qkTips = new JLabel(" ");
			qkTips.setForeground(Color.red);
			qkTips.setFont(new Font("΢���ź�", Font.PLAIN, 20));
			qkTips.setAlignmentX(Component.CENTER_ALIGNMENT);
			qkBox.add(qkTips);

			// ����ȷ�ϰ�ť�¼�
			qk_yes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String qkin = qk_textfield.getText();
					int qkmoney = 0;
					// �������Ϊ�գ���ʾ��Ϣ
					if (qkin.equals("")) {
						qkTips.setText("���벻��Ϊ�գ�");
						qk_textfield.requestFocus();
					} else if (((qkmoney = Integer.parseInt(qkin)) % 100 != 0) || (qkmoney == 0)) {
						// ������벻Ϊ����
						qkTips.setText("���������ٽ�");
						qk_textfield.setText("");
						qk_textfield.requestFocus();
					} else if (qkmoney > bo.doChaxun()) {
						qk_textfield.setText("");
						qkTips.setText("�������㣡");
						qk_textfield.requestFocus();
					} else {
						// ������ȷ,���ú���
						qk_textfield.setText("");
						bo.doQukuan(qkmoney);
						showQukuanSuccess();
					}
				}
			});

			// �������ذ�ť�¼�
			qk_cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});

			qkPane.add(qkBox);
			// ��ʾ������
			frontLayer.add(qkPane, "qkPane");
			cardLayout.show(frontLayer, "qkPane");
			frontLayer.validate();
			qk_textfield.requestFocus();
		} else {
			// �ѳ�ʼ��
			qk_textfield.setText("");
			qkTips.setText(" ");
			cardLayout.show(frontLayer, "qkPane");
			qk_textfield.requestFocus();
		}
	}

	// ȡ��ɹ�����
	JPanel qksPane = null;

	public void showQukuanSuccess() {
		// ����δ��ʼ��
		if (qksPane == null) {
			// ��������������
			qksPane = new JPanel();
			qksPane.setOpaque(false);
			Box qksBox = Box.createVerticalBox();
			qksPane.add(qksBox);

			qksBox.add(Box.createVerticalStrut(150));

			// ����������
			JLabel qks_Lable = new JLabel();
			qks_Lable.setForeground(Color.white);
			qks_Lable.setFont(new Font("΢���ź�", Font.PLAIN, 30));
			qks_Lable.setAlignmentX(Component.CENTER_ALIGNMENT);
			qks_Lable.setText("ȡ��ɹ���");
			qksBox.add(qks_Lable);
			qksBox.add(Box.createVerticalStrut(50));

			// �������ذ�ť
			JButton fanhui_btn = new JButton("����");
			fanhui_btn.setFont(new Font("΢���ź�", Font.PLAIN, 20));
			fanhui_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			qksBox.add(fanhui_btn);

			// ��ʾ��Ϣ
			frontLayer.add(qksPane, "qksPane");
			cardLayout.show(frontLayer, "qksPane");
			frontLayer.validate();

			// �������ذ�ť
			fanhui_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});
		} else {
			// ��ʾ�����Ѿ���ʼ��
			cardLayout.show(frontLayer, "qksPane");
		}
	}

	// �����޸Ľ���
	JPanel xgPane = null;
	JLabel xgTips = null;
	JPasswordField oldPass = null, newPass = null, newcPass = null;

	public void showXiugai() {
		if (xgPane == null) {
			// �����޸�������Box����
			xgPane = new JPanel();
			xgPane.setOpaque(false);
			Box xgBox = Box.createVerticalBox();
			xgPane.add(xgBox);
			xgBox.add(Box.createVerticalStrut(150));

			// ���þ�������
			JPanel oldPassPane = new JPanel();
			oldPassPane.setOpaque(false);
			JLabel xg_old_label = new JLabel("����������룺");
			xg_old_label.setForeground(Color.white);
			xg_old_label.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			oldPass = new JPasswordField(10);
			oldPass.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			oldPassPane.add(xg_old_label);
			oldPassPane.add(oldPass);
			xgBox.add(oldPassPane);
			xgBox.add(Box.createVerticalStrut(15));

			// ������������
			JPanel newPassPane = new JPanel();
			newPassPane.setOpaque(false);
			JLabel xg_new_label = new JLabel("�����������룺");
			xg_new_label.setForeground(Color.white);
			xg_new_label.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			newPass = new JPasswordField(10);
			newPass.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			newPassPane.add(xg_new_label);
			newPassPane.add(newPass);
			xgBox.add(newPassPane);
			xgBox.add(Box.createVerticalStrut(15));

			// ����ȷ����������
			JPanel newcPassPane = new JPanel();
			newcPassPane.setOpaque(false);
			JLabel xg_newc_label = new JLabel("��ȷ�������룺");
			xg_newc_label.setForeground(Color.white);
			xg_newc_label.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			newcPass = new JPasswordField(10);
			newcPass.setFont(new Font("΢���ź�", Font.PLAIN, 25));
			newcPassPane.add(xg_newc_label);
			newcPassPane.add(newcPass);
			xgBox.add(newcPassPane);
			xgBox.add(Box.createVerticalStrut(30));

			// ���ð�ť
			JButton xg_yes = new JButton("ȷ��");
			JButton xg_cancel = new JButton("����");
			JPanel xg_btn_pane = new JPanel();
			xg_btn_pane.setOpaque(false);
			xg_yes.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			xg_cancel.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			xg_btn_pane.add(xg_yes);
			xg_btn_pane.add(xg_cancel);
			xgBox.add(xg_btn_pane);
			xgBox.add(Box.createVerticalStrut(15));

			// ������ʾ��Ϣ
			xgTips = new JLabel(" ");
			xgTips.setForeground(Color.red);
			xgTips.setFont(new Font("΢���ź�", Font.PLAIN, 20));
			xgTips.setAlignmentX(Component.CENTER_ALIGNMENT);
			xgBox.add(xgTips);

			// ����ȷ�ϰ�ť����
			xg_yes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String oldins, newins, newcins;
					oldins = new String(oldPass.getPassword());
					newins = new String(newPass.getPassword());
					newcins = new String(newcPass.getPassword());
					if (oldins.equals("") || newins.equals("") || newcins.equals("")) {
						xgTips.setText("���벻��Ϊ�գ�");
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
							xgTips.setText("���������");
							oldPass.setText("");
							newPass.setText("");
							newcPass.setText("");
							oldPass.requestFocus();
						} else if (rtn == -2) {
							xgTips.setText("�������벻һ�£�");
							newPass.setText("");
							newcPass.setText("");
							newPass.requestFocus();
						} else {
							showXiugaiSuccess();
						}
					}
				}
			});

			// ���÷��ذ�ť����
			xg_cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});

			// ��ʾ����
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

	// �����޸ĳɹ�����
	JPanel xgsPane = null;
	public void showXiugaiSuccess() {
		// ����δ��ʼ��
		if (xgsPane == null) {
			// ��������������
			xgsPane = new JPanel();
			xgsPane.setOpaque(false);
			Box xgsBox = Box.createVerticalBox();
			xgsPane.add(xgsBox);

			xgsBox.add(Box.createVerticalStrut(150));

			// ����������
			JLabel xgs_Lable = new JLabel();
			xgs_Lable.setForeground(Color.white);
			xgs_Lable.setFont(new Font("΢���ź�", Font.PLAIN, 30));
			xgs_Lable.setAlignmentX(Component.CENTER_ALIGNMENT);
			xgs_Lable.setText("�޸�����ɹ���");
			xgsBox.add(xgs_Lable);
			xgsBox.add(Box.createVerticalStrut(50));

			// �������ذ�ť
			JButton fanhui_btn = new JButton("����");
			fanhui_btn.setFont(new Font("΢���ź�", Font.PLAIN, 15));
			fanhui_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			xgsBox.add(fanhui_btn);

			// ��ʾ��Ϣ
			frontLayer.add(xgsPane, "xgsPane");
			cardLayout.show(frontLayer, "xgsPane");
			frontLayer.validate();

			// �������ذ�ť
			fanhui_btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMenu();
				}
			});
		} else {
			// ��ʾ�����Ѿ���ʼ��
			cardLayout.show(frontLayer, "xgsPane");
		}
	}

	// �˿�
	public void quit() {
		// ���³�ʼ��ҵ�����
		initBO();
		// ������ʾ��¼����
		showLogin();
	}
}
