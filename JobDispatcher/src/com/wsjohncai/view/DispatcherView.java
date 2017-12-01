package com.wsjohncai.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.wsjohncai.common.JCB;
import com.wsjohncai.tool.DispatcherThread;

public class DispatcherView extends JFrame implements ActionListener {

	private static final long serialVersionUID = 119748115546105132L;
	private static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	private static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	public static int TIME = 0;
	private JPanel waiting_pane, op_pane;
	private JTable waiting_table;
	private JScrollPane waiting_scroll;
	private JButton summit, execute, stop, pause, delete;
	private JLabel ta, wta, time;
	private MyTableModel model;
	public boolean isPaused = false;
	public boolean isStopped = true;
	private DispatcherThread dispatcher;

	// ���ý�����
	{
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		createUI();
	}

	private void createUI() {
		this.setTitle("��ҵ����ģ��");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width / 5 * 2, 310);
		this.setResizable(false);

		model = new MyTableModel();
		waiting_table = new JTable(model);
		waiting_table.setPreferredScrollableViewportSize(new Dimension(this.getWidth() - 40, 150));
		waiting_table.getColumnModel().getColumn(0).setPreferredWidth(50);
		waiting_table.getColumnModel().getColumn(4).setPreferredWidth(40);
		waiting_table.getColumnModel().getColumn(6).setPreferredWidth(70);
		waiting_table.getColumnModel().getColumn(7).setPreferredWidth(100);
		waiting_table.setRowSelectionAllowed(true);
		waiting_table.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 
		// waiting_table.setRowSelectionAllowed(true);
		waiting_scroll = new JScrollPane(waiting_table);
		// waiting_scroll.setPreferredSize(new Dimension(this.getWidth() - 20,
		// this.getHeight() - 50));
		waiting_scroll.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		waiting_scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "��ҵ����"));
		waiting_pane = new JPanel();
		waiting_pane.add(waiting_scroll);
		// waiting_scroll.setBounds(3, 25, this.getWidth() - 20,
		// this.getHeight() - 50);
		this.add(waiting_pane, BorderLayout.NORTH);

		ta = new JLabel("ƽ����תʱ�䣺");
		wta = new JLabel("ƽ����Ȩ��תʱ�䣺");
		time = new JLabel("��ǰʱ�䣺0");
		time.setFont(new Font("������", Font.PLAIN, 12));
		ta.setFont(new Font("������", Font.PLAIN, 12));
		wta.setFont(new Font("������", Font.PLAIN, 12));
		summit = new JButton("�ύ");
		summit.setToolTipText("�ύһ����ҵ���ȴ��б�");
		summit.setActionCommand("summit");
		summit.addActionListener(this);
		execute = new JButton("��ʼ");
		execute.setToolTipText("��ʼִ����ҵ����");
		execute.setActionCommand("exec");
		execute.addActionListener(this);
		stop = new JButton("ֹͣ");
		stop.setActionCommand("stop");
		stop.addActionListener(this);
		pause = new JButton("��ͣ");
		pause.setActionCommand("pause");
		pause.addActionListener(this);
		delete = new JButton("ɾ��");
		delete.setToolTipText("ɾ��ѡ������ҵ");
		delete.setActionCommand("del");
		delete.addActionListener(this);
		op_pane = new JPanel(null);
		op_pane.setPreferredSize(new Dimension(this.getWidth() - 50, this.getHeight() - waiting_pane.getHeight() - 20));
		op_pane.add(time);
		time.setBounds(10, 5, this.getWidth() / 5, 30);
		time.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		op_pane.add(ta);
		ta.setBounds(10 + this.getWidth() / 5 + 5, 5, this.getWidth() / 5 * 2 - 20, 30);
		ta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		op_pane.add(wta);
		wta.setBounds(this.getWidth() / 5 * 3, 5, this.getWidth() / 5 * 2 - 20, 30);
		wta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		op_pane.add(summit);
		op_pane.add(delete);
		op_pane.add(execute);
		op_pane.add(pause);
		op_pane.add(stop);
		summit.setBounds(20, 40, this.getWidth() / 6, 30);
		delete.setBounds(20 + this.getWidth() / 6, 40, this.getWidth() / 6, 30);
		execute.setBounds(20 + this.getWidth() / 3, 40, this.getWidth() / 6, 30);
		pause.setBounds(20 + this.getWidth() / 2, 40, this.getWidth() / 6, 30);
		stop.setBounds(20 + this.getWidth() / 3 * 2, 40, this.getWidth() / 6, 30);
		this.add(op_pane, BorderLayout.CENTER);

		this.setLocation(width / 2 - this.getWidth() / 2, height / 2 - this.getHeight() / 2);
		this.setVisible(true);
		this.validate();
	}

	public void setFinal() {
		Vector<JCB> q = model.getQueue();
		int t1 = 0,t2 = 0, count = 0;
		for(JCB j: q) {
			if(j.getStatus()!=JCB.FINISHED)
				continue;
			t1+=j.getTurnaround_time();
			t2+=j.getWeigh_turnaround_time();
			count++;
		}
		ta.setText("ƽ����תʱ�䣺"+t1*1.0/count);
		wta.setText("ƽ����Ȩ��תʱ�䣺"+t2*1.0/count);
	}
	
	public void updateCurTime() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				time.setText("��ǰʱ��: " + TIME);
				model.fireTableDataChanged();
				setFinal();
			}

		});
	}

	void addJob(JCB job) {
		if (job != null)
			model.addJob(job);
	}

	void startDispatch(int al) {
		dispatcher = new DispatcherThread(this, model, al);
		dispatcher.start();
	}

	public static void main(String[] args) {
		DispatcherView view = new DispatcherView();
		view.init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "exec":
			isStopped = false;
			new StartDialog(this);
			break;
		case "summit":
			new NewJobDialog(this);
			model.fireTableDataChanged();
			break;
		case "del":
			int row = waiting_table.getSelectedRow();
			if (model.deleteJob(row))
				model.fireTableDataChanged();
			else
				JOptionPane.showMessageDialog(this, "�޷�ɾ������ҵ��������", "�޷�ɾ��", JOptionPane.ERROR_MESSAGE);
			break;
		case "pause":
			if (isPaused) {
				dispatcher.notify();
				pause.setText("��ͣ");
			}
			else
				pause.setText("����");
			isPaused = !isPaused;
			break;
		case "stop":
			isStopped = true;
			model.setDataToDefault();
			TIME = 0;
			updateCurTime();
			break;
		default:
		}

		this.requestFocus();
	}

}
