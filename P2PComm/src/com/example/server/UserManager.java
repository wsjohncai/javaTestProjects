package com.example.server;

import java.util.Calendar;
import java.util.Vector;

import static com.example.misc.Constants.SEP_VALUE;

public class UserManager {
    private static final int TIMEOUT = 300000;
    static Vector<User> users = new Vector<>();

    static User hasUser(String id) {
        for (User u : users) {
            if (u.getId().equals(id))
                return u;
        }
        return null;
    }

    static int size() {
        return users.size();
    }

    //添加在线用户,如果用户存在,那么忽略
    static boolean addUser(User u) {
        return users.add(u);
    }

    static boolean removeUser(String id) {
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getId().equals(id)) {
                users.remove(i);
                return true;
            }
        }
        return false;
    }

    static String userList() {
        StringBuilder listBuilder = new StringBuilder();
        for (int i = 0; i < users.size(); ) {
            User u = users.get(i);
            long now = Calendar.getInstance().getTimeInMillis();
            if ((now - u.getLastCheckIn()) > TIMEOUT) {
                users.remove(i);
                continue;
            }
            listBuilder.append(u.getId()).append(SEP_VALUE).append(u.getName()).append(",");
            i++;
        }
        String list = listBuilder.toString();
        if (list.length() != 0)
            list = list.substring(0, list.length() - 1);
        return list;
    }
}
