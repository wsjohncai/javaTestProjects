package com.wsjc.connection;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.wsjc.data.Data;
import com.wsjc.tools.BasicThread;
import com.wsjc.tools.ThreadMgr;
import com.wsjc.view.FTView;

public class FileSend extends BasicThread {

	private InetAddress ip;
	private int port;
	private FileInputStream fis = null;
	private BufferedOutputStream bos = null;
	private File file;
	private Socket socket = null;
	private long totalLen;
	private FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
	private boolean isShutdown = false;

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

		String output = "正在发送：" + file.getName() + " " + "已完成";
		try {
			socket = new Socket(ip, port);
			if (socket != null) {
				view.appendText("确认接收：" + file.getName() + "...");
				fis = new FileInputStream(file);
				bos = new BufferedOutputStream(socket.getOutputStream());
				while ((read = fis.read(buf)) != -1 && length < totalLen) {
					bos.write(buf);
					length += read;
					per = (int) (length * 100 / totalLen);
					if (tper != per) {
						view.updateProgress(file, output + per + "%");
					}
					bos.flush();
				}
				if (length == totalLen) {
					view.appendText(
							java.util.Calendar.getInstance().toString() + "：" + "传输完成：" + file.getAbsolutePath());
					view.updateProgress(file, "发送成功");
				} else {
					view.appendText("文件传输出错，请重试！" + file.getAbsolutePath());
				}
			} else {
				view.appendText("文件端口连接失败！");
				SendDataPkg send = new SendDataPkg();
				send.sendPacket(new Data(Data.ERROR, "FileRecv" + file.getName()), ip);
			}

			shutdown();
		} catch (Exception e) {
			if (length < totalLen) {
				SendDataPkg send = new SendDataPkg();
				send.sendPacket(new Data(Data.ERROR, "FileRecv" + file.getName()), ip);
			}
			shutdown();
			e.printStackTrace();
		}
	}

	public void shutdown() {
		if (!isShutdown)
			try {
				if (socket != null) {
					socket.shutdownOutput();
					socket.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (fis != null) {
					fis.close();
				}
				isShutdown = true;
				if (!view.getRunning())
					ThreadMgr.remove("FileSend" + file.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

}
