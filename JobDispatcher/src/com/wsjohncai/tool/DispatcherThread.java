package com.wsjohncai.tool;

import java.util.Vector;

import com.wsjohncai.common.JCB;
import com.wsjohncai.view.DispatcherView;
import com.wsjohncai.view.MyTableModel;

public class DispatcherThread extends Thread {
	private MyTableModel model;
	private int al_type;
	private Vector<JCB> queue;
	private JCB running;
	private DispatcherView view;

	public DispatcherThread(DispatcherView view, MyTableModel tm, int al) {
		model = tm;
		al_type = al;
		this.view = view;
		queue = model.getQueue();

	}

	private void alterTimeBy1() {
		int count = 0;
		int total = queue.size();
		boolean change = false;
		for (JCB job : queue) {
			switch (job.getStatus()) {
			case JCB.NOT_EXIST:
				if (job.getSummit_time() == DispatcherView.TIME)
					job.setStatus(JCB.COMMITTED);
				if (running == null) {
					running = model.getNextJob(al_type);
					if (running != null) {
						running.setStatus(JCB.RUNNING);
						running.setStart_time(DispatcherView.TIME);
					}
				}
				break;
			case JCB.COMMITTED:
				break;
			case JCB.RUNNING:
				int svc = job.getTime_required();
				if (svc - 1 == 0) {
					if(change) {
						break;
					}
					running = model.getNextJob(al_type);
					if (running != null) {
						running.setStatus(JCB.RUNNING);
						running.setStart_time(DispatcherView.TIME);
						change = true;
					}
					job.setStatus(JCB.FINISHED);
					job.setTime_required(0);
					job.setStop_time(DispatcherView.TIME);
					job.setTurnaround_time(job.getStop_time() - job.getSummit_time());
					job.setWeigh_turnaround_time(
							job.getTurnaround_time() * 1.0 / (job.getStop_time() - job.getStart_time()));
				} else {
					if (change) {
						break;
					}
					job.setTime_required(svc - 1);
				}
				break;
			default:
				if (++count == total)
					view.isStopped = true;
				break;
			}
		}
	}

	@Override
	public void run() {
		alterTimeBy1();
		view.updateCurTime();
		DispatcherView.TIME++;
		while (true) {
			synchronized (this) {
				if (view.isStopped) {
					view.updateCurTime();
					break;
				}
				try {
					if (view.isPaused)
						this.wait();
					Thread.sleep(1000);
					alterTimeBy1();
					view.updateCurTime();
					DispatcherView.TIME++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
