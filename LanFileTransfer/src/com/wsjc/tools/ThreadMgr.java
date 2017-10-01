package com.wsjc.tools;

import java.util.HashMap;
import java.util.Set;

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
			if (key.equals(BGTHREAD)) {
				ListenBC l = (ListenBC) ThreadMgr.getThread(key);
				l.setTask(false);
				l.shutdown();
			} else if (!key.equals(FTVIEW)) {
				BasicThread t = (BasicThread) mgr.get(key);
				t.shutdown();
			}
		}
		mgr = null;
	}

}
