package com.mmrd.tools;

import com.mmrd.common.JCB;
import com.mmrd.common.MMRBlock;
import com.mmrd.view.MainFrame;

import java.util.Vector;

public class AllotAlgorithm {
    public static final int FF = 0; //首次适应算法
    public static final int RFF = 1;//循环首次适应算法
    public static final int OPF = 2;//最佳适应算法
    public static final int WF = 3;//最坏适应算法
    private static Vector<MMRBlock> mmrList;

    static {
        mmrList = MMRModel.getQueue();
    }

    /**
     * 首次适应算法
     */
    private static void a_FF(JCB j) {
        int i = mmrList.size();
        while (--i >= 0) {
            MMRBlock m = mmrList.get(i);
            if (load(m, j))
                break;
        }
    }

    private static void a_OPF(JCB j) {
        int i = -1;
        MMRBlock fin = null;
        while (++i < mmrList.size()) {
            MMRBlock m = mmrList.get(i);
            if (load(m, j))
                break;
        }
    }

    private static void a_WF(JCB j) {
        MMRBlock m = mmrList.lastElement();
        load(m, j);
    }

    private static boolean load(MMRBlock m, JCB j) {
        if (m.getLength() > j.getSize()) {//如果空闲块大小大于作业大小，装入，并将下次循环坐标设为当前索引
            j.setStore_start_addr(m.getStart_addr());
            m.setStart_addr(m.getStart_addr() + j.getSize());
            m.setLength(m.getLength() - j.getSize());
            return true;
        } else if (m.getLength() == j.getSize()) {//如果刚好等于空闲块大小，将此空闲块从列表中移除
            j.setStore_start_addr(m.getStart_addr());
            mmrList.remove(m);
            return true;
        }
        return false;
    }

    private static int seq = mmrList.size();

    private static void a_RFF(JCB j) {
        int i = seq;
        while (--i >= 0) {
            MMRBlock m = mmrList.get(i);
            if (load(m, j))//如果空闲块被装入
                seq = i;
            if (seq == i) { //作业被分配，跳出循环
                //如果索引值为0，那么将其跳转到链表末
                if (seq == 0)
                    seq = mmrList.size();
                break;
            }
            if (i == 0) { //循环到链表首部，从最后重新开始
                i = mmrList.size();
                if (i == seq)
                    break;
            }

        }
    }

    public static void call(JCB j) {
        if (j.getStore_start_addr() != JCB.NOT_STORE)
            return;
        switch (MainFrame.getAl_Sel()) {
            case FF:
                a_FF(j);
                break;
            case RFF:
                a_RFF(j);
                break;
            case OPF:
                a_OPF(j);
                break;
            case WF:
                a_WF(j);
                MMRModel.reorganize();
                break;
            default:
                a_FF(j);
                break;
        }
    }
}
