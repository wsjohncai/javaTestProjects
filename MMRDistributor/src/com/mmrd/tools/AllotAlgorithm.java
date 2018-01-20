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
        //从最高地址开始遍历，直到找到可以容得下作业的内存块为止
        while (--i >= 0) {
            MMRBlock m = mmrList.get(i);
            if (load(m, j))
                break;
        }
    }

    /**
     * 最佳适应算法
     */
    private static void a_OPF(JCB j) {
        int i = -1;
        //从最小内存块开始遍历，直到找到能容下作业的最小内存块，然后结束循环
        while (++i < mmrList.size()) {
            MMRBlock m = mmrList.get(i);
            if (load(m, j))
                break;
        }
        //每次分配后重新组织，保证内存块按从小到大顺序排列
        MMRModel.reorganize();
    }

    /**
     * 最差适应算法
     */
    private static void a_WF(JCB j) {
        MMRBlock m = mmrList.lastElement();
        load(m, j);
        //每次调用后重新组织内存块，保证最大内存块序列在数组末尾
        MMRModel.reorganize();
    }

    /**
     * 用于将指定作业加载到指定内存块中
     * @return 如果装入成功，返回true,否则返回false
     */
    private static boolean load(MMRBlock m, JCB j) {
        if (m.getLength() > j.getSize()) {//如果空闲块大小大于作业大小，装入
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

    public static void setSeqToDefault() {
        seq = mmrList.size();
    }

    /**
     * 循环首次适应算法
     */
    private static void a_RFF(JCB j) {
        int i = seq;
        //从上次搜索的索引开始，往前搜索
        while (--i >= 0) {
            MMRBlock m = mmrList.get(i);
            if (load(m, j))//如果空闲块被装入，将下次循环的序列改为当前位置
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
                break;
            default:
                a_FF(j);
                break;
        }
    }
}
