package com.ds.tools;

import com.ds.common.JCB;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.Vector;

public class DataModel extends AbstractTableModel {

    public static final int TABLE_NOT_START = 3;
    public static final int TABLE_COMMITED = 0;
    public static final int TABLE_RUNNING = 1;
    public static final int TABLE_FINISHED = 2;

    private String[] colNames;
    private Vector<String> columnNames = new Vector<>();
    private static Vector<JCB>[] queue = new Vector[4];
    private int table_type;

    public DataModel(int table_type) {
        switch (table_type) { //根据不同类型的JTable初始化列名
            case TABLE_NOT_START:
                colNames = new String[]{"作业名", "提交时间", "运行时间", "优先级", "状态"};
                break;
            case TABLE_COMMITED:
                colNames = new String[]{"作业名", "提交时间", "优先级", "服务需时"};
                break;
            case TABLE_RUNNING:
                colNames = new String[]{"作业名", "服务需时", "优先级"};
                break;
            case TABLE_FINISHED:
                colNames = new String[]{"作业名", "开始时间", "结束时间", "周转时间", "带权周转时间"};
                break;
            default:
                break;
        }
        this.table_type = table_type;
        columnNames.addAll(Arrays.asList(colNames));
        queue[table_type] = new Vector();
        if (table_type == TABLE_NOT_START)
            initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (queue[table_type] != null && queue[table_type].size() > 0)
            queue[table_type].removeAllElements();
        else if (queue[table_type] == null)
            queue[table_type] = new Vector<>();
        queue[table_type].add(new JCB("JOB1", 40, "10:00", 5));
        queue[table_type].add(new JCB("JOB2", 30, "10:20", 3));
        queue[table_type].add(new JCB("JOB3", 50, "10:30", 4));
        queue[table_type].add(new JCB("JOB4", 20, "10:50", 6));
    }

    /**
     * 将数据重置为初始状态
     */
    public void setDataToDefault() {
        switch (table_type) { //根据不同类型的JTable初始化列名
            case TABLE_NOT_START:
                for (JCB j : queue[table_type]) {
                    j.setLeft_svc_time(j.getSvc_time());
                    j.setStart_time("");
                    j.setStatus(JCB.NOT_EXIST);
                    j.setStop_time("");
                }
                break;
            case TABLE_COMMITED:
                queue[table_type].removeAllElements();
                break;
            case TABLE_RUNNING:
                queue[table_type].removeAllElements();
                break;
            case TABLE_FINISHED:
                queue[table_type].removeAllElements();
                break;
            default:
                break;
        }
    }

    public void addJob(JCB job) {
        if (queue[table_type] == null)
            queue[table_type] = new Vector<>();
        queue[table_type].add(job);
    }

    public void deleteJob(int idx) {
        queue[table_type].remove(idx);
    }

    public static Vector<JCB> getQueue(int type) {
        return queue[type];
    }

    @Override
    public int getRowCount() {
        return queue[table_type].size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames.get(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        JCB job = queue[table_type].get(rowIndex);
        switch (columnIndex) {
            case 0:
                return job.getName();
            case 1:
                if (table_type == TABLE_NOT_START || table_type == TABLE_COMMITED)
                    return job.getSummit_time();
                else if (table_type == TABLE_RUNNING)
                    return job.getLeft_svc_time();
                else
                    return job.getStart_time();
            case 2:
                if (table_type == TABLE_RUNNING || table_type == TABLE_COMMITED)
                    return job.getPriority();
                else if (table_type == TABLE_NOT_START)
                    return job.getSvc_time();
                else
                    return job.getStop_time();
            case 3:
                if (table_type == TABLE_NOT_START)
                    return job.getPriority();
                else if (table_type == TABLE_FINISHED) {
                    int time = TimeHelper.subTime(job.getStart_time(), job.getStop_time());
                    if (time == -1)
                        return "未运行";
                    else return time;
                } else if (table_type == TABLE_COMMITED) {
                    return job.getLeft_svc_time();
                }
            case 4:
                if (table_type == TABLE_NOT_START) {
                    int status = job.getStatus();
                    String statusString = "错误";
                    switch (status) {
                        case JCB.NOT_EXIST:
                            statusString = "未提交";
                            break;
                        case JCB.COMMITTED:
                            statusString = "等待调度";
                            break;
                        case JCB.RUNNING:
                            statusString = "运行中";
                            break;
                        case JCB.FINISHED:
                            statusString = "已结束";
                    }
                    return statusString;
                } else if (table_type == TABLE_FINISHED) {
                    int turn = TimeHelper.subTime(job.getStart_time(), job.getStop_time());
                    if (turn == -1)
                        return "ERROR";
                    return turn * 1.0 / job.getSvc_time();
                }
            default:
                break;
        }
        return null;
    }
}
