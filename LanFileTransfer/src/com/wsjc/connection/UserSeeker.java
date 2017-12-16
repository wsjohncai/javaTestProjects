package com.wsjc.connection;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.wsjc.data.Data;
import com.wsjc.tools.ThreadMgr;
import com.wsjc.view.FTView;

public class UserSeeker extends Thread {

	private FTView view;
	private SendDataPkg sender = null;

	public UserSeeker() {
		view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
	}

	public void run() {
		try {
			sender = (SendDataPkg) ThreadMgr.getThread("SendDataPkg");
			while (view.getRunning()) {
				Data data = new Data(Data.REQUEST_CONNECT, InetAddress.getLocalHost().getHostName());
				if (sender == null) {
					sender = new SendDataPkg();
					ThreadMgr.add("SendDataPkg", sender);
				} 
				sender.sendBrocast(data);
				Thread.sleep(5000);
			}
		} catch (UnknownHostException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
