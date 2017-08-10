package com.elephant.client.tools;

import java.util.HashMap;
import com.elephant.common.User;

public class UserManager {
	private static HashMap<String, User> usermgr = new HashMap<String, User>();

	public static User addUser(User u) {
		return usermgr.put(u.getAddr(), u);
	}

	public static User getUser(String addr) {
		return usermgr.get(addr);
	}

	public static boolean isConnected(String addr) {
		if (usermgr.get(addr) != null)
			return true;
		return false;
	}

	public static void removeUser(String addr) {
		usermgr.remove(addr);
	}

	public static void removeAll() {
		usermgr = new HashMap<String, User>();
	}

	public static HashMap<String, User> getUserMap() {
		return usermgr;

	}

}
