package com.wsjc.connection;

import java.io.IOException;
import java.net.*;
import com.wsjc.data.*;
import com.wsjc.tools.DataHandler;

public class SendDataPkg implements Runnable {

	private Data data = null;
	private DatagramPacket packet;
	
	/**
	 * 构造一个广播器
	 */
	public SendDataPkg(Data data) {
		this.data = data;
	}
	
	/**
	 * 构造一个用于向特定地址发送的发送器
	 * @param data 需要发送的数据包
	 * @param ip 目标IP
	 */
	public SendDataPkg(Data data, InetAddress ip) {
			byte[] d = new DataHandler(data).getSendData();
			packet = new DatagramPacket(d, d.length, ip,6666);
	}
	
	/**
	 * 用广播地址发送一个数据包
	 */
	public void sendBrocast() {
		if(data == null) return;
		DataHandler handler = new DataHandler(data);
		byte[] d = handler.getSendData();
		try {
			packet = new DatagramPacket(d, d.length, InetAddress.getByName("255.255.255.255"),6666);
			Thread t = new Thread(this);
			t.start();
			
		} catch (UnknownHostException e) {
			System.out.println("广播地址不可用");
			e.printStackTrace();
		}
	}
	
	/**
	 * 向特定地址发送单个数据包
	 */
	public void sendPacket() {
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		DatagramSocket s = null;
		try {
			s = new DatagramSocket();
			s.send(packet);
		} catch (UnknownHostException e) {
			System.out.println("广播地址不可用");
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("创建本地发送端口失败");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(s!=null)
					s.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}

}
