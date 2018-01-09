package com.ds.tools;

public class TimeHelper {

    /**
     * 计算两个时间的时间差
     *
     * @param time1 较小的时间
     * @param time2 较大的时间
     * @return 返回两个时间以分钟计算的差，若格式不正确，返回-1
     */
    public static int subTime(String time1, String time2) {
        if (isTimeFormat(time1, time2)) {
            String[] t1 = time1.split(":");
            String[] t2 = time2.split(":");
            int h1 = Integer.parseInt(t1[0]);
            int h2 = Integer.parseInt(t2[0]);
            int m1 = Integer.parseInt(t1[1]);
            int m2 = Integer.parseInt(t2[1]);
            return (h2 - h1) * 60 + m2 - m1;
        } else
            return -1;
    }

    /**
     * 用于检查输入的所有文本是否都为指定时间格式（xx:xx）
     *
     * @param time 需要检查的时间文本
     * @return 所有输入符合条件，返回true。否则返回false
     */
    public static boolean isTimeFormat(String... time) {
        for (String t : time) {
            if (t == null)
                return false;
            if (!t.matches("\\d{1,2}:\\d{1,2}"))
                return false;
        }
        return true;
    }

    /**
     * 用于为time增加step分钟
     *
     * @param time 需要增加的初始时间
     * @param step 增加的分钟数
     * @return 如果增加成功，返回增加后时间；否则返回传入的time
     */
    public static String addTime(String time, int step) {
        if (isTimeFormat(time)) {
            String[] t = time.split(":");
            int h = Integer.parseInt(t[0]);
            int m = Integer.parseInt(t[1]);
            h += step / 60;
            m += step % 60;
            if (m >= 60) {
                m -= 60;
                h += 1;
            }
            return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m);
        }
        return time;
    }
}
