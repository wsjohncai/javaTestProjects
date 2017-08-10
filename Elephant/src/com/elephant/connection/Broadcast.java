package com.elephant.connection;

import java.util.Iterator;
import java.util.Vector;
import com.elephant.client.tools.GetIPs;
import com.elephant.client.tools.ProgramManager;
import com.elephant.common.Data;
import com.elephant.common.User;

public class Broadcast implements Runnable {

	private User me;

	public Broadcast(User u) {
		me = u;
	}

	// 向局域网广播本机的用户信息
	protected void acquireClients(String addr) {
		// 初始化请求数据包
		Data data = new Data(Data.REQUEST, me);
		// 发送请求
		Transmission transMsg = new Transmission(addr, data);
		transMsg.firstRequest();
	}

	@Override
	public void run() {
		Vector<String> IPs = null;
		GetIPs getIPs = new GetIPs();
		int timeCount = 0;
		getIPs.PingOnlineIPs();
		while (true) {
			try {
				if (timeCount % 5 == 0) {
					timeCount = 0;
					IPs = getIPs.getOnlineIPs();
					Iterator<String> it = IPs.iterator();
					while (it.hasNext()) {
						String ip = it.next();
						acquireClients(ip);
					}
					ProgramManager.refreshUserList();
				}
				Thread.sleep(1000);
				timeCount++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// public static void main(String[] o) {
	// User u = new User();
	// u.setPort(3333);
	// EleClient ele = new EleClient(u);
	// }

}
