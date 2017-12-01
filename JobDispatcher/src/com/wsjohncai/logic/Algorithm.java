package com.wsjohncai.logic;

import java.util.Vector;

import com.wsjohncai.common.JCB;

public class Algorithm {

	public static final int AL_HRN = 3;
	public static final int AL_SJF = 2;
	public static final int AL_FCFS = 1;

	private JCB job;
	private Vector<JCB> queue;

	private void fcfs() {
		for (JCB j : queue) {
			if (j.getStatus() != JCB.COMMITTED)
				continue;
			if (job == null) {
				job = j;
			} else if (job.getSummit_time() > j.getSummit_time())
				job = j;
		}
	}

	private void sjf() {
		for (JCB j : queue) {
			if (j.getStatus() != JCB.COMMITTED)
				continue;
			if (job == null) {
				job = j;
			} else if (job.getTime_required() > j.getTime_required())
				job = j;
		}
	}

	private void hrn() {
		for (JCB j : queue) {
			if (j.getStatus() != JCB.COMMITTED)
				continue;
			if (job == null) {
				job = j;
			} else if (job.getStop_time() * 1.0 / job.getTime_required() < j.getStop_time() * 1.0
					/ j.getTime_required())
				job = j;
		}
	}

	public JCB getNextJob(Vector<JCB> queue, int al) {
		this.queue = queue;
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
		return job;
	}

}
