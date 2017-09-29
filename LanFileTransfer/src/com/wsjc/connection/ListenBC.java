package com.wsjc.connection;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.wsjc.tools.*;
import com.wsjc.view.FTView;
import com.wsjc.data.Data;
import com.wsjc.data.User;

public class ListenBC extends Thread {

	private DatagramSocket ds;
	private boolean task = true;
	private int filePort = 6667;
	private FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);

	public void shutdown() {
		task = false;
		ds.close();
	}

	private void handlerFileReq(InetAddress ip, Data data) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				SendDataPkg sender = null;
				Data tobeSend = null;
				// ���ܵ�Send���󣬽������ļ����Լ��ļ���Ϣ��
				String[] info = data.getData().split(";");
				boolean isAccept = view.recvFile(info);
				if (isAccept) {
					File file = view.saveFile(info[0]);
					if (file == null) { // �ļ�ѡ����Ч
						tobeSend = new Data(Data.REJECT_FILE);
					} else {
						// �����������ļ���ȷ�Ͽ������ȷ�ϣ�����6667�Ŷ˿ڽ���TCP��������
						// ����ʼ�������ɹ�����RECEIVE����������ERROR��
						FileRecv receiver = new FileRecv(file, Long.parseLong(info[1]), filePort++);
						ThreadMgr.add("FileRecv" + info[0], receiver);
						receiver.start();
						tobeSend = new Data(Data.RECEIVE_FILE, info[0] + ";" + (filePort - 1));
					}
				} else { // ���ܾ������ļ�������REJECT��
					tobeSend = new Data(Data.REJECT_FILE);
				}
				sender = new SendDataPkg(tobeSend, ip);
				sender.sendPacket();
			}

		});
		t.start();
	}

	@Override
	public void run() {
		DatagramPacket dp;
		byte[] data = new byte[1024];
		DataHandler dhandler;
		Data daObj;
		try {
			ds = new DatagramSocket(6666);
			while (task) {
				dp = new DatagramPacket(data, data.length);
				ds.receive(dp);
				dhandler = new DataHandler(dp.getData());
				daObj = dhandler.getDataObject();
				SendDataPkg sender = null;
				Data tobeSend = null;
				InetAddress ip = dp.getAddress();
				String host = ip.getHostAddress();
				String name = daObj.getData();
				
			
				switch (daObj.getType()) {
				case Data.REQUEST_CONNECT: // ���յ�������Request������Ӧ��ַ�������������Է����������б���
					tobeSend = new Data(Data.REQUEST_REPLY, InetAddress.getLocalHost().getHostName());
					sender = new SendDataPkg(tobeSend, ip);
					sender.sendPacket();
					if (!host.equals(InetAddress.getLocalHost().getHostAddress())) {
						UserMgr.add(host, new User(host, name));
						view.refreshUser();
						view.appendText("�û���" + name + "(" + host + ")" + " ����");
					}
					break;
				case Data.SEND_FILE:
					handlerFileReq(dp.getAddress(), daObj);
					break;
				case Data.RECEIVE_FILE:
					String[] s = daObj.getData().split(";");
					FileSend send = (FileSend) ThreadMgr.getThread("FileSend" + s[0]);
					send.setPort(Integer.parseInt(s[1]));
					send.start();
					break;
				case Data.REJECT_FILE:
					view.appendText("�Է������������͵��ļ���");
					break;
				case Data.LOG_OUT:
					UserMgr.remove(host);
					view.refreshUser();
					view.appendText("�û���" + name + "(" + host + ")" + " ����");
					break;
				case Data.ERROR:
					view.appendText("�������");
					break;
				case Data.REQUEST_REPLY:
					if (!host.equals(InetAddress.getLocalHost().getHostAddress())) {
						UserMgr.add(host, new User(host, name));
						view.refreshUser();
						view.appendText("�û���" + name + "(" + host + ")" + " ����");
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
