/**
 *  @author WSJohnCai
 *  @���ܣ����Բ���
 */

package com.johncai;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LayoutTest extends JFrame {

	/**
	 *  @serial
	 */
	private static final long serialVersionUID = -1616722380655099304L;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LayoutTest frame = new LayoutTest();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.creatLayout();
			}
		});
	}

	//��������ʾ����
	JButton bt1,bt2= null;
	JTextArea textarea = null;
	JTextField textfield = null;
	JLabel jb1,jb2,jb3=null;
	
	public void creatLayout() {
		this.setSize(300, 300);
		this.setTitle("LayoutTest");
		this.setLocation(200, 200);;
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		textfield = new JTextField(50);
		this.add(textfield,BorderLayout.NORTH);
		
		textarea = new JTextArea();
		textarea.setEditable(false);
		this.add(textarea,BorderLayout.CENTER);
		
		bt1 = new JButton("��ť1");
		bt1.setFont(new Font("΢���ź�",Font.PLAIN,16));
		bt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				textfield.setText("");
				textarea.setText("");
				textfield.requestFocus();
			}
		});
		this.add(bt1,BorderLayout.SOUTH);
	}
}
