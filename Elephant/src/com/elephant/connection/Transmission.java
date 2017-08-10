package com.elephant.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import com.elephant.client.tools.*;
import com.elephant.client.view.ChattingUI;
import com.elephant.common.*;

public class Transmission extends Thread {

	private User u;
	private Socket socket;
	private int datatype = -1;
	private Data data = null;
	private File file = null;
	private boolean mannulInterrupted = false;
	private String addr;

	public void setMannulInterrupted(boolean mannulInterrupted) {
		this.mannulInterrupted = mannulInterrupted;
	}

	public Socket getSocket() {
		return socket;
	}

	public Transmission(String Addr, Data data) { // 接受与发送普通信息的构造方法
		this.u = UserManager.getUser(Addr);
		if (u == null)
			u = data.getUser();
		this.addr = Addr;
		this.datatype = data.getDataType();
		this.data = data;
	}

	public Transmission(Socket s, Data data) { // 接受与发送普通信息的构造方法
		this.addr = s.getInetAddress().getHostAddress();
		socket = s;
		this.u = UserManager.getUser(addr);
		if (u == null)
			u = data.getUser();
		this.datatype = data.getDataType();
		this.data = data;
	}

	public Transmission(File directory, Socket s, Data data) {// 接收文件的构造方法
		this.file = directory;
		socket = s;
		datatype = data.getDataType();
		this.data = data;
		this.u = data.getUser();
	}

	public Transmission(File file, User target) { // 发送文件的构造方法
		this.file = file;
		u = target;
		addr = u.getAddr();
	}

	public boolean startCon() {
		try {
			socket = new Socket(addr, u.getPort());
		} catch (Exception e) {
			System.out.println("尝试连接失败");
			e.printStackTrace();
		}
		if (socket != null)
			return true;
		return false;
	}

	public boolean sendData(Data data) { // 发送单个数据包
		System.out.println("sendData: " + data.getDataType());
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(data);
			oos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Data readData() { // 接收单个数据包
		Data d = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			d = (Data) ois.readObject();
			System.out.println("readData: " + d.getDataType());
		} catch (Exception e) {
			e.printStackTrace();
			return d;
		}
		return d;
	}

	public void stateChanged() {
		if (data.getDataType() == Data.INFOCHANGE) { // 其他用户发来的信息更改包
			User newu = data.getUser();
			newu.setAddr(addr);
			UserManager.addUser(newu);
		} else if (data.getDataType() == Data.SHUTDOWN) { // 其他用户下线通知
			UserManager.removeUser(addr);
			ChattingUI chat = ProgramManager.getChatDialog(addr);
			if (chat != null)
				chat.appendText(null, "用户已下线！无法发送消息！\n\r");
		}
		ProgramManager.refreshUserList();
	}

	public void firstRequest() { // 向所有局域网用户发送请求
		Data d;
		try {
			startCon();
			sendData(data);
			d = readData();
			if (d.getDataType() == Data.ANSWER) { // 接收应答消息后处理
				User u = d.getUser();
				u.setAddr(addr);
				UserManager.addUser(u);
			}
		} catch (Exception e) {
			if (UserManager.isConnected(addr))
				UserManager.removeUser(addr);
			e.printStackTrace();
		}
	}

