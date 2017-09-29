package com.wsjc.connection;

import java.io.IOException;
import java.net.*;
import com.wsjc.data.*;
import com.wsjc.tools.DataHandler;

public class SendDataPkg implements Runnable {

	private Data data = null;
	private DatagramPacket packet;
	
	/**
	 * ����һ���㲥��
	 */
	public SendDataPkg(Data data) {
		this.data = data;
	}
	
	/**
	 * ����һ���������ض���ַ���͵ķ�����
	 * @param data ��Ҫ���͵����ݰ�
	 * @param ip Ŀ��IP
	 */
	public SendDataPkg(Data data, InetAddress ip) {
			byte[] d = new DataHandler(data).getSendData();
			packet = new DatagramPacket(d, d.length, ip,6666);
	}
	
	/**
	 * �ù㲥��ַ����һ�����ݰ�
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
			System.out.println("�㲥��ַ������");
			e.printStackTrace();
		}
	}
	
	/**
	 * ���ض���ַ���͵������ݰ�
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
			System.out.println("�㲥��ַ������");
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("�������ط��Ͷ˿�ʧ��");
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
