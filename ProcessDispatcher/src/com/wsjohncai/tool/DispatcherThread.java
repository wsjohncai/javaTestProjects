package com.wsjohncai.tool;

import java.util.Vector;

import com.wsjohncai.view.DispatcherView;
import com.wsjohncai.common.PCB;

public class DispatcherThread extends Thread {

    public static final int AL_HRN = 3;
    public static final int AL_SR = 2;
    public static final int AL_FCFS = 1;

    private int ProcessSpeed = 1; //调度速度
    private int al_type; //表示当前运行的算法
    private Vector<PCB> queue;
    private PCB aljob;
    private DispatcherView view; //用于保存界面指针
    private int round; //时间片

    public DispatcherThread(DispatcherView view, Vector<PCB> queue, int al) {
        view.isStopped = false;
        al_type = al;
        this.view = view;
        this.queue = queue;
        round = view.getRound();
    }

    /**
     * 先来先服务算法
     */
    private void fcfs() {
        PCB job = null;
        for (PCB j : committed) { //从就绪队列中挑选出提交时间最早的进程
//            if (j.getStatus() != PCB.COMMITTED)
//                continue;
            if (job == null) {
                job = j;
            } else if (job.getSummit_time() > j.getSummit_time())
                job = j;
        }
        aljob = job;
    }

    private int poi = 0; //当前时间片选中的进程索引

    /**
     * 简单时间片轮转算法
     */
    private void sr() {
        PCB job = null;
        if (committed.size() == 1) { //如果只剩一个就绪进程，则直接选中
            job = committed.get(0);
            poi = 0;
        } else if (committed.size() > 1) {
            //如果当前索引对应的进程已经改变，则选择当前索引进程所为下一个
            if (committed.get(poi) != aljob)
                job = committed.get(poi);
            //如果索引值已经超过范围，则将索引值归0
            else if (poi >= committed.size() - 1) {
                job = committed.get(0);
                poi = 0;
            } else {
                //否则，将索引+1，选中其对应进程
                job = committed.get(++poi);
            }
        }
        aljob = job;
    }

    /**
     * 最高优先级算法，优先级数越小，优先级越大
     */
    private void hrn() {
        PCB job = null;
        for (PCB j : committed) {
//            if (j.getStatus() != PCB.COMMITTED)
//                continue;
            //选取优先级最小进程
            if (job == null) {
                job = j;
            } else if (job.getPriority() > j.getPriority())
                job = j;
        }
        aljob = job;
    }

    /**
     * 根据不同算法调用不同函数选择进程
     */
    private PCB getNextProc() {
        switch (al_type) {
            case AL_FCFS:
                fcfs();
                break;
            case AL_SR:
                sr();
                break;
            case AL_HRN:
                hrn();
                break;
        }
        //如果选择好了进程，设置其状态为“Running”，设置其开始时间为当前时间
        if (aljob != null) {
            if (aljob.getStart_time() == 0)
                aljob.setStart_time(DispatcherView.TIME);
            aljob.setStatus(PCB.RUNNING);
            System.out.println("Running: " + aljob.getName());
        }
        return aljob;
    }

    private Vector<PCB> committed;

    /**
     * 检查任务，查询是否提交，若是，加入提交队列
     */
    private void alterAsCommitted() {
        if (committed == null) {
            committed = new Vector<>();
        }
        int count = 0;
        for (PCB p : queue) {
            //如果其提交时间等于当前时间，将其放入就绪队列
            if (p.getSummit_time() == DispatcherView.TIME) {
                p.setStatus(PCB.COMMITTED);
                committed.add(p);
                continue;
            }
            //计算运行结束的进程的任务
            if (p.getStatus() == PCB.FINISHED)
                count++;
        }
        //如果所有进程运行结束，那么停止调度
        if (queue.size() == count || queue.size() == 0) {
            view.setStopped();
        }
        System.out.println("Committed: " + (committed == null ? "0" : committed.size()));
    }

    /**
     * 设置进程的速度，可选值为1,2,4,8；
     */
    public void setProcessSpeed(int speed) {
        ProcessSpeed = speed;
    }

    public int getProcessSpeed() {
        return ProcessSpeed;
    }

    @Override
    public void run() {
        //0时刻，进行一次调度
        DispatcherView.TIME = 0;
        alterAsCommitted();
        PCB running = getNextProc();
        view.updateCurTime();
        while (true) {
            try {
                //模拟时间进行了1秒
                Thread.sleep((int) (1000 * (1.0/ProcessSpeed)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //程序暂停
            if(view.isPaused)
                continue;
            //程序停止，跳出循环
            if (view.isStopped)
                break;
            //如果当前有进程处于运行状态
            if (running != null) {
                //将进程的服务需要时间-1，时间片-1，当前时间+1
                running.setTime_req_left(running.getTime_req_left() - 1);
                round--;
                DispatcherView.TIME++;
                //如果进程已经运行结束，那么设置其状态为“Finished”,
                if (running.getTime_req_left() == 0) {
                    running.setStatus(PCB.FINISHED);
                    running.setStop_time(DispatcherView.TIME);
                    //从就绪队列中移除，时间片重置
                    committed.remove(committed.indexOf(running));
                    round = view.getRound();
                    //检查是否有新进程进入就绪队列
                    alterAsCommitted();
                    //获取下一个进程，更新界面数据显示
                    running = getNextProc();
                    view.updateCurTime();
                    continue;
                }
                //如果运行时间片轮转方法，时间片为0时
                if (al_type == AL_SR && round == 0) {
                    //将当前进程设为就绪，重新设置时间片
                    running.setStatus(PCB.COMMITTED);
                    round = view.getRound();
                    alterAsCommitted();
                    running = getNextProc();
                } else {
                    //程序运行未结束，时间片不为0，那么只检查是否有新进程进入
                    alterAsCommitted();
                }
            } else {
                //当前没有进程在调度，时间+1，检查新进程
                DispatcherView.TIME++;
                alterAsCommitted();
                running = getNextProc();
            }
            view.updateCurTime();
        }
    }
}
