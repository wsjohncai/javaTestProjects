package com.wsjc.connection;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import com.wsjc.tools.*;
import com.wsjc.view.FTView;
import com.wsjc.data.Data;
import com.wsjc.data.User;

public class ListenBC extends BasicThread {

	private DatagramSocket unids;
	private MulticastSocket multids;
	private boolean task = false;
	private FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
	private String BCIP = "224.0.5.5";
	private int filePort = 6668;

	public void init() {
		if (!task) {
			task = true;
			uniCast();
			multiCast();
			view.appendText("�����������ɹ�...");
		}
	}

	public void setTask(boolean task) {
		this.task = task;
	}

	synchronized public void shutdown() {
		try {
			unids.close();
			multids.leaveGroup(InetAddress.getByName(BCIP));
			multids.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (task) {
			view.appendText("���ڳ�����������������...");
		}
	}

	private void handlerFileReq(InetAddress ip, Data recData) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				Data tobeSend = null;
				SendDataPkg sender = null;
				// ���ܵ�Send���󣬽������ļ����Լ��ļ���Ϣ��
				String[] info = recData.getData().split(";");
				boolean isAccept = view.recvFile(info);
				if (isAccept) { // ���������ļ���ȷ�Ͽ������ȷ�ϣ�
					File file = view.saveFile(info[0]);
					if (file == null) { // �ļ�ѡ����Ч������ERROR��
						tobeSend = new Data(Data.ERROR);
					} else {// ����ȷ�Ͻ��յİ���׼�������ļ�
						FileRecv receiver = new FileRecv(file, Long.parseLong(info[1]), filePort++);
						ThreadMgr.add("FileRecv" + info[0], receiver);
						receiver.start();
						tobeSend = new Data(Data.RECEIVE_FILE, info[0] + ";" + (filePort - 1));
					}
				} else { // ���ܾ������ļ�������REJECT��
					tobeSend = new Data(Data.REJECT_FILE);
				}
				sender = new SendDataPkg();
				sender.sendPacket(tobeSend, ip);
			}

		});
		t.start();
	}

	/**
	 * ���յ���ʵ��
	 */
	private void uniCast() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				DatagramPacket dp;
				byte[] data = new byte[1024];
				DataHandler dhandler;
				Data daObj;
				try {
					unids = new DatagramSocket(6666);
					while (task) {
						dp = new DatagramPacket(data, data.length);
						unids.receive(dp);
						dhandler = new DataHandler(dp.getData());
						daObj = dhandler.getDataObject();
						InetAddress ip = dp.getAddress();
						String host = ip.getHostAddress();
						String recData = daObj.getData();

						if (!ip.equals(InetAddress.getLocalHost()))
							switch (daObj.getType()) {
							case Data.SEND_FILE:
								handlerFileReq(ip, daObj);
								break;
							case Data.RECEIVE_FILE:
								String[] s = recData.split(";");
								FileSend send = (FileSend) ThreadMgr.getThread("FileSend" + s[0]);
								send.start();
								break;
							case Data.REJECT_FILE:
								view.appendText("�Է������������͵��ļ���");
								break;
							case Data.ERROR: // �������󣬹ر������Դ
								view.appendText("�������");
								break;
							case Data.REQUEST_REPLY:
								if (!UserMgr.hasUser(host)) {
									UserMgr.add(host, new User(host, recData));
									view.refreshUser();
									view.appendText("�û���" + recData + "(" + host + ")" + " ����");
								}
								break;
							}
						// ������
						view.appendText("uniCast���ܵ������������ͣ�" + ", " + daObj.getTypeName() + ", " + ip.getHostAddress()
								+ ", " + recData);
					}
				} catch (Exception e) {
					if (task)
						shutdown();
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	/**
	 * ���ڽ��նಥ������
	 */
	private void multiCast() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				DatagramPacket dp;
				byte[] data = new byte[1024];
				DataHandler dhandler;
				Data daObj;
				try {
					multids = new MulticastSocket(6667);
					multids.joinGroup(InetAddress.getByName(BCIP));
					multids.setLoopbackMode(false);
					while (task) {
						dp = new DatagramPacket(data, data.length);
						multids.receive(dp);
						dhandler = new DataHandler(dp.getData());
						daObj = dhandler.getDataObject();
						SendDataPkg sender = null;
						Data tobeSend = null;
						InetAddress ip = dp.getAddress();
						String host = ip.getHostAddress();
						String revData = daObj.getData();
						if (!ip.equals(InetAddress.getLocalHost()))
							switch (daObj.getType()) {
							case Data.REQUEST_CONNECT: // ���յ�������Request������Ӧ��ַ�������������Է����������б���
								if (!UserMgr.hasUser(host)) {
									tobeSend = new Data(Data.REQUEST_REPLY, InetAddress.getLocalHost().getHostName());
									sender = new SendDataPkg();
									sender.sendPacket(tobeSend, ip);
									UserMgr.add(host, new User(host, revData));
									view.refreshUser();
									view.appendText("�û���" + revData + "(" + host + ")" + " ����");
								}
								break;
							case Data.LOG_OUT:
								UserMgr.remove(host);
								view.refreshUser();
								view.appendText("�û���" + revData + "(" + host + ")" + " ����");
								break;
							}
						// ������
						// view.appendText(
						// "mulCast���ܵ��㲥����" + "����, " + daObj.getType() +", "+ ip.getHostAddress() + ", "
						// + revData);
					}
				} catch (IOException e) {
					if (task)
						shutdown();
					e.printStackTrace();
				}
			}

		});
		t.start();
	}

}
