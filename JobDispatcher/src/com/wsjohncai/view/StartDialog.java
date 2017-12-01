package com.wsjohncai.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.wsjohncai.logic.Algorithm;

public class StartDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 3935880619257483913L;
	private JButton bt1, bt2, bt3;
	private JPanel bg;
	private DispatcherView parent;

	public StartDialog(DispatcherView parent) {
		super(parent, "ѡ��һ���㷨", true);
		this.parent  = parent;
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		init();
		this.pack();
		this.setResizable(false);
		 int x = parent.getX() + parent.getWidth() / 2 - this.getWidth() / 2;
		 int y = parent.getY() + parent.getHeight() / 2 - this.getHeight() /2;
		 this.setLocation(x, y);
		this.setVisible(true);
		this.setAutoRequestFocus(true);
		this.validate();
	}

	private void init() {

		bt1 = new JButton("�����ȷ����㷨");
		bt1.addActionListener(this);
		bt2 = new JButton("����ҵ�����㷨");
		bt2.addActionListener(this);
		bt3 = new JButton("����Ӧ�������㷨");
		bt3.addActionListener(this);

		bg = new JPanel();
		bg.setPreferredSize(new Dimension(150,100));
		bg.add(bt1);
		bg.add(bt2);
		bg.add(bt3);

		this.add(bg);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bt1) 
			parent.startDispatch(Algorithm.AL_FCFS);
		else if(e.getSource() == bt2)
			parent.startDispatch(Algorithm.AL_SJF);
		else 
			parent.startDispatch(Algorithm.AL_HRN);
		dispose();
	}
}
