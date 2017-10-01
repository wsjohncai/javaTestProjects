package com.wsjc.connection;

import java.io.IOException;
import java.net.*;
import com.wsjc.data.*;
import com.wsjc.tools.BasicThread;
import com.wsjc.tools.DataHandler;
import com.wsjc.tools.ThreadMgr;
import com.wsjc.view.FTView;

public class SendDataPkg extends BasicThread {

	private DatagramPacket packet;
	private FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
	private InetAddress bcip = null;
	private MulticastSocket sock = null;

	/**
	 * ����һ��������
	 */
	public SendDataPkg() {
	}

	public void shutdown() {
		if (sock != null) {
			try {
				sock.leaveGroup(bcip);
				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �ù㲥��ַ����һ�����ݰ�
	 */
	synchronized public void sendBrocast(Data data) {
		if (data == null)
			return;
		DataHandler handler = new DataHandler(data);
		byte[] d = handler.getSendData();
		try {
			bcip = InetAddress.getByName("224.0.5.5");
			packet = new DatagramPacket(d, d.length, bcip, 6667);
			if (sock == null) {
				sock = new MulticastSocket();
				sock.joinGroup(bcip);
				sock.setLoopbackMode(false);
				sock.setTimeToLive(1);
			}
			sock.send(packet);
			// ������
			// view.appendText("SendData�㲥�������ͣ�"+data.getType()+", "+data.getData());
		} catch (UnknownHostException e) {
			view.appendText("�㲥��ַ������");
			e.printStackTrace();
		} catch (IOException e) {
			shutdown();
			view.appendText("�������ͣ�" + data.getType() + "�㲥��Ϣδ����������");
			e.printStackTrace();
		} finally {
			ThreadMgr.remove("SendDataPkg");
		}
	}

	/**
	 * ���ض���ַ���͵������ݰ�
	 */
	public void sendPacket(Data data, InetAddress ip) {
		DatagramSocket s = null;
		try {
			byte[] d = new DataHandler(data).getSendData();
			packet = new DatagramPacket(d, d.length, ip, 6666);
			s = new DatagramSocket();
			s.send(packet);
			// ������
			view.appendText("SendData"+": ��"+ip.getHostAddress()+" ���ͣ����ͣ�" + data.getTypeName() + ",  " + data.getData());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (s != null)
					s.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
