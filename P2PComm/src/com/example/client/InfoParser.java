package com.example.client;

import java.util.ArrayList;
import java.util.List;

import static com.example.misc.Constants.*;

class InfoParser {
    private int code;
    private String msg;

    InfoParser(String info) {
        String[] ins = info.split(SEP_CODE);
        code = Integer.parseInt(ins[0]);
        if (ins.length == 1) msg = "";
        else msg = ins[1];
    }

    int getCode() {
        return code;
    }

    String getMsg() {
        return msg;
    }

    List<User> getUserList() {
        List<User> list = new ArrayList<>();
        if (msg.contains(",")) {
            String[] uins = msg.split(",");
            for (String us : uins) {
                String[] in = us.split(SEP_VALUE);
                User u = new User();
                u.setId(in[0]);
                u.setName(in[1]);
                list.add(u);
            }
        } else if (!msg.isEmpty()) {
            User u = new User();
            String[] in = msg.split(SEP_VALUE);
            u.setId(in[0]);
            u.setName(in[1]);
            list.add(u);
        }
        return list;
    }

    String getDstId() {
        switch (code) {
            case COMM_REQ_FEEDBACK:
                return msg.split(SEP_VALUE)[0];
            case COMM_MSG:
                return msg.split(SEP_VALUE)[2];
            case COMM_USER_OFF:
                return msg;
            default:
                return "";
        }
    }

    String getDstAddress() {
        if (msg.contains(SEP_VALUE)) {
            String[] ins = msg.split(SEP_VALUE);
            return ins[1];
        } else
            return msg;
    }

    String getMsgId() {
        return msg.split(SEP_VALUE)[0];
    }

    String getSrcId() {
        switch (code) {
            case COMM_MSG:
                return msg.split(SEP_VALUE)[1];
            case COMM_REQUEST:
                return msg.split(SEP_VALUE)[0];
            default:
                return "";
        }
    }

    String getCommMsg() {
        return msg.split(SEP_VALUE)[3];
    }
}
