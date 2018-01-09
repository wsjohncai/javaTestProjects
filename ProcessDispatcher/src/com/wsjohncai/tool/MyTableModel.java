package com.wsjohncai.tool;

import java.util.Arrays;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.wsjohncai.common.PCB;

public class MyTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 2039423832353747535L;
    private static final String[] colNames = {"进程名", "提交时间", "服务需时","优先级", "开始时间", "停止时间", "状态"};
    private static Vector<String> columnNames = new Vector<>();
    private Vector<PCB> queue;

    public MyTableModel() {
        columnNames.addAll(Arrays.asList(colNames));
        queue = new Vector<>();
        //初始化数据
        for (int i = 0; i < 5; i++) {
            int r = (int) (Math.random() * 10);
            int r1 = (int) (Math.random() * 5);
            PCB p = new PCB("进程" + i, r+r1 , r*r1+1, r1+1);
            queue.add(p);
        }
    }

    /**
     * 将数据重置为初始状态
     */
    public void setDataToDefault() {
        for (PCB p : queue) {
            p.setTime_req_left(p.getTime_req());
            p.setStart_time(0);
            p.setStatus(PCB.NOT_EXIST);
            p.setStop_time(0);
        }
    }

    //添加一个新进程到队列中
    public void addProc(PCB proc) {
        if (queue == null)
            queue = new Vector<>();
        queue.add(proc);
    }

    /**
     * 删除指定索引的进程
     */
    public boolean deleteProc(int idx) {
        PCB p = queue.get(idx);
        if (p.getStatus() != PCB.RUNNING) {
            queue.remove(idx);
            return true;
        }
        return false;
    }

    /**
     * 获得数据存储队列
     */
    public Vector<PCB> getQueue() {
        return queue;
    }

    @Override
    public int getRowCount() {
        return queue.size();
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

    /**
     * 设置不同状态Table的返回值
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PCB proc = queue.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return proc.getName();
            case 1:
                return proc.getSummit_time();
            case 2:
                return proc.getTime_req_left();
            case 3:
                return proc.getPriority();
            case 4:
                if (proc.getStatus() == PCB.NOT_EXIST)
                    return "未开始";
                else if(proc.getStatus() == PCB.COMMITTED && proc.getStart_time() == 0)
                    return "未开始";
                else
                    return proc.getStart_time();
            case 6:
                int status = proc.getStatus();
                String statusString = "错误";
                switch (status) {
                    case PCB.NOT_EXIST:
                        statusString = "未提交";
                        break;
                    case PCB.COMMITTED:
                        statusString = "等待调度";
                        break;
                    case PCB.RUNNING:
                        statusString = "运行中";
                        break;
                    case PCB.FINISHED:
                        statusString = "已结束";
                }
                return statusString;

            case 5:
                if (proc.getStatus() != PCB.FINISHED)
                    return "未运行结束";
                return proc.getStop_time();
        }
        return null;
    }

}
