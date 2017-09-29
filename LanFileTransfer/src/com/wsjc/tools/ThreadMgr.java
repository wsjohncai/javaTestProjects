package com.wsjc.tools;

import java.util.HashMap;
import java.util.Set;

import com.wsjc.connection.FileRecv;
import com.wsjc.connection.FileSend;
import com.wsjc.connection.ListenBC;

public class ThreadMgr {
	public static final String FTVIEW = "FTView";
	public static final String BGTHREAD = "BGThread";

	private static HashMap<String, Object> mgr = new HashMap<String, Object>();

	public static void add(String key, Object obj) {
		mgr.put(key, obj);
	}

	public static Object getThread(String key) {
		return mgr.get(key);
	}

	public static void remove(String key) {
		mgr.remove(key, mgr.get(key));
	}

	public static int threadSize() {
		return mgr.size();
	}
	
	public static void removeAll() {
		Set<String> threads = mgr.keySet();
		for (String key : threads) {
			if (key.contains("Recv")) {
				FileRecv f = (FileRecv) ThreadMgr.getThread(key);
				f.shutdown();
			}
			if (key.contains("Send")) {
				FileSend f = (FileSend) ThreadMgr.getThread(key);
				f.shutdown();
			}
			if (key.equals(BGTHREAD)) {
				ListenBC l = (ListenBC) ThreadMgr.getThread(key);
				l.shutdown();
			}
		}
		mgr = null;
	}

}
