package com.elephant.client.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.*;

import com.elephant.client.tools.ProgramManager;
import com.elephant.client.tools.UserManager;
import com.elephant.common.*;
import com.elephant.connection.Transmission;

public class ListUI implements MouseListener, MouseMotionListener {

	public ListUI(User user) {
		this.u = user;
		createView();
	}

	private JFrame mainframe;
	private JPanel pl_list, pl_head, pl_bottom;// 主要面板
	private JScrollPane sc_list;// 滚动列表
	private JPanel pl_user;// 名单面板
	private JLabel[] lb_list;// 名单卡片
	private JTextField tf_name, tf_group;
	private JLabel lb_hicon, lb_name, lb_group;// 用于放置头像和用户名等
	private JPanel pl_opw;// 窗口操作面板
	private JLabel lb_title, lb_min, lb_max, lb_close;// 窗口首部的标签
	private JLabel lb_settings;
	private User u;
	private int mfwidth = SystemInfo.SCR_WIDTH / 5;
	private int mfhight = SystemInfo.SCR_HEIGHT - 200;

	private void createView() {
		// 窗体设置
		mainframe = new JFrame();
		mainframe.setSize(mfwidth, mfhight);
		mainframe.setUndecorated(true);
		mainframe.setLayout(null);
		// mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 添加窗口操作面板
		pl_opw = new JPanel();
		pl_opw.setBackground(Color.CYAN);
		pl_opw.setLayout(null);
		pl_opw.addMouseListener(this);
		pl_opw.addMouseMotionListener(this);
		lb_title = new JLabel("ELEPHANT");
		lb_title.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lb_title.setForeground(Color.WHITE);
		pl_opw.add(lb_title);
		lb_title.setBounds(10, 3, mfwidth / 2, 23);
		lb_min = new JLabel(new ImageIcon("opwbtn/min.png"));
		lb_max = new JLabel(new ImageIcon("opwbtn/max_nor.png"));
		lb_close = new JLabel(new ImageIcon("opwbtn/close.png"));
		pl_opw.add(lb_min);
		pl_opw.add(lb_max);
		pl_opw.add(lb_close);
		lb_min.setBounds(mfwidth - 90, 0, 30, 23);
		lb_max.setBounds(mfwidth - 60, 0, 30, 23);
		lb_close.setBounds(mfwidth - 30, 0, 30, 23);
		// 为按钮设置鼠标监听
		lb_min.addMouseListener(this);
		lb_max.addMouseListener(this);
		lb_close.addMouseListener(this);
		mainframe.add(pl_opw);
		pl_opw.setBounds(0, 0, mfwidth, 40);

		// 添加头部面板，包括头像和名字
		pl_head = new JPanel();
		pl_head.setBackground(Color.CYAN);
		pl_head.setLayout(null);
		lb_hicon = new JLabel(u.getHicon());
		lb_name = new JLabel(u.getName());
		lb_name.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lb_name.setBorder(null);
		lb_name.setFont(new Font("微软雅黑", Font.BOLD, 20));
		lb_name.addMouseListener(this);
		pl_head.add(lb_hicon);
		lb_hicon.setBounds(20, 5, 75, 75);
		pl_head.add(lb_name);
		lb_name.setBounds(150, 10, 150, 30);
		lb_group = new JLabel(u.getGroup());
		lb_group.addMouseListener(this);
		lb_group.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		pl_head.add(lb_group);
		lb_group.setBounds(150, 45, 80, 30);
		mainframe.add(pl_head);
		pl_head.setBounds(0, 40, mfwidth, 90);

		// 列表界面
		pl_list = new JPanel();
		pl_list.setBackground(Color.WHITE);

		// 底部界面
		pl_bottom = new JPanel();
		pl_bottom.setLayout(null);
		pl_bottom.setBackground(Color.CYAN);
		lb_settings = new JLabel("Settings", new ImageIcon("opwbtn/settings.png"), SwingConstants.LEFT);
		pl_bottom.add(lb_settings);
		lb_settings.setBounds(10, 2, 150, 23);
		lb_settings.addMouseListener(this);
		mainframe.add(pl_bottom);
		pl_bottom.setBounds(0, mfhight - 30, mainframe.getWidth(), 30);

		mainframe.setLocation(SystemInfo.SCR_WIDTH / 2 - mfwidth / 2, SystemInfo.SCR_HEIGHT / 2 - mfhight / 2);
		mainframe.setVisible(true);
		mainframe.validate();
	}

