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

		// ��Ӧ���ݰ�
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		ServerSocket server = null;
		Socket s = null;
		try {
			// �򿪷�����������3333�˿ڼ���
			server = new ServerSocket(port);
			System.out.println("����������");
			// һ���г������ӣ�����д���
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
					// ����ͬ�������ڵĿͻ�����Ϣ
				} else if (data.getDataType() == Data.SENDFILE_REQUEST) { // �����ͻ����������ļ�
					// System.out.println("�����ļ�����");
					ChattingUI chatDialog = ProgramManager.getChatDialog(s.getInetAddress().getHostAddress());
					if (chatDialog == null) { // �Ի���δ��ʱ��ֱ�Ӵ����¶Ի���
						chatDialog = new ChattingUI(UserManager.getUser(s.getInetAddress().getHostAddress()),
								user.getName());
						ProgramManager.putObject("C" + s.getInetAddress().getHostAddress(), chatDialog);
					}
					File directory = chatDialog.fileReceive(data.getText());
					if (directory != null) { // �ļ����շ�ͬ������ļ�
						oos.writeObject(new Data(Data.RECEIVEFILE_CONFIRM, user));
						oos.flush();
						Transmission filetrans = new Transmission(directory, s, data);
						filetrans.start();
						ProgramManager.putObject("TF" + s.getInetAddress().getHostAddress(), filetrans);
					}
					// ��ͬ������ļ�
					oos.writeObject(new Data(Data.ANSWER, user));
					oos.flush();
				} else if (data.getDataType() == Data.MESSAGE) { // �����������
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
