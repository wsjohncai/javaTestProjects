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
	 * 构造一个发送器
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
	 * 用广播地址发送一个数据包
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
			// 测试用
			// view.appendText("SendData广播包：类型："+data.getType()+", "+data.getData());
		} catch (UnknownHostException e) {
			view.appendText("广播地址不可用");
			e.printStackTrace();
		} catch (IOException e) {
			shutdown();
			view.appendText("数据类型：" + data.getType() + "广播信息未正常发出！");
			e.printStackTrace();
		} finally {
			ThreadMgr.remove("SendDataPkg");
		}
	}

	/**
	 * 向特定地址发送单个数据包
	 */
	public void sendPacket(Data data, InetAddress ip) {
		DatagramSocket s = null;
		try {
			byte[] d = new DataHandler(data).getSendData();
			packet = new DatagramPacket(d, d.length, ip, 6666);
			s = new DatagramSocket();
			s.send(packet);
			// 测试用
			view.appendText("SendData"+": 向"+ip.getHostAddress()+" 发送，类型：" + data.getTypeName() + ",  " + data.getData());
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
