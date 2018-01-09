package com.wsjohncai.view;

import com.wsjohncai.tool.DispatcherThread;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class StartDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 3935880619257483913L;
    private JButton bt1;
    private JButton bt2;
    private DispatcherView parent;

    StartDialog(DispatcherView parent) {
        super(parent, "选择一种算法", true);
        this.parent = parent;
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init();
        this.pack();
        this.setResizable(false);
        int x = parent.getX() + parent.getWidth() / 2 - this.getWidth() / 2;
        int y = parent.getY() + parent.getHeight() / 2 - this.getHeight() / 2;
        this.setLocation(x, y);
        this.setVisible(true);
        this.setAutoRequestFocus(true);
        this.validate();
    }

    private void init() {

        bt1 = new JButton("先来先服务算法");
        bt1.addActionListener(this);
        bt2 = new JButton("简单轮转算法");
        bt2.addActionListener(this);
        JButton bt3 = new JButton("高优先级优先算法");
        bt3.addActionListener(this);

        JPanel bg = new JPanel();
        bg.setPreferredSize(new Dimension(150, 100));
        bg.add(bt1);
        bg.add(bt2);
        bg.add(bt3);

        this.add(bg);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bt1)
            parent.startDispatch(DispatcherThread.AL_FCFS);
        else if (e.getSource() == bt2) {
            SetRoundTimeDialog setTime = new SetRoundTimeDialog(parent,"设置时间片大小",true);
        }
        else
            parent.startDispatch(DispatcherThread.AL_HRN);
        dispose();
    }
}

class SetRoundTimeDialog extends JDialog {
    /**
     * 构造一个设置时间片的对话框
     */
    SetRoundTimeDialog(DispatcherView owner, String title, boolean modal) {
        super(owner, title, modal);
        this.parent = owner;
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init();
        this.pack();
        this.setResizable(false);
        int x = parent.getX() + parent.getWidth() / 2 - this.getWidth() / 2;
        int y = parent.getY() + parent.getHeight() / 2 - this.getHeight() / 2;
        this.setLocation(x, y);
        this.setVisible(true);
        this.setAutoRequestFocus(true);
        this.validate();
    }

    private DispatcherView parent;

    private void init() {
        JTextField textField = new JTextField(15);
        textField.setToolTipText("请输入时间片大小");
        JButton confirm = new JButton("确定");
        JButton cancel = new JButton("取消");
        confirm.addActionListener(e -> {
            String s = textField.getText();
            if(s.matches("\\d+")) {
                parent.setRound(Integer.parseInt(s));
                parent.startDispatch(DispatcherThread.AL_SR);
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "输入有误，请输入大于0的正整数");
            }
        });
        JPanel bg = new JPanel();
        bg.setPreferredSize(new Dimension(100,50));
        bg.add(textField);
        bg.add(confirm);
        bg.add(cancel);

        this.add(bg);
    }
}
