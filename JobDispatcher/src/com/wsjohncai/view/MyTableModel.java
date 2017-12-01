package com.wsjohncai.view;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.wsjohncai.common.JCB;
import com.wsjohncai.logic.Algorithm;

public class MyTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2039423832353747535L;
	private static final String[] colNames = { "��ҵ��", "�ύʱ��", "������ʱ", "��ʼʱ��", "״̬", "ֹͣʱ��", "��תʱ��", "��Ȩ��תʱ��" };
	private static Vector<String> columnNames = new Vector<String>();
	private Vector<JCB> queue;

	public MyTableModel() {
		for (String s : colNames) {
			columnNames.add(s);
		}
		queue = new Vector<>();
		for (int i = 0; i < 5; i++) {
			JCB j = new JCB("��ҵ" + i, 2 + 3 * i, (int) (Math.random() * 10 + 1));
			queue.add(j);
		}
	}

	public void setDataToDefault() {
		for (JCB j : queue) {
			if (j.getStatus() == JCB.RUNNING || j.getStatus() == JCB.FINISHED)
				j.setTime_required(j.getTime_required() + j.getStop_time() - j.getStart_time());
			j.setStart_time(0);
			j.setStatus(JCB.NOT_EXIST);
			j.setStop_time(0);
			j.setTurnaround_time(0);
			j.setWeigh_turnaround_time(0);
		}
	}

	public void addJob(JCB job) {
		if (queue == null)
			queue = new Vector<>();
		queue.add(job);
	}

	public boolean deleteJob(int idx) {
		JCB j = queue.get(idx);
		if (j.getStatus() != JCB.RUNNING) {
			queue.remove(idx);
			return true;
		}
		return false;
	}

	public JCB getNextJob(int al) {
		return new Algorithm().getNextJob(queue, al);
	}

	public Vector<JCB> getQueue() {
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
			return job.getSummit_time();
		case 2:
			return job.getTime_required();
		case 3:
			if (job.getStatus() == JCB.NOT_EXIST || job.getStatus() == JCB.COMMITTED)
				return "δ��ʼ";
			else
				return job.getStart_time();

		case 4:
			int status = job.getStatus();
			String statusString = "����";
			switch (status) {
			case JCB.NOT_EXIST:
				statusString = "δ�ύ";
				break;
			case JCB.COMMITTED:
				statusString = "�ȴ�����";
				break;
			case JCB.RUNNING:
				statusString = "������";
				break;
			case JCB.FINISHED:
				statusString = "�ѽ���";
			}
			return statusString;

		case 5:
			if (job.getStatus() == JCB.NOT_EXIST)
				return "δ���н���";
			return job.getStop_time();
		case 6:
			if (job.getTime_required() > 0)
				return "δ���н���";
			return job.getTurnaround_time();
		case 7:
			if (job.getTime_required() > 0)
				return "δ���н���";
			return job.getWeigh_turnaround_time();
		}
		return null;
	}

}
