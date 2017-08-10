package com.elephant.client.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class GetIPs {

	private Enumeration<NetworkInterface> netinterfaces = null; // 本机网络接口集合
	private Vector<String> onlineIPs = null;
	private Vector<String> allip = null;

	private void getNetworkInterfaces() {
		try {
			netinterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	// 返回一个本地IP的向量
	public Vector<String> getAllLocalIPs() {
		Enumeration<InetAddress> ips = null;
		getNetworkInterfaces();
		// 网络接口不为空时
		if (netinterfaces != null) {
			while (netinterfaces.hasMoreElements()) { // 获得所有的网络接口中的IP
				NetworkInterface netin = netinterfaces.nextElement();
				ips = netin.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress ip = ips.nextElement();
					if (ip != null && ip instanceof Inet4Address && !ip.equals(InetAddress.getLoopbackAddress())) { // 将所有的IPv4地址存在向量中
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

		// 使用命令行Ping
		try {
			ping = Runtime.getRuntime().exec("ping " + IP + " -w 3 -n 1 -l 16");
			cmdline = new BufferedReader(new InputStreamReader(ping.getInputStream()));
			String line = null;
			isOnline = true;
			while ((line = cmdline.readLine()) != null) {
				if (line.equals("请求超时。")) {
					isOnline = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isOnline;
	}

	// 获取所有局域网在线用户
	public void PingOnlineIPs() {

		// ping所有网段的用户
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
			// 对每一个网段的用户ping,若在线，则加入在线IP地址
			for (int i = start; i < end; i++) {
				tempip = host + i;
				// System.out.println(tempip);
				if (!tempip.equals(localIP) && checkOnline(tempip)) { // 不为本机IP且在线,则加入向量中
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
					System.out.println("在线IP: " + it.next());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
