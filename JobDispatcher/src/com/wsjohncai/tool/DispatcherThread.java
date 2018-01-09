package com.wsjohncai.tool;

import java.util.Vector;

import com.wsjohncai.common.JCB;
import com.wsjohncai.view.DispatcherView;

public class DispatcherThread extends Thread {

    public static final int AL_HRN = 3;
    public static final int AL_SJF = 2;
    public static final int AL_FCFS = 1;

    private int al_type;
    private Vector<JCB> queue, committed;
    private DispatcherView view;
    private JCB job;
    private int ProcessSpeed = 1;

    /**
     * 先来先服务算法
     */
    private void fcfs() {
        JCB temp = null;
        for (JCB j : committed) {
            if (temp == null) {
                temp = j;
            } else if (temp.getSummit_time() > j.getSummit_time())
                temp = j;
        }
        job = temp;
    }

    /**
     * 短作业优先算法
     */
    private void sjf() {
        JCB temp = null;
        for (JCB j : committed) {
            if (temp == null) {
                temp = j;
            } else if (temp.getTime_required() > j.getTime_required())
                temp = j;
        }
        job = temp;
    }

    /**
     * 高响应比优先算法
     */
    private void hrn() {
        JCB temp = null;
        for (JCB j : committed) {
            if (temp == null) {
                temp = j;
            } else if (temp.getStop_time() * 1.0 / temp.getTime_required() < j.getStop_time() * 1.0
                    / j.getTime_required())
                temp = j;
        }
        job = temp;
    }

    private void getNextJob(int al) {
        switch (al) {
            case AL_FCFS:
                fcfs();
                break;
            case AL_SJF:
                sjf();
                break;
            case AL_HRN:
                hrn();
                break;
        }
        if (job != null) {
            job.setStart_time(DispatcherView.TIME);
            job.setStatus(JCB.RUNNING);
        }
    }

    public DispatcherThread(DispatcherView view, Vector<JCB> queue, int al) {
        al_type = al;
        this.view = view;
        this.queue = queue;

    }

    /**
     * 将作业提交到就绪队列
     */
    private void getAllCommitted() {
        if (committed == null)
            committed = new Vector<>();
        int count = 0;
        for (JCB jcb : queue) {
            //如果所有作业都运行结束，那么进程结束
            if (jcb.getStatus() == JCB.FINISHED) {
                count++;
                if (count == queue.size())
                    view.setStopped();
                continue;
            }
            //有作业提交
            if (jcb.getSummit_time() == DispatcherView.TIME) {
                jcb.setStatus(JCB.COMMITTED);
                committed.add(jcb);
            }
        }
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
        getAllCommitted();
        getNextJob(al_type);
        view.updateCurTime();
        while (true) {
            try {
                Thread.sleep((int) (1000 * (1.0/ProcessSpeed)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (view.isPaused)
                continue;
            if (view.isStopped) {
                view.updateCurTime();
                break;
            }
            if (job == null) {
                DispatcherView.TIME++;
                getAllCommitted();
                getNextJob(al_type);
                view.updateCurTime();
                continue;
            }
            job.setTime_required(job.getTime_required() - 1);
            DispatcherView.TIME++;
            if (job.getTime_required() == 0) {
                job.setStatus(JCB.FINISHED);
                job.setStop_time(DispatcherView.TIME);
                job.setTurnaround_time(job.getStop_time() - job.getSummit_time());
                job.setWeigh_turnaround_time(job.getTurnaround_time() * 1.0 /
                        (job.getStop_time() - job.getStart_time()));
                committed.remove(job);
                job = null;
                getAllCommitted();
                getNextJob(al_type);
            } else
                getAllCommitted();
            view.updateCurTime();
            view.updateAfterAJob();
        }

    }
}
