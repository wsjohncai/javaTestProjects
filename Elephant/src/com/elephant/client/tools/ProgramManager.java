package com.elephant.client.tools;

import java.util.HashMap;
import java.util.Iterator;

import com.elephant.client.view.ChattingUI;
import com.elephant.client.view.ListUI;

public class ProgramManager {
	public static HashMap<String, Object> PROGRAMS = new HashMap<String, Object>();

	public static void putObject(String name, Object obj) {
		PROGRAMS.put(name, obj);
	}

	public static Object getObject(String name) {
		return PROGRAMS.get(name);
	}

	public static void removeObject(String name) {
		PROGRAMS.remove(name);
	}

	public static void removeAll() {
		Iterator<String> it = PROGRAMS.keySet().iterator();
		while (it.hasNext()) {
			PROGRAMS.remove(it.next());
		}
	}

	public static void refreshUserList() {
		ListUI list = (ListUI) ProgramManager.getObject("ListUI");
		list.userList();
	}

	public static ChattingUI getChatDialog(String addr) {
		return (ChattingUI) ProgramManager.getObject("C" + addr);
	}

}
