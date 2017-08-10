package com.elephant.connection;

import java.io.*;
import java.net.*;
import com.elephant.client.tools.ProgramManager;
import com.elephant.client.tools.UserManager;
import com.elephant.client.view.ChattingUI;
import com.elephant.common.Data;
import com.elephant.common.User;

public class ReceiverCenter implements Runnable {

	private int port = 3333;
	private User user;

	public ReceiverCenter(User u, int port) {
		user = u;
		this.port = port;
	}

	public ReceiverCenter(User u) {
		user = u;
	}

	private void init() {

		// 回应数据包
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		ServerSocket server = null;
		Socket s = null;
		try {
			// 打开服务器，并在3333端口监听
			server = new ServerSocket(port);
			System.out.println("服务器启动");
			// 一旦有尝试连接，则进行处理
			while (true) {
				s = server.accept();
				ois = new ObjectInputStream(s.getInputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
				Data data = (Data) ois.readObject();
				System.out.println("Center: " + data.getDataType());

				User u = data.getUser();
				u.setAddr(s.getInetAddress().getHostAddress());
				UserManager.addUser(u);
				ProgramManager.refreshUserList();
				if (data.getDataType() == Data.REQUEST
						&& !UserManager.isConnected(s.getInetAddress().getHostAddress())) {
					Data answer = new Data(Data.ANSWER, user);
					oos.writeObject(answer);
					oos.flush();
					// 请求同个网络内的客户端信息
				} else if (data.getDataType() == Data.SENDFILE_REQUEST) { // 其他客户端请求发送文件
					// System.out.println("发送文件请求！");
					ChattingUI chatDialog = ProgramManager.getChatDialog(s.getInetAddress().getHostAddress());
					if (chatDialog == null) { // 对话框未打开时，直接创建新对话框
						chatDialog = new ChattingUI(UserManager.getUser(s.getInetAddress().getHostAddress()),
								user.getName());
						ProgramManager.putObject("C" + s.getInetAddress().getHostAddress(), chatDialog);
					}
					File directory = chatDialog.fileReceive(data.getText());
					if (directory != null) { // 文件接收方同意接收文件
						oos.writeObject(new Data(Data.RECEIVEFILE_CONFIRM, user));
						oos.flush();
						Transmission filetrans = new Transmission(directory, s, data);
						filetrans.start();
						ProgramManager.putObject("TF" + s.getInetAddress().getHostAddress(), filetrans);
					}
					// 不同意接收文件
					oos.writeObject(new Data(Data.ANSWER, user));
					oos.flush();
				} else if (data.getDataType() == Data.MESSAGE) { // 建立聊天进程
					Transmission tranMsg = new Transmission(s, data);
					tranMsg.start();
					ProgramManager.putObject("T" + s.getInetAddress().getHostAddress(), tranMsg);
					oos.writeObject(new Data(Data.ANSWER, user));
					oos.flush();
				} else if (data.getDataType() == Data.INFOCHANGE || data.getDataType() == Data.SHUTDOWN) {
					Transmission changeInfo = new Transmission(s.getInetAddress().getHostAddress(), data);
					changeInfo.stateChanged();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (ois != null)
					ois.close();
				if (server != null)
					server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		int errCount = 0;
		while (errCount < 5) {
			init();
			UserManager.removeAll();
			errCount++;
		}
		ProgramManager.removeAll();
		System.out.println("System ERROR!");
	}
}
