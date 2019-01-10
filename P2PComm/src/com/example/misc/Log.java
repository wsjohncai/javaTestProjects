package com.example.misc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Log {
    public static void info(String text) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(new Date(Calendar.getInstance().getTimeInMillis())) + "  " + text);
    }
}
