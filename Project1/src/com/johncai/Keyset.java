package com.johncai;

import javax.swing.*;
import java.awt.event.*;

public class Keyset implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		String lab = ((JMenuItem) e.getSource()).getText();
		System.out.println("label = " + lab);
		if (lab.equals("Exit")) {
			System.exit(0);
		}
	}

	public static void main(String args[]) {
		JFrame f = new JFrame("testing");
		JMenu m = new JMenu("File");
		m.setMnemonic(KeyEvent.VK_F);
		Keyset acl = new Keyset();
		JMenuItem mi1 = new JMenuItem("Open");
		mi1.setMnemonic(KeyEvent.VK_O);
		mi1.addActionListener(acl);
		m.add(mi1);
		JMenuItem mi2 = new JMenuItem("Save");
		mi2.setMnemonic(KeyEvent.VK_S);
		mi2.addActionListener(acl);
		m.add(mi2);
		KeyStroke ms3 = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK);
		JMenuItem mi3 = new JMenuItem("Exit");
		mi3.setMnemonic(KeyEvent.VK_E);
		mi3.setAccelerator(ms3);
		mi3.addActionListener(acl);
		m.add(mi3);
		KeyStroke ms4 = KeyStroke.getKeyStroke('X');
		JMenuItem mi4 = new JMenuItem("Close");
		mi4.setMnemonic(KeyEvent.VK_N);
		mi4.setAccelerator(ms4);
		mi4.addActionListener(acl);
		m.add(mi4);
		JMenuBar mb = new JMenuBar();
		mb.add(m);
		f.setJMenuBar(mb);
		f.setSize(200, 200);
		f.setVisible(true);
	}
}