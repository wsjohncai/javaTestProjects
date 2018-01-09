package com.wsjohncai.view;

import com.wsjohncai.tool.DispatcherThread;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class StartDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 3935880619257483913L;
	private JButton bt1, bt2, bt3;
	private JPanel bg;
	private DispatcherView parent;

	public StartDialog(DispatcherView parent) {
		super(parent, "选择一种算法", true);
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

		bt1 = new JButton("先来先服务算法");
		bt1.addActionListener(this);
		bt2 = new JButton("短作业优先算法");
		bt2.addActionListener(this);
		bt3 = new JButton("高响应比优先算法");
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
			parent.startDispatch(DispatcherThread.AL_FCFS);
		else if(e.getSource() == bt2)
			parent.startDispatch(DispatcherThread.AL_SJF);
		else 
			parent.startDispatch(DispatcherThread.AL_HRN);
		dispose();
	}
}
