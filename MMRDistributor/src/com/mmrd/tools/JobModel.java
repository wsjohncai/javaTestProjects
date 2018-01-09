package com.mmrd.tools;

import com.mmrd.common.JCB;
import com.mmrd.common.MMRBlock;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.Vector;

public class JobModel extends AbstractTableModel {
    private static final long serialVersionUID = 2039423832353747535L;
    private static final String[] colNames = {"作业名", "大小", "起始地址"};
    private static Vector<String> columnNames = new Vector<>();
    private static Vector<JCB> queue;

    public JobModel() {
        columnNames.addAll(Arrays.asList(colNames));
        queue = new Vector<>();
        initData();
    }

    public void initData() {
        if (queue != null && queue.size() > 0)
            queue.removeAllElements();
        else if (queue == null)
            queue = new Vector<>();
        int len = (int) (Math.random() * 10) + 10;
        for (int i = 0; i < len; i++) {
            int r = (int) (Math.random() * 1024);
            JCB j = new JCB("作业" + i,
                    r > 1024 ? r + (int) (Math.random() * 512) : r);
            queue.add(j);
        }
    }

    public static void addJob(JCB job) {
        if (queue == null)
            queue = new Vector<>();
        queue.add(job);
    }

    public void recycleJob(int idx) {
        //回收指定作业内存
        JCB j = queue.get(idx);
        if(j.getStore_start_addr() == JCB.NOT_STORE)
            return;
        Vector<MMRBlock> blocks = MMRModel.getQueue();
        blocks.add(new MMRBlock(j.getStore_start_addr(), j.getSize()));
        j.setStore_start_addr(JCB.NOT_STORE);
    }

    public void deleteJob(int idx) {
        JCB j = queue.get(idx);
        if (j.getStore_start_addr() == JCB.NOT_STORE) {
            queue.remove(idx);
        } else {  //如果已分配内存，那么先回收再删除
            recycleJob(idx);
            queue.remove(idx);
        }
    }

    public void recycleAll() {
        for (int i = 0; i < queue.size(); i++) {
            recycleJob(i);
        }
    }

    public static Vector<JCB> getQueue() {
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

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        JCB job = queue.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return job.getName();
            case 1:
                return job.getSize();
            case 2:
                switch (job.getStore_start_addr()) {
                    case JCB.NOT_STORE:
                        return "未分配";
                    default:
                        return job.getStore_start_addr();
                }
        }
        return null;
    }
}