	public void userList() { // 刷新用户列表
		// 清楚之前的列表
		if (sc_list != null && pl_list != null) {
			pl_list.removeAll();
			sc_list.remove(pl_list);
			pl_user.remove(sc_list);
			pl_list.repaint();
			sc_list.repaint();
		}

		int MIN_SIZE = (mfhight - 160) / 75;
		HashMap<String, User> users = UserManager.getUserMap();
		Iterator<Entry<String, User>> it = users.entrySet().iterator();
		int size = users.size();
		if (size >= MIN_SIZE)
			MIN_SIZE = size;
		if (pl_user != null)
			mainframe.remove(pl_user);
		lb_list = new JLabel[size];
		pl_user = new JPanel(new GridLayout(MIN_SIZE, 1, 5, 5));
		for (int i = 0; i < size; i++) {
			Map.Entry<String, User> entry = it.next();
			User tu = entry.getValue();
			if (tu == u)
				continue;
			lb_list[i] = new JLabel(tu.getName() + ":" + tu.getAddr(), tu.getHicon(), SwingConstants.LEFT);
			lb_list[i].setFont(new Font("宋体", Font.PLAIN, 20));
			lb_list[i].addMouseListener(this);
			lb_list[i].setOpaque(true);
			pl_user.add(lb_list[i]);
		}
		sc_list = new JScrollPane(pl_user);
		sc_list.setPreferredSize(new Dimension(mainframe.getWidth(), mfhight - 160));
		pl_list.add(sc_list, "Center");
		mainframe.add(pl_list);
		pl_list.setBounds(0, 130, mainframe.getWidth(), mfhight - 160);
		pl_list.repaint();
		pl_list.revalidate();
		sc_list.repaint();
		sc_list.revalidate();
		pl_user.repaint();
		pl_user.revalidate();

	}

	public void sendChange(int statueChanged) {
		Data d = new Data(statueChanged, u);
		HashMap<String, User> users = UserManager.getUserMap();
		Iterator<Entry<String, User>> it = users.entrySet().iterator();
		int size = users.size();
		for (int i = 0; i < size; i++) {
			Map.Entry<String, User> entry = it.next();
			User tu = entry.getValue();
			if (tu.getAddr().equals(u.getAddr()))
				continue;
			Transmission t = (Transmission) ProgramManager.getObject("T" + tu.getAddr());
			if (t == null) {
				t = new Transmission(tu.getAddr(), d);
				if (t.startCon())
					t.sendData(d);
				else if (statueChanged == Data.SHUTDOWN)
					System.exit(1);
			} else
				t.sendData(d);
		}
	}

	private void startChat(String IP) {
		ChattingUI chat = ProgramManager.getChatDialog(IP);
		Transmission transMsg = (Transmission) ProgramManager.getObject(IP);
		if (chat == null) {
			chat = new ChattingUI(UserManager.getUser(IP), u.getName());
			ProgramManager.putObject("C" + IP, chat);
			if (transMsg == null) {
				transMsg = new Transmission(IP, new Data(Data.MESSAGE, u));
				int i = 3;
				while (i > 0) {
					if (transMsg.startCon()) {
						transMsg.sendData(new Data(Data.MESSAGE, u));
						if (transMsg.readData().getDataType() == Data.ANSWER)
							break;
					} else
						i--;
				}
				if (i > 0) {
					transMsg.start();
					ProgramManager.putObject("T" + IP, transMsg);
				} else {
					UserManager.removeUser(IP);
					ProgramManager.refreshUserList();
				}
			}
			transMsg.setMannulInterrupted(false);
		} else {
			chat.appendText(null, "");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		ListUI to = this;
		if (lb_list != null)
			for (JLabel lb : lb_list) {
				if (e.getClickCount() == 2 && e.getSource() == lb) {
					String[] ip = lb.getText().split(":");
					startChat(ip[1]);
				}
			}
		if (e.getSource() == lb_min) {
			mainframe.setExtendedState(Frame.ICONIFIED);
		}
		if (e.getSource() == lb_close) {
			mainframe.dispose();
			sendChange(Data.SHUTDOWN);
			System.exit(0);
		}
		if (e.getSource() == lb_name) { // 点击后可以更改名字
			if (tf_name == null) {
				tf_name = new JTextField(lb_name.getText());
				tf_name.setFont(new Font("微软雅黑", Font.PLAIN, 20));
				tf_name.addFocusListener(new FocusAdapter() { // 当输入框数去焦点时
					@Override
					public void focusLost(FocusEvent e) {
						lb_name.setText(u.getName());
						lb_name.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						pl_head.remove(tf_name);
						pl_head.add(lb_name);
						pl_head.repaint(); // 需要重绘并且更新才能正确显示
						pl_head.revalidate();
						lb_name.addMouseListener(to);
					}
				});
				tf_name.addKeyListener(new KeyAdapter() { // 键入回车后，重新显示名片
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							u.setName(tf_name.getText());
							lb_name.setText(u.getName());
							lb_name.addMouseListener(to);
							lb_name.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
							pl_head.remove(tf_name);
							pl_head.add(lb_name);
							pl_head.repaint();
							pl_head.revalidate();
							sendChange(Data.INFOCHANGE);
						}
					}
				});
			}
			lb_name.removeMouseListener(this);
			lb_name.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			pl_head.remove(lb_name);
			tf_name.setText(lb_name.getText());
			pl_head.add(tf_name);
			tf_name.setBounds(150, 10, 150, 30);
			tf_name.requestFocusInWindow();
			pl_head.repaint();
			pl_head.revalidate();
		}

