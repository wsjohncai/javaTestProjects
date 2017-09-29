package com.wsjc.connection;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.wsjc.data.Data;
import com.wsjc.tools.ThreadMgr;
import com.wsjc.view.FTView;

public class FileSend extends Thread {

	private InetAddress ip;
	private int port;
	private FileInputStream fis = null;
	private BufferedOutputStream bos = null;
	private File file;
	private Socket socket = null;
	private long totalLen;
	private FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);

	public FileSend(InetAddress ip, File file) {
		this.ip = ip;
		this.file = file;
		totalLen = file.length();
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		byte[] buf = new byte[2048];
		int read;
		long length = 0;
		int tper = -1, per = 0;
		String output = "正在发送："+file.getName()+" "+"已完成";
		try {
			view.appendText("准备发送："+file.getAbsolutePath());
			socket = new Socket(ip, port);
			if (socket != null) {
				view.appendText("正在发送："+file.getName()+"...");
				fis = new FileInputStream(file);
				bos = new BufferedOutputStream(socket.getOutputStream());
				while ((read = fis.read(buf)) != -1 && length < totalLen) {
					bos.write(buf);
					length += read;
					per = (int) (length * 100 / totalLen);
					if (tper != per) {
						view.updateProgress(file, output+per+"%");
					}
					bos.flush();
				}
				if(length == totalLen) {
					socket.shutdownOutput();
					view.appendText(java.util.Calendar.getInstance().toString()+"："+"传输完成："+file.getAbsolutePath());
					view.updateProgress(file, "发送成功");
				}
				else {
					view.appendText("文件传输出错，请重试！"+file.getAbsolutePath());
				}
				shutdown();
			}
		} catch (Exception e) {
			if(length<totalLen) {
			Data d = new Data(Data.ERROR);
			SendDataPkg send = new SendDataPkg(d, ip);
			send.sendPacket();
		}
			e.printStackTrace();
		}
	}

	public void shutdown() {
		try {
			if(socket!=null) {
				socket.close();
			}
			if(bos!=null) {
				bos.close();
			}
			if(fis!=null) {
				fis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
