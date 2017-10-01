package com.wsjc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.wsjc.connection.FileRecv;
import com.wsjc.connection.FileSend;
import com.wsjc.connection.ListenBC;
import com.wsjc.connection.SendDataPkg;
import com.wsjc.connection.UserSeeker;
import com.wsjc.data.Data;
import com.wsjc.data.User;
import com.wsjc.tools.ThreadMgr;
import com.wsjc.tools.UserMgr;

/**
 * This application is used to transfer files in a LAN network
 * 
 * @version 1.1
 * @author WSJohnCai
 *
 */

public class FTView extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	// Variables are defined here
	private JPanel bg_pl, recv_pl, fc_pl, tfinfo_pl; // �������
	private JTextField file_tf;
	private JComboBox<String> recv_list;
	private JButton fc_btn, send_btn;
	private JTextArea info_ta;
	private JScrollPane info_sc;
	private Box backPane;
	private int SW = getToolkit().getScreenSize().width, SH = getToolkit().getScreenSize().height;
	private HashMap<String, ProgressBar> bars = new HashMap<>();
	private boolean isRunning = true;

	private void initUI() {
		if (backPane == null) {
			this.setTitle("FileTransfer Helper");
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					FTView f = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
					if (ThreadMgr.threadSize() > 3) {
						int op = JOptionPane.showConfirmDialog(f, "�����������ڽ��У��Ƿ�Ҫ�˳���", "�ر�", JOptionPane.YES_NO_OPTION);
						if (op == JOptionPane.YES_OPTION) {
							LogoutMsg();
							f.isRunning = false;
							ThreadMgr.removeAll();
							System.exit(0);
						}
					} else {
						LogoutMsg();
						ThreadMgr.removeAll();
						f.isRunning = false;
						f.dispose();
						System.exit(0);
					}
				}

			});
			bg_pl = new JPanel();
			backPane = Box.createVerticalBox();
			backPane.add(Box.createVerticalStrut(3));

			// �ļ�ѡ�����
			fc_pl = new JPanel();
			file_tf = new JTextField();
			fc_btn = new JButton("���");
			fc_btn.addActionListener(this);
			file_tf.setText("��ѡ��һ���ļ�����");
			file_tf.setEditable(false);
			file_tf.setMinimumSize(new Dimension(300, 28));
			file_tf.setMaximumSize(new Dimension(200, 28));
			file_tf.setPreferredSize(new Dimension(300, 28));
			file_tf.setFont(new Font("΢���ź�", Font.PLAIN, 12));
			fc_pl.add(file_tf, SwingConstants.CENTER, 0);
			fc_pl.add(fc_btn, SwingConstants.CENTER, 1);
			backPane.add(fc_pl);
			backPane.add(Box.createVerticalStrut(3));

			// ���÷��ͷ�������
			recv_pl = new JPanel();
			recv_list = new JComboBox<String>();
			send_btn = new JButton("����");
			send_btn.setEnabled(false);
			send_btn.addActionListener(this);
			recv_list.setMinimumSize(new Dimension(300, 25));
			recv_list.setMaximumSize(new Dimension(300, 25));
			recv_list.setPreferredSize(new Dimension(300, 25));

			recv_list.setFont(new Font("Consolas", Font.BOLD, 14));
			recv_pl.add(recv_list);
			recv_pl.add(send_btn);
			backPane.add(recv_pl);
			backPane.add(Box.createVerticalStrut(3));

			// �ļ�������Ϣ��ʾ����
			info_ta = new JTextArea();
			info_sc = new JScrollPane(info_ta);
			tfinfo_pl = new JPanel();
			info_ta.setEditable(false);
			info_ta.setLineWrap(true);
			info_ta.setFont(new Font("����", Font.PLAIN, 12));
			info_sc.setMinimumSize(new Dimension(350, 150));
			info_sc.setMaximumSize(new Dimension(350, 150));
			info_sc.setPreferredSize(new Dimension(350, 150));
			info_sc.setAutoscrolls(true);
			tfinfo_pl.add(info_sc);
			backPane.add(tfinfo_pl);
			backPane.add(Box.createVerticalStrut(3));
		} else {
			backPane.removeAll();
			bg_pl.remove(backPane);
			this.remove(bg_pl);
			backPane = Box.createVerticalBox();
			backPane.add(Box.createVerticalStrut(3));
			backPane.add(fc_pl);
			backPane.add(Box.createVerticalStrut(3));
			backPane.add(recv_pl);
			backPane.add(Box.createVerticalStrut(3));
			backPane.add(tfinfo_pl);
			backPane.add(Box.createVerticalStrut(3));
			refreshUser();
			Set<String> keys = bars.keySet();
			for (String key : keys) {
				ProgressBar bar = bars.get(key);
				backPane.add(Box.createVerticalStrut(3));
				backPane.add(bar);
			}
		}

		bg_pl.add(backPane);
		bg_pl.setSize(backPane.getSize());
		this.add(bg_pl);
		this.setResizable(false);
		this.setVisible(true);
		this.validate();
		this.pack();
		this.setLocation((SW - this.getWidth()) / 2, (SH - this.getHeight()) / 2);
	}

	/**
	 * ����������Ϣ
	 */
	private void LogoutMsg() {
		Data d = null;
		try {
			d = new Data(Data.LOG_OUT, InetAddress.getLocalHost().getHostName());
			SendDataPkg sender = (SendDataPkg) ThreadMgr.getThread("SendDataPkg");
			if (sender == null) {
				sender = new SendDataPkg();
				ThreadMgr.add("SendDataPkg", sender);
			}
			sender.sendBrocast(d);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * ���ڴ��ⲿ���ã�ˢ���û�������
	 */
	public void refreshUser() {
		HashMap<String, User> users = UserMgr.getUsers();
		send_btn.setEnabled(true);
		Set<String> list = users.keySet();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				recv_list.removeAllItems();

				for (String item : list) {
					if (item != null) {
						recv_list.addItem(users.get(item).getAddr() + "-" + users.get(item).getHostName());
					}
				}
			}
		});
		if (users.size() == 0)
			send_btn.setEnabled(false);
	}

	/**
	 * ���ڵ����ļ�����ȷ�ϵĽ���
	 * 
	 * @param info
	 *            �����ļ������Լ���С��Ϣ
	 * @return true �����ļ�\nfalse �������ļ�
	 */
	public boolean recvFile(String[] info) {
		long length = Long.parseLong(info[1]);
		double size;
		double t;
		String ssize;
		if ((t = length * 10.0 / 1024 / 1024 / 1024) >= 1) {
			int i = (int) t;
			size = i * 1.0 / 10;
			ssize = size + "GB";
		} else if ((t = length * 10.0 / 1024 / 1024) >= 1) {
			int i = (int) t;
			size = i * 1.0 / 10;
			ssize = size + "MB";
		} else if ((t = length * 10.0 / 1024) >= 1) {
			int i = (int) t;
			size = i * 1.0 / 10;
			ssize = size + "KB";
		} else {
			ssize = length + "B";
		}
		String msg = "�ļ�����" + info[0] + "\n��С��" + ssize;
		int op = JOptionPane.showConfirmDialog(this, msg, "�����ļ�", JOptionPane.OK_CANCEL_OPTION);
		if (op == JOptionPane.OK_OPTION) {
			// ����
			appendText("ͬ������ļ�");
			return true;
		}
		return false;
	}

	public File saveFile(String filename) {
		JFileChooser choose = new JFileChooser();
		File file = null;
		choose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		choose.setSelectedFile(new File("%username%\\" + filename));
		int op = choose.showSaveDialog(this);
		if (op == JFileChooser.APPROVE_OPTION) { // ѡ������ļ���
			File temp = choose.getSelectedFile();
			if (temp.isDirectory()) // ���ѡ������ļ���
				file = new File(temp.getAbsolutePath() + "\\" + filename);
			else
				file = temp;
			if (file.exists()) { // ���ļ�����
				int ot = JOptionPane.showConfirmDialog(this, "�ļ��Ѵ��ڣ��Ƿ񸲸ǣ�", "����", JOptionPane.YES_NO_OPTION);
				if (ot == JOptionPane.YES_OPTION)
					return file;
				else {
					saveFile(filename);
				}
			} else { // ��ѡ����ļ�������
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return file;
			}
		} else { // ȡ�����ļ�ѡ���
			return null;
		}
		appendText("ѡ���ļ�����");
		return null;
	}

	/**
	 * ���һ������
	 * 
	 * @param text
	 */
	public void appendText(String text) {
		info_ta.append(text + "\n");
		info_ta.setCaretPosition(info_ta.getText().length());
	}

	/**
	 * �����ļ��������
	 */
	public void updateProgress(File file, String text) {
		ProgressBar bar = bars.get(file.getName());
		if (bar == null) {
			int type;
			if (text.contains("����")) {
				type = ProgressBar.SEND_FILE;
			} else
				type = ProgressBar.ACCEPT_FILE;
			bar = new ProgressBar(type, file, this.getWidth());
			bars.put(file.getName(), bar);
			backPane.add(Box.createVerticalStrut(3));
			backPane.add(bar);
		}
		bar.setLabelText(text);
		refreshUI();
	}

	// ˢ����������
	private void refreshUI() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
				view.validate();
				view.pack();
				view.setLocation((SW - view.getWidth()) / 2, (SH - view.getHeight()) / 2);
			}

		});
	}

	// ɾ��ָ����bar��
	public void cancelProgress(String progress) {
		ProgressBar bar = bars.get(progress);
		if (bar != null) {
			bars.remove(progress, bar);
			initUI();
		}
	}

	public boolean getRunning() {
		return isRunning;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == fc_btn) {
			JFileChooser choose = new JFileChooser();
			choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
			String path = file_tf.getText();
			if (path != null) {
				File f = new File(path);
				if (f.exists())
					choose.setCurrentDirectory(f);
			}
			int r = choose.showOpenDialog(this);
			if (r == JFileChooser.APPROVE_OPTION) {
				file_tf.setText(choose.getSelectedFile().getAbsolutePath());
			}
		}
		if (e.getSource() == send_btn) {
			String[] user = recv_list.getSelectedItem().toString().split("-");
			File file = new File(file_tf.getText());
			if (file.exists()) { // ����ļ����ڣ��򹹽����ݰ�����ѡ���û���������
				if (bars.containsKey(file.getName())) {
					ProgressBar bar = bars.get(file.getName());
					bar.requestFocus();
					return;
				}
				Data data = new Data(Data.SEND_FILE, file.getName() + ";" + file.length());
				try {
					InetAddress ip = InetAddress.getByName(user[0]);
					//����
					System.out.println(ip.getHostAddress()+", "+user[0]+", "+data.getTypeName()+", "+data.getData());
					FileSend send = new FileSend(ip, file);
					SendDataPkg sender = new SendDataPkg();
					sender.sendPacket(data, ip);
					appendText("�ȴ���" + UserMgr.getUser(ip.getHostAddress()).getHostName() + "ȷ��");
					ThreadMgr.add("FileSend" + file.getName(), send);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
			} else {
				appendText("����ʧ�ܣ��ļ������ڣ�");
			}
		}

		this.requestFocus();
	}

	public static void main(String[] args) {
		FTView view = new FTView();
		view.initUI();
		ThreadMgr.add(ThreadMgr.FTVIEW, view);
		ListenBC bgThread = new ListenBC();
		bgThread.init();
		ThreadMgr.add(ThreadMgr.BGTHREAD, bgThread);
		UserSeeker seeker = new UserSeeker();
		seeker.start();
	}

}

