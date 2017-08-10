package com.elephant.client.view;

import javax.swing.*;

import com.elephant.client.tools.ProgramManager;
import com.elephant.client.tools.UserManager;
import com.elephant.common.*;
import com.elephant.connection.Transmission;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChattingUI implements ActionListener {

	private User targetuser;
	private String myname;
	private JFrame chatframe;
	private JPanel showp, inputp;
	private JScrollPane chatsc, inputsc;
	private JTextArea inputa, showa;
	private JButton sendfile, sendmsg;
	public JTextArea filetrans;
	public JLabel shutfiletrans;
	private int width = SystemInfo.SCR_WIDTH / 3;
	private int height = SystemInfo.SCR_HEIGHT / 2;

	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ChattingUI(User target, String me) {
		targetuser = target;
		myname = me;

		// 设置主窗体
		chatframe = new JFrame(targetuser.getName());
		chatframe.setSize(width, height);
		chatframe.setLayout(null);
		chatframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		chatframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				chatframe.dispose();
				ProgramManager.removeObject("C" + targetuser.getAddr());
				Transmission tran = (Transmission) ProgramManager.getObject("T" + targetuser.getAddr());
				tran.sendData(new Data(Data.ENDCON, UserManager.getUser("127.0.0.1")));
				tran.setMannulInterrupted(true);
			}
		});

		// 聊天历史界面
		showp = new JPanel();
		showa = new JTextArea();
		showa.setFont(new Font("宋体", Font.PLAIN, 16));
		showa.setLineWrap(true);
		showa.setEditable(false);
		showa.setSize(width - 20, height / 10 * 5);
		chatsc = new JScrollPane(showa);
		chatsc.setPreferredSize(new Dimension(width - 20, height / 10 * 7 - 35));
		chatsc.setAutoscrolls(true);
		showp.add(chatsc);
		chatframe.add(showp);
		showp.setBounds(0, 0, width - 20, height / 10 * 5);

		// 输入界面
		inputp = new JPanel();
		inputa = new JTextArea();
		inputa.setFont(new Font("宋体", Font.PLAIN, 16));
		inputa.setLineWrap(true);
		inputa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (inputa.getText().equals("")) {
						inputa.setText("");
						JOptionPane.showMessageDialog(chatframe, "输入不能为空！", "警告", JOptionPane.WARNING_MESSAGE);
					} else {
						Data d = new Data(Data.MESSAGE, UserManager.getUser("127.0.0.1"));
						d.setText(inputa.getText());
						Transmission sendMsg = (Transmission) ProgramManager.getObject("T" + targetuser.getAddr());
						sendMsg.sendData(d);
						appendText(myname, inputa.getText());
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					inputa.setText("");
					inputa.requestFocus();
				}
			}
		});
		inputa.setSize(width - 20, height / 10 * 3);
		inputsc = new JScrollPane(inputa);
		inputsc.setPreferredSize(new Dimension(width - 20, height / 10 * 3));
		inputsc.setAutoscrolls(true);
		inputp.add(inputsc);
		chatframe.add(inputp);
		inputp.setBounds(0, height / 10 * 5, width - 20, height / 10 * 3);

		// 按钮
		sendfile = new JButton();
		sendfile.setIcon(new ImageIcon("opwbtn/sendfile.png"));
		sendfile.addActionListener(this);
		sendmsg = new JButton();
		sendmsg.setIcon(new ImageIcon("opwbtn/sendmsg.png"));
		sendmsg.addActionListener(this);
		chatframe.add(sendfile);
		sendfile.setBounds(width - 40 - 100, height / 10 * 8 + 10, 100, 35);
		chatframe.add(sendmsg);
		sendmsg.setBounds(width - 60 - 100 - 50, height / 10 * 8 + 10, 50, 35);

		// 文件传输标签
		filetrans = new JTextArea();
		filetrans.setForeground(Color.BLUE);
		filetrans.setOpaque(false);
		filetrans.setLineWrap(true);
		filetrans.setWrapStyleWord(true);
		filetrans.setText("");
		chatframe.add(filetrans);
		filetrans.setBounds(20, height / 10 * 8, width - 280, 35);
		filetrans.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (filetrans.getText() != "") {
					filetrans.setToolTipText(filetrans.getText());
				}
			}
		});
		// 取消文件传输标签
		shutfiletrans = new JLabel();
		shutfiletrans.setForeground(Color.RED);
		shutfiletrans.setFont(new Font("宋体", Font.BOLD, 16));
		shutfiletrans.setText("");
		chatframe.add(shutfiletrans);
		shutfiletrans.setBounds(width - 255, height / 10 * 8 + 10, 40, 35);
		shutfiletrans.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		shutfiletrans.setEnabled(false);
		shutfiletrans.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Transmission trans = (Transmission) ProgramManager.getObject("TF" + targetuser.getAddr());
				trans.setMannulInterrupted(true);
				trans = (Transmission) ProgramManager.getObject("T" + targetuser.getAddr());
				trans.sendData(new Data(Data.MANUALINTERUPTED, UserManager.getUser("127.0.0.1")));
				shutfiletrans.setText("");
				shutfiletrans.setEnabled(false);
			}
		});

		chatframe.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width1 = chatframe.getWidth();
				int height1 = chatframe.getHeight();
				chatsc.setPreferredSize(new Dimension(width1 - 20, height1 / 10 * 6 - 35));
				showp.setBounds(0, 0, width1 - 20, height1 / 10 * 6 - 35);
				inputsc.setPreferredSize(new Dimension(width1 - 20, height1 / 10 * 3));
				inputp.setBounds(0, height1 / 10 * 6 - 35, width1 - 20, height1 / 10 * 3);
				sendfile.setBounds(width1 - 40 - 100, height1 / 10 * 9 - 30, 100, 35);
				sendmsg.setBounds(width1 - 60 - 100 - 50, height1 / 10 * 9 - 30, 50, 35);
				filetrans.setBounds(20, height1 / 10 * 9 - 35, width1 - 280, 45);
				shutfiletrans.setBounds(width1 - 255, height1 / 10 * 9 - 30, 40, 35);
			}
		});
		chatframe.setLocation(200, 200);
		chatframe.setVisible(true);
		chatframe.validate();
	}

	public void breakCon() {
		appendText(null, "连接中断，请稍后重试！");
		int i = 3;
		while (i > 0) {
			Data d = new Data(Data.MESSAGE, UserManager.getUser("127.0.0.1"));
			Transmission transMsg = new Transmission(targetuser.getAddr(), d);
			if (transMsg.startCon()) {
				transMsg.sendData(d);
				if (transMsg.readData().getDataType() == Data.ANSWER) {
					ProgramManager.putObject("T" + targetuser.getAddr(), transMsg);
					appendText(null, "连接已恢复！");
				} else
					i--;
			} else
				i--;
		}
		ProgramManager.removeObject("C" + targetuser.getAddr());
	}

	public void appendText(String name, String text) {
		if (name == null) {
			showa.append(text);
			showa.requestFocus();
			return;
		}
		String date;
		Calendar calendar = Calendar.getInstance();
		date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(calendar.getTime());
		date = name + "    " + date + "  \n\r";
		showa.append(date);
		showa.append(text);
		showa.append("\n\n\r");
		showa.setCaretPosition(showa.getDocument().getLength());
	}

	public void fileTransError() {
		JOptionPane.showMessageDialog(chatframe, "文件传输异常中断，请稍后再试！", "错误", JOptionPane.ERROR_MESSAGE);
	}

	public File fileReceive(String fileinfo) {
		// 询问是否接受文件
		String[] options = { "确认", "拒绝" };
		int op = JOptionPane.showOptionDialog(chatframe, "是否接收文件", "确认", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (op == JOptionPane.NO_OPTION) { // 不接收文件
			return null;
		}
		// 同意接收文件,选择存储位置
		String[] fi = fileinfo.split(":");
		String filename = fi[0];
		return selectFile(filename);

	}

	private File selectFile(String fileName) {
		JFileChooser choose = new JFileChooser();
		choose.setDialogTitle("请选择文件存储位置 ");
		choose.setSelectedFile(new File(fileName));
		choose.showSaveDialog(chatframe);
		File directory = choose.getSelectedFile();
		if (directory.exists() && directory.isFile()) {
			int op1 = JOptionPane.showConfirmDialog(chatframe, "目标文件存在，是否覆盖", "文件已存在", JOptionPane.OK_CANCEL_OPTION);
			if (op1 == JOptionPane.CANCEL_OPTION)
				directory = selectFile(fileName);
		}
		return directory;
	}

	public static void main(String[] args) {
		User u = new User();
		u.setAddr("127.0.0.1");
		u.setName("Local");
		ChattingUI ch = new ChattingUI(new User(), "测试");
		ProgramManager.putObject(u.getName(), ch);
		// File file = ch.fileReceive("Test.java");
		// System.out.println(file.getAbsolutePath());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendmsg) {
			if (inputa.getText().equals("")) {
				JOptionPane.showMessageDialog(chatframe, "输入不能为空！", "警告", JOptionPane.WARNING_MESSAGE);
			} else {
				Data d = new Data(Data.MESSAGE, UserManager.getUser("127.0.0.1"));
				d.setText(inputa.getText());
				Transmission sendMsg = (Transmission) ProgramManager.getObject("T" + targetuser.getAddr());
				sendMsg.sendData(d);
				appendText(myname, inputa.getText());
			}
			inputa.setText("");
			inputa.requestFocus();
		}

		// 响应发送文件按钮
		if (e.getSource() == sendfile) {
			JFileChooser choose = new JFileChooser();
			choose.setDialogTitle("请选择文件");
			choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
			choose.showOpenDialog(chatframe);
			File file = choose.getSelectedFile();

			// 创建发送文件的进程
			Transmission sendfile = new Transmission(file, targetuser);
			sendfile.start();
			ProgramManager.putObject("TF" + targetuser.getAddr(), sendfile);
		}
	}

}