	private void transMsg() { // 用于接收信息
		Data msgdata;
		ChattingUI chatDialog = null;
		try {
			while (true) {
				msgdata = readData();
				if (msgdata.getDataType() == Data.MESSAGE) { // 接受消息
					mannulInterrupted = false;
					chatDialog = ProgramManager.getChatDialog(addr);
					if (chatDialog == null) { // 如果对话框不存在，创建新对话框
						chatDialog = new ChattingUI(u, UserManager.getUser("127.0.0.1").getName());
						ProgramManager.putObject("C" + u.getAddr(), chatDialog);
					}
					chatDialog.appendText(msgdata.getUser().getName(), msgdata.getText()); // 打印数据
				}
				if (msgdata.getDataType() == Data.MANUALINTERUPTED) {
					mannulInterrupted = false;
					Transmission t = (Transmission) ProgramManager.getObject("TF" + u.getAddr());
					t.setMannulInterrupted(true);
					Socket s = t.getSocket();
					s.close();
				}
				if (msgdata.getDataType() == Data.INFOCHANGE || msgdata.getDataType() == Data.SHUTDOWN) {
					mannulInterrupted = false;
					data = msgdata;
					stateChanged();
				}
				if (msgdata.getDataType() == Data.ENDCON) {
					if (mannulInterrupted)
						socket.close();
					mannulInterrupted = true;
				}
			}
		} catch (Exception e) {
			try {
				socket.close();
				ProgramManager.removeObject("T" + addr);
				if (!mannulInterrupted)
					chatDialog.breakCon();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void receiveFile() { // 接收文件的进程方法
		String[] string = data.getText().split(":");
		long filelength = Long.parseLong(string[1]);
		DataInputStream datain = null;
		FileOutputStream store = null;
		byte[] buf = null;
		long read = 0;
		ChattingUI chat = null;
		int BUFSIZE = 1024;
		try {
			// 初始化界面和缓存区
			chat = ProgramManager.getChatDialog(socket.getInetAddress().getHostAddress());
			datain = new DataInputStream(socket.getInputStream());
			store = new FileOutputStream(file);
			buf = new byte[BUFSIZE];
			chat.shutfiletrans.setText("取消");
			chat.shutfiletrans.setEnabled(true);
			String filename = file.getAbsolutePath();
			// 文件接收
			while (true) {
				int temp = datain.read(buf, 0, BUFSIZE);
				if (temp == -1) {
					if (read == filelength) {
						chat.shutfiletrans.setText("");
						chat.shutfiletrans.setEnabled(false);
						chat.filetrans.setText("文件接收完成: " + file.getAbsolutePath());
						sendData(new Data(Data.RECEIVEFILE_CONFIRM, data.getUser()));
					}
					break;
				}
				read += temp;
				store.write(buf, 0, temp);
				int percent = (int) (read * 100 / filelength);
				chat.filetrans.setText(filename + "：" + percent + "%已接收");
				store.flush();
			}
		} catch (Exception e) {
			chat.shutfiletrans.setText("");
			chat.shutfiletrans.setEnabled(false);
			try {
				if (store != null)
					store.close();
				datain.close();
				if (read < filelength) {
					if (!mannulInterrupted)
						chat.fileTransError();
					else {
						chat.filetrans.setText("文件传输已取消");
					}
					file.delete();
				}
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void sendFile() { // 发送文件
		// 创建请求数据包
		Data d = new Data(Data.SENDFILE_REQUEST, UserManager.getUser("127.0.0.1"));
		String fileinfo = file.getName() + ":" + file.length();
		d.setText(fileinfo);
		FileInputStream fis = null;
		DataOutputStream dos = null;
		long filelength = 0;
		long read = 0;
		int BUFSIZE = 1024;
		ChattingUI chat = ProgramManager.getChatDialog(u.getAddr());
		try {
			startCon();
			sendData(d);
			if (readData().getDataType() != Data.RECEIVEFILE_CONFIRM)
				return;
			chat.shutfiletrans.setText("取消");
			chat.shutfiletrans.setEnabled(true);
			fis = new FileInputStream(file);
			dos = new DataOutputStream(socket.getOutputStream());
			byte[] buf = new byte[BUFSIZE];
			filelength = file.length();
			read = 0;
			// 文件正式开始发送
			while (true) {
				int temp = fis.read(buf, 0, BUFSIZE);
				if (temp == -1) {
					if(read == filelength) {
						chat.filetrans.setText(file.getAbsolutePath() + "发送完成！");
						chat.shutfiletrans.setText("");
						chat.shutfiletrans.setEnabled(false);
					}
					if (readData().getDataType() != Data.RECEIVEFILE_CONFIRM)
						System.out.println("文件接收成功");
						break;
				}
				read += temp;
				dos.write(buf, 0, temp);
				int per = (int) (read * 100 / filelength);
				// System.out.println(per);
				chat.filetrans.setText("文件:"+file.getAbsolutePath()+"已发送：" + per + "%");
				dos.flush();
			}
		} catch (Exception e) {
			chat.shutfiletrans.setText("");
			chat.shutfiletrans.setEnabled(false);
			if(read < filelength) {
				if (!mannulInterrupted)
					chat.fileTransError();
				else {
					chat.filetrans.setText("文件传输已取消");
				}
			}
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
				if (fis != null)
					fis.close();
				if (dos != null)
					dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		if (datatype == Data.MESSAGE) {
			transMsg();
		} else if (datatype == Data.SENDFILE_REQUEST) {
			receiveFile();
		} else if (data == null && file != null) {
			sendFile();
		}
	}

	class InvalidUserException extends Exception {

		private static final long serialVersionUID = 1L;

		@Override
		public void printStackTrace() {
			System.out.println("Invalid User Connection");
		}
	}

}
