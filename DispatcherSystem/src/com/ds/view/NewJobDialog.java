package com.ds.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ds.common.JCB;
import com.ds.tools.TimeHelper;

//填写作业信息对话窗口
class NewJobDialog extends JDialog {
    private JPanel bg;
    private JTextField name_tx, time_tx, ttime_tx, pri_tx;
    private JCB job;
    private MainFrame parent;

    NewJobDialog(MainFrame parent) {
        super(parent, "添加作业", true);
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
        bg = new JPanel(new GridLayout(5,2));
        bg.setPreferredSize(new Dimension(200, 150));
        // bg.setMinimumSize(bg.getPreferredSize());

        JLabel name_lb = new JLabel("作业名");
        JLabel time_lb = new JLabel("提交时间");
        JLabel ttime_lb = new JLabel("运行时间");
        JLabel pri_lb = new JLabel("优先级");
        name_tx = new JTextField(18);
        time_tx = new JTextField(18);
        ttime_tx = new JTextField(18);
        pri_tx = new JTextField(18);
        time_tx.setToolTipText("时间格式：xx:xx");
        ttime_tx.setToolTipText("大于0开始的正整数");

        bg.add(name_lb);
        bg.add(name_tx);
        bg.add(time_lb);
        bg.add(time_tx);
        bg.add(ttime_lb);
        bg.add(ttime_tx);
        bg.add(pri_lb);
        bg.add(pri_tx);

        JButton summit = new JButton("提交");
        JButton cancel = new JButton("取消");
        bg.add(summit);
        bg.add(cancel);
        summit.addActionListener((e) -> {
            String name = name_tx.getText();
            String time = time_tx.getText();
            String ttime = ttime_tx.getText();
            String pri = pri_tx.getText();
            boolean nameVali = name.matches("[^\\s]+");
            boolean timeVali = TimeHelper.isTimeFormat(time);
            boolean ttimeVali = ttime.matches("\\d+");
            boolean priVali = pri.matches("\\d+");
            if (nameVali && timeVali && ttimeVali && priVali) {
                job = new JCB(name, Integer.parseInt(ttime), time, Integer.parseInt(pri));
                parent.addJob(job);
                NewJobDialog.this.dispose();
            } else {
                JOptionPane.showMessageDialog(NewJobDialog.this, "格式错误，请检查后重试", "输入有错", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancel.addActionListener((e) -> {
            NewJobDialog.this.dispose();
        });
    }

}
