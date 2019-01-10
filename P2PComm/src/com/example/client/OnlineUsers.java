package com.example.client;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsers {
    static List<User> onlineUsers = new ArrayList<>();

    static void updateList(List<User> list) {
        onlineUsers.clear();
        onlineUsers.addAll(list);
    }

    static User hasUser(String id) {
        for (User u : onlineUsers) {
            if (u.getId().equals(id))
                return u;
        }
        return null;
    }

}
