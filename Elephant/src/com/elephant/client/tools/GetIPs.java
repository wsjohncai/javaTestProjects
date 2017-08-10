package com.elephant.client.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class GetIPs {

	private Enumeration<NetworkInterface> netinterfaces = null; // ��������ӿڼ���
	private Vector<String> onlineIPs = null;
	private Vector<String> allip = null;

	private void getNetworkInterfaces() {
		try {
			netinterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	// ����һ������IP������
	public Vector<String> getAllLocalIPs() {
		Enumeration<InetAddress> ips = null;
		getNetworkInterfaces();
		// ����ӿڲ�Ϊ��ʱ
		if (netinterfaces != null) {
			while (netinterfaces.hasMoreElements()) { // ������е�����ӿ��е�IP
				NetworkInterface netin = netinterfaces.nextElement();
				ips = netin.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress ip = ips.nextElement();
					if (ip != null && ip instanceof Inet4Address && !ip.equals(InetAddress.getLoopbackAddress())) { // �����е�IPv4��ַ����������
						if (allip == null)
							allip = new Vector<String>();
						allip.addElement(ip.getHostAddress());
					}
				}
			}
		}
		return allip;
	}

	private boolean checkOnline(String IP) {
		boolean isOnline = false;
		BufferedReader cmdline = null;
		Process ping = null;

		// ʹ��������Ping
		try {
			ping = Runtime.getRuntime().exec("ping " + IP + " -w 3 -n 1 -l 16");
			cmdline = new BufferedReader(new InputStreamReader(ping.getInputStream()));
			String line = null;
			isOnline = true;
			while ((line = cmdline.readLine()) != null) {
				if (line.equals("����ʱ��")) {
					isOnline = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isOnline;
	}

	// ��ȡ���о����������û�
	public void PingOnlineIPs() {

		// ping�������ε��û�
		allip = new Vector<String>();
		allip = getAllLocalIPs();
		onlineIPs = new Vector<String>();
		Iterator<String> it = allip.iterator();
		while (it.hasNext()) {
			PingIP pingOnline = new PingIP(it.next());
		}
	}

	public Vector<String> getOnlineIPs() {
		return onlineIPs;
	}

	private class PingIP implements Runnable {
		private static final int MAX_THREAD = 10;
		private int threadCount = 0;
		private String localIP = null;
		private int start, end;

		public PingIP(String localIP) {
			this.localIP = localIP;
			splitLoad();
		}

		private PingIP(String localIP, int start, int end) {
			this.localIP = localIP;
			this.start = start;
			this.end = end;
		}

		private void splitLoad() {
			int ipFields = allip.size();
			if (ipFields > 0) {
				int split = MAX_THREAD / ipFields;
				if (split <= 0)
					split = 1;
				int i = 1;
				int s = 1;
				int e = 255 / split;
				PingIP task = null;
				// ExecutorService exec = Executors.newFixedThreadPool(split);
				while (threadCount < MAX_THREAD && split > 0) {
					if (split == 1 || (MAX_THREAD - threadCount) < split)
						task = new PingIP(localIP, s, 255);
					else {
						task = new PingIP(localIP, s, e * i);
						s = e * i;
					}
					Thread t = new Thread(task);
					t.start();
					try {
						Thread.sleep(10);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					i++;
					split--;
					threadCount += 1;
				}
			}
			// exec.shutdown();

		}

		@Override
		public void run() {
			int pos = localIP.lastIndexOf(".");
			String host = localIP.substring(0, pos + 1);
			String tempip;
			// ��ÿһ�����ε��û�ping,�����ߣ����������IP��ַ
			for (int i = start; i < end; i++) {
				tempip = host + i;
				// System.out.println(tempip);
				if (!tempip.equals(localIP) && checkOnline(tempip)) { // ��Ϊ����IP������,�����������
					onlineIPs.addElement(tempip);
				}
			}
			threadCount -= 1;
		}
	}

	public static void main(String[] o) {
		GetIPs g = new GetIPs();
		g.PingOnlineIPs();
		while (true) {
			try {
				Thread.sleep(5000);
				Iterator<String> it = g.onlineIPs.iterator();
				while (it.hasNext()) {
					System.out.println("����IP: " + it.next());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
