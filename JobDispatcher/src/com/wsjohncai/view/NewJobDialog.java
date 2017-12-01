package com.wsjohncai.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.wsjohncai.common.JCB;

//��д��ҵ��Ϣ�Ի�����
public class NewJobDialog extends JDialog {
	private static final long serialVersionUID = -3183179636765136074L;
	private JPanel bg;
	private JLabel name_lb, time_lb, ttime_lb;
	private JTextField name_tx, time_tx, ttime_tx;
	private JButton summit, cancel;
	private JCB job;
	private DispatcherView parent;

	public NewJobDialog(DispatcherView parent) {
		super(parent, "�����ҵ", true);
		this.parent = parent;
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		initDialog();
		this.add(bg);
		this.pack();
		this.setResizable(false);
		int x = parent.getX() + parent.getWidth() / 2 - this.getWidth() / 2;
		int y = parent.getY() + parent.getHeight() / 2 - this.getHeight() / 2;
		this.setLocation(x, y);
		this.setVisible(true);
		this.setAutoRequestFocus(true);
		this.validate();
	}

	private void initDialog() {
		bg = new JPanel();
		bg.setPreferredSize(new Dimension(200, 100));
		// bg.setMinimumSize(bg.getPreferredSize());

		name_lb = new JLabel("��ҵ��  ");
		time_lb = new JLabel("�ύʱ��");
		ttime_lb = new JLabel("�����ʱ");
		name_tx = new JTextField(18);
		time_tx = new JTextField(18);
		ttime_tx = new JTextField(18);
		time_tx.setToolTipText("��0��ʼ��������������Խ���ʾʱ��Խ��");
		ttime_tx.setToolTipText("����0��ʼ��������");

		bg.add(name_lb);
		bg.add(name_tx);
		bg.add(time_lb);
		bg.add(time_tx);
		bg.add(ttime_lb);
		bg.add(ttime_tx);

		summit = new JButton("�ύ");
		cancel = new JButton("ȡ��");
		bg.add(summit);
		bg.add(cancel);
		summit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = name_tx.getText();
				String time = time_tx.getText();
				String ttime = ttime_tx.getText();
				boolean nameVali = name.matches("[^\\s]+");
				boolean timeVali = time.matches("\\d+");
				boolean ttimeVali = ttime.matches("\\d+");
				if (nameVali && timeVali && ttimeVali) {
					job = new JCB(name, Integer.parseInt(time), Integer.parseInt(ttime));
					parent.addJob(job);
					NewJobDialog.this.dispose();
				} else {
					JOptionPane.showMessageDialog(NewJobDialog.this, "��ʽ�������������", "�����д�", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				NewJobDialog.this.dispose();
			}
		});
	}

}
