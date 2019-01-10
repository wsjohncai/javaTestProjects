package com.example.server;

import java.util.Calendar;

import static com.example.misc.Constants.SEP_CODE;
import static com.example.misc.Constants.SEP_VALUE;

public class InfoParser {
    private int code;
    private String info;

    InfoParser(String info) {
        String[] frags = info.split(SEP_CODE);
        code = Integer.parseInt(frags[0]);
        if (frags.length == 1) this.info = "";
        else this.info = frags[1];
    }

    int getCode() {
        return code;
    }

    String getMsg() {
        return info;
    }

    User getSrcUser() {
        User u = new User();
        if (info.contains(SEP_VALUE)) {
            String[] uin = info.split(SEP_VALUE);
            u.setId(uin[0]);
            u.setName(uin[1]);
        } else
            u.setId("");
        u.setLastCheckIn(Calendar.getInstance().getTimeInMillis());
        return u;
    }
}
