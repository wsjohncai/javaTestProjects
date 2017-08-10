package com.elephant.client.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

import com.elephant.client.tools.ImageScale;
import com.elephant.client.tools.ProgramManager;
import com.elephant.client.tools.UserManager;
import com.elephant.common.User;
import com.elephant.connection.Broadcast;
import com.elephant.connection.ReceiverCenter;

public class LoginUI implements MouseListener, MouseMotionListener {

	public static void main(String[] args) {
		LoginUI login = new LoginUI();
		login.loginSurface();
	}

	protected void init(User u) {
		// 初始化列表
		ListUI list = new ListUI(u);
		// 初始化网络
		ReceiverCenter server = new ReceiverCenter(u, u.getPort());
		Thread sThread = new Thread(server);
		sThread.start();
		Broadcast client = new Broadcast(u);
		Thread cThread = new Thread(client);
		cThread.start();
		ProgramManager.putObject("Broadcast", client);
		ProgramManager.putObject("ReceiverCenter", server);
		ProgramManager.putObject("ListUI", list);
	}

	private JFrame loginframe;
	private JPanel login_title, login_body;
	private JLabel title, name, group;
	private JButton login;
	private JTextField ntext, gtext;
	private JLabel lb_min, lb_max, lb_close;

	protected void loginSurface() {
		loginframe = new JFrame();
		loginframe.setSize(450, 278);
		loginframe.setUndecorated(true);
		loginframe.setLayout(null);

		// 设置标题行
		login_title = new JPanel();
		login_title.setBackground(Color.CYAN);
		login_title.setLayout(null);
		login_title.addMouseListener(this);
		login_title.addMouseMotionListener(this);
		title = new JLabel("ELEPHANT");
		title.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		title.setForeground(Color.WHITE);
		login_title.add(title);
		title.setBounds(10, 5, 100, 23);
		lb_min = new JLabel(new ImageIcon("opwbtn/min.png"));
		lb_max = new JLabel(new ImageIcon("opwbtn/max_nor.png"));
		lb_close = new JLabel(new ImageIcon("opwbtn/close.png"));
		login_title.add(lb_min);
		login_title.add(lb_max);
		login_title.add(lb_close);
		lb_min.setBounds(loginframe.getWidth() - 90, 1, 30, 23);
		lb_max.setBounds(loginframe.getWidth() - 60, 1, 30, 23);
		lb_close.setBounds(loginframe.getWidth() - 30, 1, 30, 23);
		// 为按钮设置鼠标监听
		lb_min.addMouseListener(this);
		lb_max.addMouseListener(this);
		lb_close.addMouseListener(this);
		loginframe.add(login_title);
		login_title.setBounds(0, 0, 450, 30);

		// 设置主窗体
		login_body = new JPanel();
		login_body.setLayout(null);
		login_body.setBackground(new Color(48, 201, 222));
		name = new JLabel("登录名:");
		group = new JLabel("工作组:");
		name.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		group.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		name.setOpaque(false);
		group.setOpaque(false);
		login_body.add(name);
		name.setBounds(75, 60, 100, 30);
		login_body.add(group);
		group.setBounds(75, 120, 100, 30);
		ntext = new JTextField();
		gtext = new JTextField();
		login_body.add(ntext);
		ntext.setBounds(175, 60, 200, 30);
		login_body.add(gtext);
		gtext.setBounds(175, 120, 200, 30);
		login = new JButton();
		login.setIcon(new ImageIcon("opwbtn/login.png"));
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				User u = new User();
				u.setName(ntext.getText());
				u.setGroup(gtext.getText());
				String iconfile;
				int serial = (int) (Math.random() * 5 + 1);
				iconfile = serial + ".jpg";
				ImageIcon imgicon = new ImageIcon("defaultheadicon/" + iconfile);
				u.setHicon(new ImageScale().imageScale(75, imgicon.getImage()));
				u.setAddr("127.0.0.1");
				u.setPort(3333);
				UserManager.addUser(u);
				init(u);
				loginframe.dispose();

			}

		});
		login_body.add(login);
		login.setBounds(175, 175, 100, 55);
		loginframe.add(login_body);
		login_body.setBounds(0, 31, 450, 247);

		loginframe.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 225,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 139);
		loginframe.setVisible(true);
		loginframe.validate();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == lb_min) {
			loginframe.setExtendedState(Frame.ICONIFIED);
		}
		if (e.getSource() == lb_close) {
			loginframe.dispose();
			System.exit(0);
		}

	}

	int first_x, first_y;

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == login_title) {
			first_x = e.getX();
			first_y = e.getY(); // 记录下位移的初点
		}

	}

	ImageIcon min, min_sel, close, close_sel, settings_sel, settings;

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == lb_min) {
			if (min_sel == null)
				min_sel = new ImageIcon("opwbtn/min_sel.png");
			lb_min.setIcon(min_sel);
		}
		if (e.getSource() == lb_close) {
			if (close_sel == null)
				close_sel = new ImageIcon("opwbtn/close_sel.png");
			lb_close.setIcon(close_sel);
		}

	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == lb_min) {
			if (min == null)
				min = new ImageIcon("opwbtn/min.png");
			lb_min.setIcon(min);
		}

		if (e.getSource() == lb_close) {
			if (close == null)
				close = new ImageIcon("opwbtn/close.png");
			lb_close.setIcon(close);
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX() - first_x;
		int y = e.getY() - first_y; // 取得位移(x,y)
		loginframe.setBounds(loginframe.getX() + x, loginframe.getY() + y, loginframe.getWidth(),
				loginframe.getHeight());

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
