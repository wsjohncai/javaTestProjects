package com.johncai;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class TimerTest extends JFrame implements ActionListener {

	public static void main(String[] args) {
		TimerTest instance = new TimerTest();
	}
	
	JPanel timePane = null;
	JScrollPane editPane = null;
	JLabel time = null;
	JTextArea edit = null;
	public TimerTest() {
		this.setTitle("TimerTest");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		edit = new JTextArea();
		edit.setEditable(false);
		editPane = new JScrollPane(edit);
		editPane.setPreferredSize(new Dimension(200,150));
		this.add(editPane,BorderLayout.CENTER);
		
		timePane = new JPanel();
		this.add(timePane, BorderLayout.SOUTH);
		time = new JLabel(Calendar.getInstance().getTime().toString());
		time.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN,14));
		Timer t = new Timer(1000,this);
		t.start();
		timePane.add(time);
		
		this.setVisible(true);
		this.pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		time.setText(Calendar.getInstance().getTime().toString());
		edit.append("Time is changed !  "+Calendar.getInstance().get(Calendar.SECOND)+"\n");
	}

}