		if (e.getSource() == lb_group) { // 点击后可以更改组名
			if (tf_group == null) {
				tf_group = new JTextField(lb_group.getText());
				tf_group.addFocusListener(new FocusAdapter() { // 当输入框数去焦点时
					@Override
					public void focusLost(FocusEvent e) {
						lb_group.setText(u.getGroup());
						lb_group.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						pl_head.remove(tf_group);
						pl_head.add(lb_group);
						pl_head.repaint(); // 需要重绘并且更新才能正确显示
						pl_head.revalidate();
						lb_group.addMouseListener(to);
					}
				});
				tf_group.addKeyListener(new KeyAdapter() { // 键入回车后，重新显示名片
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							u.setGroup(tf_group.getText());
							lb_group.setText(u.getGroup());
							lb_group.addMouseListener(to);
							lb_group.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
							pl_head.remove(tf_group);
							pl_head.add(lb_group);
							pl_head.repaint();
							pl_head.revalidate();
							sendChange(Data.INFOCHANGE);
						}
					}
				});
			}
			lb_group.removeMouseListener(this);
			lb_group.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			pl_head.remove(lb_group);
			tf_group.setText(lb_group.getText());
			pl_head.add(tf_group);
			tf_group.setBounds(150, 45, 100, 30);
			tf_group.requestFocusInWindow();
			pl_head.repaint();
			pl_head.revalidate();
		}
	}

	private int first_x, first_y;

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == pl_opw) {
			first_x = e.getX();
			first_y = e.getY(); // 记录下位移的初点
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	private ImageIcon min, min_sel, close, close_sel, settings_sel, settings;

	@Override
	public void mouseEntered(MouseEvent e) {
		if (lb_list != null)
			for (JLabel lb : lb_list) {
				if (e.getSource() == lb) {
					lb.setBackground(Color.GRAY);
					lb.setToolTipText(lb.getText());
				}
			}
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
		if (e.getSource() == lb_name) { // 名字框的鼠标样式
			lb_name.setToolTipText(lb_name.getText());
		}
		if (e.getSource() == lb_settings) {
			if (settings_sel == null)
				settings_sel = new ImageIcon("opwbtn/settings_sel.png");
			lb_settings.setIcon(settings_sel);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (lb_list != null)
			for (JLabel lb : lb_list) {
				if (e.getSource() == lb) {
					lb.setBackground(null);
				}
			}
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
		if (e.getSource() == lb_settings) {
			if (settings == null)
				settings = new ImageIcon("opwbtn/settings.png");
			lb_settings.setIcon(settings);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX() - first_x;
		int y = e.getY() - first_y; // 取得位移(x,y)
		mainframe.setBounds(mainframe.getX() + x, mainframe.getY() + y, mainframe.getWidth(), mainframe.getHeight());
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	// public static void main(String[] args) {
	// User u = new User();
	// ListUI ui = new ListUI(u);
	// }

}
