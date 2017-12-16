package com.wsjc.connection;

import com.wsjc.data.Data;
import com.wsjc.data.User;
import com.wsjc.tools.BasicThread;
import com.wsjc.tools.DataHandler;
import com.wsjc.tools.ThreadMgr;
import com.wsjc.tools.UserMgr;
import com.wsjc.view.FTView;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ListenBC extends BasicThread {

	private DatagramSocket unids;
	private MulticastSocket multids;
	private boolean task = false;
	private FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
	private String BCIP = "224.0.5.5";
	private int filePort = 6668;

	public void init() {
		if (!task) {
			task = true;
			uniCast();
			multiCast();
			view.appendText("服务器启动成功...");
		} else{
            task = false;
		    shutdown();
		    init();
        }
	}

	public void setTask(boolean task) {
		this.task = task;
	}

	synchronized public void shutdown() {
		try {
			if(unids!=null) {
                unids.close();
            }
			if(multids!=null) {
				multids.leaveGroup(InetAddress.getByName(BCIP));
				multids.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (task) {
			view.appendText("正在尝试重新启动服务器...");
		}
	}

	private void handlerFileReq(InetAddress ip, Data recData) {
		Thread t = new Thread(() -> {
            Data tobeSend;
            SendDataPkg sender;
            // 接受到Send包后，解析出文件名以及文件信息，
            String[] info = recData.getData().split(";");
            boolean isAccept = view.recvFile(info);
            if (isAccept) { // 弹出接收文件的确认框，若点击确认，
                File file = view.saveFile(info[0]);
                if (file == null) { // 文件选择无效，返回ERROR包
                    tobeSend = new Data(Data.ERROR);
                } else {// 返回确认接收的包，准备接收文件
                    FileRecv receiver = new FileRecv(file, Long.parseLong(info[1]), filePort++);
                    ThreadMgr.add("FileRecv" + info[0], receiver);
                    receiver.start();
                    tobeSend = new Data(Data.RECEIVE_FILE, info[0] + ";" + (filePort - 1));
                }
            } else { // 若拒绝接收文件，则发送REJECT包
                tobeSend = new Data(Data.REJECT_FILE);
            }
            sender = new SendDataPkg();
            sender.sendPacket(tobeSend, ip);
        });
		t.start();
	}

	/**
	 * 接收单播实现
	 */
	private void uniCast() {
		Thread t = new Thread(() -> {
            DatagramPacket dp;
            byte[] data = new byte[1024];
            DataHandler dhandler;
            Data daObj;
            try {
                unids = new DatagramSocket(6666,view.getSelecetedAddress());
                while (task) {
                    dp = new DatagramPacket(data, data.length);
                    unids.receive(dp);
                    dhandler = new DataHandler(dp.getData());
                    daObj = dhandler.getDataObject();
                    InetAddress ip = dp.getAddress();
                    String host = ip.getHostAddress();
                    String recData = daObj.getData();

                    if (!ip.equals(view.getSelecetedAddress()))
                        switch (daObj.getType()) {
                        case Data.SEND_FILE:
                            handlerFileReq(ip, daObj);
                            break;
                        case Data.RECEIVE_FILE:
                            String[] s = recData.split(";");
                            FileSend send = (FileSend) ThreadMgr.getThread("FileSend" + s[0]);
                            send.start();
                            break;
                        case Data.REJECT_FILE:
                            view.appendText("对方拒收了您发送的文件！");
                            break;
                        case Data.ERROR: // 传输出错后，关闭相关资源
                            view.appendText("传输出错！");
                            break;
                        case Data.REQUEST_REPLY:
                            if (!UserMgr.hasUser(host)) {
                                UserMgr.add(host, new User(host, recData));
                                view.refreshUser();
                                view.appendText("用户：" + recData + "(" + host + ")" + " 上线");
                            }
                            break;
                        }
                    // 测试用
                    view.appendText("uniCast接受到单播包：类型：" + ", " + daObj.getTypeName() + ", " + ip.getHostAddress()
                            + ", " + recData);
                }
            } catch (Exception e) {
                if (task)
                    shutdown();
                e.printStackTrace();
            }
        });
		t.start();
	}

	/**
	 * 用于接收多播的数据
	 */
	private void multiCast() {
		Thread t = new Thread(() -> {
            DatagramPacket dp;
            byte[] data = new byte[1024];
            DataHandler dhandler;
            Data daObj;
            try {
                multids = new MulticastSocket(6667);
                multids.setNetworkInterface(view.getSelectefNetworkInterface());
                multids.joinGroup(InetAddress.getByName(BCIP));
                multids.setLoopbackMode(false);
                while (task) {
                    dp = new DatagramPacket(data, data.length);
                    multids.receive(dp);
                    dhandler = new DataHandler(dp.getData());
                    daObj = dhandler.getDataObject();
                    SendDataPkg sender;
                    Data tobeSend;
                    InetAddress ip = dp.getAddress();
                    String host = ip.getHostAddress();
                    String revData = daObj.getData();
                    if (!ip.equals(view.getSelecetedAddress()))
                        switch (daObj.getType()) {
                        case Data.REQUEST_CONNECT: // 接收到包后发送Request包给对应地址的主机，并将对方加入在线列表中
                            if (!UserMgr.hasUser(host)) {
                                tobeSend = new Data(Data.REQUEST_REPLY, InetAddress.getLocalHost().getHostName());
                                sender = new SendDataPkg();
                                sender.sendPacket(tobeSend, ip);
                                UserMgr.add(host, new User(host, revData));
                                view.refreshUser();
                                view.appendText("用户：" + revData + "(" + host + ")" + " 上线");
                            }
                            break;
                        case Data.LOG_OUT:
                            UserMgr.remove(host);
                            view.refreshUser();
                            view.appendText("用户：" + revData + "(" + host + ")" + " 下线");
                            break;
                        }
                    // 测试用
                    // view.appendText(
                    // "mulCast接受到广播包：" + "类型, " + daObj.getType() +", "+ ip.getHostAddress() + ", "
                    // + revData);
                }
            } catch (IOException e) {
                if (task)
                    shutdown();
                e.printStackTrace();
            }
        });
		t.start();
	}

}
