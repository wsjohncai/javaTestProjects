package com.wsjc.tools;

import java.util.HashMap;
import com.wsjc.data.User;

public class UserMgr {
	private static HashMap<String, User> mgr = new HashMap<String, User>();

	public static void add(String key, User obj) {
		mgr.put(key, obj);
	}

	public static void remove(String key) {
		mgr.remove(key, mgr.get(key));
	}
	
	public static User getUser(String ip) {
		return mgr.get(ip);
	}
	
	public static boolean hasUser(String key) {
		return mgr.containsKey(key);
	}
	
	public static HashMap<String, User> getUsers() {
		return mgr;
	}
}
