package com.wsjc.connection;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

import com.wsjc.data.Data;
import com.wsjc.tools.ThreadMgr;
import com.wsjc.view.FTView;

public class FileRecv extends Thread {

	private ServerSocket server = null;
	private Socket socket = null;
	private FileOutputStream fos = null;
	private BufferedInputStream bis = null;
	private int port;
	private FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
	private File file = null;
	private long totalLen = 0;

	public FileRecv(File file, long size, int port) {
		totalLen = size;
		this.file = file;
		this.port = port;
	}

	public void shutdown() {
		try {
			if (fos != null) {
				fos.close();
				if (file.exists()) {
					file.delete();
				}
			}
			if (bis != null)
				bis.close();
			if (socket != null) {
				socket.close();
			}
			if (server != null)
				server.close();
		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		long length = 0;
		int read = 0, tper = -1, per = 0;
		byte[] buf = null;
		String output = "正在接收：" + file.getName() + " " + "已完成";
		try {
			view.appendText("等待接收："+file.getName());
			server = new ServerSocket(port);
			socket = server.accept();
			view.appendText("正在接收："+file.getName()+"...");
			bis = new BufferedInputStream(socket.getInputStream());
			fos = new FileOutputStream(file);
			buf = new byte[2048];
			while ((read = bis.read(buf)) != -1 && length < totalLen) {
				fos.write(buf, 0, read);
				length += read;
				per = (int) (length * 100 / totalLen);
				if (per != tper) {
					tper = per;
					output += per + "%";
					view.updateProgress(file, output);
				}
				fos.flush();
			}
			if (length == totalLen) {
				socket.shutdownInput();
				view.appendText(java.util.Calendar.getInstance().toString()+"："+"传输完成："+file.getName());
				view.updateProgress(file, file.getAbsolutePath());
			}
			else {
				view.appendText("文件传输出错，请重试！"+file.getName());
			}
			shutdown();
		} catch (IOException e) {
			if(length<totalLen) {
				Data d = new Data(Data.ERROR);
				SendDataPkg send = new SendDataPkg(d, socket.getInetAddress());
				send.sendPacket();
			}
			e.printStackTrace();
		}
	}

}
