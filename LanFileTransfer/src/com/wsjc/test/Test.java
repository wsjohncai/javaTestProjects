package com.wsjc.test;

import static org.junit.Assert.*;

import java.io.File;

import javax.swing.JFileChooser;

import com.wsjc.view.FTView;

public class Test {

	@org.junit.Test
	public void test() {
		JFileChooser ch = new JFileChooser();
		ch.setSelectedFile(new File("%username%\\" + "mysql"));
		int op = ch.showSaveDialog(null);
		if (op == JFileChooser.APPROVE_OPTION) {
			File temp = ch.getSelectedFile();
			System.out.println(temp.getAbsolutePath());
		} else System.out.println(ch.getSelectedFile());
	}

}
