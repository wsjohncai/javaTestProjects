package com.mmrd.tools;

import com.mmrd.common.JCB;
import com.mmrd.common.MMRBlock;
import com.mmrd.view.MainFrame;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

public class MMRModel extends AbstractTableModel {
    private static final String[] colNames = {"内存块号", "起始地址", "长度"};
    private static Vector<String> columnNames = new Vector<>();
    private static Vector<MMRBlock> queue;

    public MMRModel() {
        columnNames.addAll(Arrays.asList(colNames));
        queue = new Vector<>();
        initData();
    }

    public void initData() {
        if (queue.size() > 0)
            queue.removeAllElements();
        queue.add(new MMRBlock((int) (Math.random() * 512), (int) (Math.random() * 512)));
        int len = (int) (Math.random() * 10) + 10;
        for (int i = 0; i < len; i++) {
            int r = (int) (Math.random() * 1024) + 200;
            int l = (int) (Math.random() * 1024);
            MMRBlock last = queue.get(i);
            int start = r + last.getStart_addr() + last.getLength();
            queue.add(new MMRBlock(start, l + 50));
        }
    }

    public static boolean addMMR(MMRBlock b) {
        for (MMRBlock bin : queue) {
            if (b.getStart_addr() > bin.getStart_addr()) {
                if (b.getStart_addr() < bin.getStart_addr() + bin.getLength())
                    return false;
            } else if (b.getStart_addr() < bin.getStart_addr()) {
                if (b.getStart_addr() + b.getLength() > bin.getStart_addr())
                    return false;
            } else
                return false;
        }
        for (JCB jin : JobModel.getQueue()) {
            if (b.getStart_addr() > jin.getStore_start_addr()) {
                if (b.getStart_addr() < jin.getStore_start_addr() + jin.getSize())
                    return false;
            } else if (b.getStart_addr() < jin.getStore_start_addr()) {
                if (b.getStart_addr() + b.getLength() > jin.getStore_start_addr())
                    return false;
            } else
                return false;
        }
        if (queue == null)
            queue = new Vector<>();
        queue.add(b);
        return true;
    }

    public void deleteMMR(int idx) {
        queue.remove(idx);
    }

    public static Vector<MMRBlock> getQueue() {
        return queue;
    }

    /**
     * 将内存队列按初始地址大小排序
     */
    public static void reorganize() {
        queue.sort(MyComparator.getAddrOrderInstance());
        int i = 0;
        while (true) {
            if (i == queue.size() - 1)
                break;
            MMRBlock b = queue.get(i);
            MMRBlock b1 = queue.get(i + 1);
            if ((b.getStart_addr() + b.getLength()) == b1.getStart_addr()) {
                b.setLength(b.getLength() + b1.getLength());
                queue.remove(i + 1);
            } else {
                i++;
            }
        }
        if (MainFrame.getAl_Sel() == AllotAlgorithm.OPF
                || MainFrame.getAl_Sel() == AllotAlgorithm.WF)
            queue.sort(MyComparator.getInstance());
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
        MMRBlock mmr = queue.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex;
            case 1:
                return mmr.getStart_addr();
            case 2:
                return mmr.getLength();
        }
        return null;
    }

}

class MyComparator implements Comparator<MMRBlock> {

    private static int mode = AllotAlgorithm.FF;
    private static MyComparator instance;

    static {
        instance = new MyComparator();
    }

    protected static MyComparator getInstance() {
        mode = MainFrame.getAl_Sel();
        return instance;
    }

    static MyComparator getAddrOrderInstance() {
        mode = AllotAlgorithm.FF;
        return instance;
    }

    @Override
    public int compare(MMRBlock o1, MMRBlock o2) {
        if (mode == AllotAlgorithm.FF || mode == AllotAlgorithm.RFF) {
            if (o1.getStart_addr() < o2.getStart_addr())
                return -1;
            else if (o1.getStart_addr() > o2.getStart_addr())
                return 1;
        } else {
            if (o1.getLength() < o2.getLength())
                return -1;
            else if (o1.getLength() > o2.getLength())
                return 1;
        }
        return 0;
    }
}