package com.ds.tools;

import com.ds.common.JCB;
import com.ds.view.MainFrame;

import java.util.Vector;

public class DispatcherThread implements Runnable {

    private Vector<JCB> alList, inList, runList, finList;
    private MainFrame view;
    private JCB running = null;
    private float speed = 1; //调度速度

    public DispatcherThread(MainFrame view) {
        this.view = view;
        alList = DataModel.getQueue(DataModel.TABLE_NOT_START);
        inList = DataModel.getQueue(DataModel.TABLE_COMMITTED);
        runList = DataModel.getQueue(DataModel.TABLE_RUNNING);
        finList = DataModel.getQueue(DataModel.TABLE_FINISHED);
    }

    /**
     * 从输入井中选择作业提交
     */
    private void commitJob() {
        if (alList.size() == (inList.size() + runList.size() + finList.size()))
            return;
        while (inList.size() < 2) {
            JCB first = null;
            for (JCB j : alList) {
                if (inList.contains(j))
                    continue;
                if (j.getStatus() == JCB.NOT_EXIST && TimeHelper
                        .subTime(j.getSummit_time(), view.getTime()) >= 0) {
                    if (first == null)
                        first = j;
                    else if (TimeHelper.subTime(first.getSummit_time()
                            , j.getSummit_time()) < 0)
                        first = j;
                }
            }
            if (first != null) {
                first.setStatus(JCB.COMMITTED);
                first.setStart_time(view.getTime());
                inList.add(first);
            }
            else
                break;
        }
    }

    /**
     * 选择进程进行调度
     */
    private void putIntoRun() {
        JCB toRun = null;
        //获取就绪队列中优先级最高的作业
        for (JCB j : inList) {
            if (toRun == null)
                toRun = j;
            else if (toRun.getPriority() > j.getPriority()) {
                toRun = j;
            }
        }
        if (toRun != null) {
            //如果当前没有运行进程，则该进程调入运行
            if (running == null) {
                toRun.setStatus(JCB.RUNNING);
                running = toRun;
                runList.add(toRun);
                inList.remove(toRun);
                commitJob();
            } else {
                //如果正在运行进程，那么优先级高的将进入运行，低的被调回就绪队列
                if (toRun.getPriority() < running.getPriority()) {
                    toRun.setStatus(JCB.RUNNING);
                    running.setStatus(JCB.COMMITTED);
                    inList.add(running);
                    runList.remove(running);
                    running = toRun;
                    inList.remove(toRun);
                    runList.add(toRun);
                }
            }
        }
    }

    /**
     * 设置进程的速度，可选值为1,2,4,8；
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        commitJob();
        putIntoRun();
        view.refreshView();
        while (true) {
            try {
                //模拟时间进行了1秒
                Thread.sleep((int) (1000 * (1.0f / speed)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //程序暂停
            if (view.isPaused)
                continue;
            //程序停止，跳出循环
            if (view.isStopped)
                break;
            if (running != null) {
                //将当前运行的程序的服务需时-1，当前时间+1
                running.setLeft_svc_time(running.getLeft_svc_time() - 1);
                view.setTime(TimeHelper.addTime(view.getTime(), 1));
                //如果当前进程运行结束，将其转移到结束队列
                if (running.getLeft_svc_time() == 0) {
                    running.setStatus(JCB.FINISHED);
                    running.setStop_time(view.getTime());
                    finList.add(running);
                    runList.removeAllElements();
                    running = null;
                    view.countAVG();
                    //如果所有进程都运行结束，停止
                    if (finList.size() == alList.size()) {
                        view.setStopped();
                        view.refreshView();
                        break;
                    }
                }
            } else {
                view.setTime(TimeHelper.addTime(view.getTime(), 1));
            }
            //检查输入井
            commitJob();
            putIntoRun();
            view.refreshView();
        }
    }
}