class ProgressBar extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ACCEPT_FILE = 1;
	public static final int SEND_FILE = 0;
	private JTextArea text;
	private JButton openFile, openDir, close;
	private String progress;
	private String progressPath;
	private File file;
	private FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
	private boolean isFinished = false;
	private int type;

	/**
	 * ����һ��������ʾ���ȵ����
	 * 
	 * @param file.getName()
	 * @param width
	 * @param type
	 */
	public ProgressBar(int type, File file, int width) {
		this.file = file;
		this.progress = file.getName();
		this.progressPath = file.getAbsolutePath();
		this.type = type;
		this.setMinimumSize(new Dimension(width - 30, 50));
		this.setMaximumSize(new Dimension(width - 30, 90));
		this.setPreferredSize(new Dimension(width - 30, 70));
		this.setBackground(Color.LIGHT_GRAY);
		text = new JTextArea();
		openFile = new JButton("ȡ��");
		openDir = new JButton("���ļ���");
		close = new JButton("�ر�");

		text.setPreferredSize(new Dimension(width - 30, 30));
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setFont(new Font("Consolas", Font.PLAIN, 12));
		text.setEditable(false);
		text.setOpaque(false);
		openFile.addActionListener(this);
		openDir.addActionListener(this);
		close.addActionListener(this);

		this.add(text);
		this.add(openFile);
		this.add(openDir);
		this.add(close);

	}

	public void setLabelText(String x) {
		text.setText(x);
		text.setToolTipText(x);
		if (x.equals("���ͳɹ�") && type == ProgressBar.SEND_FILE) {
			view.cancelProgress(progress);
			FileSend send = (FileSend) ThreadMgr.getThread("FileSend" + progress);
			send.shutdown();
			ThreadMgr.remove("FileSend" + progress);
			isFinished = true;
			return;
		}
		if (x.equals(file.getAbsolutePath())) {
			openFile.setText("���ļ�");
			isFinished = true;
			ThreadMgr.remove("FileRecv" + progress);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openFile) {
			if (openFile.getText().equals("ȡ��")) {
				int op = JOptionPane.showConfirmDialog(view, "�Ƿ�Ҫȡ������", "ȡ������", JOptionPane.YES_NO_OPTION);
				if (op == JOptionPane.YES_OPTION) {
					if (type == ProgressBar.ACCEPT_FILE) {
						FileRecv f = (FileRecv) ThreadMgr.getThread("FileRecv" + progress);
						f.shutdown();
						openFile.setEnabled(false);
						ThreadMgr.remove("FileRecv" + progress);
					} else {
						FileSend s = (FileSend) ThreadMgr.getThread("FileSend" + progress);
						s.shutdown();
						view.cancelProgress(progress);
						ThreadMgr.remove("FileSend" + progress);
					}
				}

			} else {
				try {
					if (file.exists())
						Runtime.getRuntime().exec("explore.exe " + progressPath);
					else
						view.appendText("�ļ������ڣ�");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (e.getSource() == openDir) {
			if (file.exists()) {
				String path = progressPath.substring(0, progressPath.lastIndexOf('\\'));
				try {
					Runtime.getRuntime().exec("explore.exe " + path);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				openDir.setEnabled(false);
				view.appendText("�ļ������ڣ�");
			}
		}
		if (e.getSource() == close) {
			if (isFinished)
				view.cancelProgress(progress);
			else {
				int op = JOptionPane.showConfirmDialog(view, "�Ƿ�Ҫ�ŵ�����̨����", "�رմ��䴰��", JOptionPane.YES_NO_OPTION);
				if (op == JOptionPane.YES_OPTION)
					view.cancelProgress(progress);
			}
		}
	}

}
