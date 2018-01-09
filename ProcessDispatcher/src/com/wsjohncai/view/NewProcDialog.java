package com.wsjohncai.view;

import com.wsjohncai.common.PCB;

import javax.swing.*;
import java.awt.*;

//填写进程信息对话窗口
class NewProcDialog extends JDialog {
    private static final long serialVersionUID = -3183179636765136074L;
    private JPanel bg;
    private JTextField name_tx, time_tx, ttime_tx, prior_tx;
    private PCB proc;
    private DispatcherView parent;

    NewProcDialog(DispatcherView parent) {
        super(parent, "添加进程", true);
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
        bg.setPreferredSize(new Dimension(200, 130));
        // bg.setMinimumSize(bg.getPreferredSize());

        JLabel name_lb = new JLabel("进程名  ");
        JLabel time_lb = new JLabel("提交时间");
        JLabel ttime_lb = new JLabel("服务耗时");
        JLabel prior_lb = new JLabel("优先级");
        name_tx = new JTextField(18);
        time_tx = new JTextField(18);
        ttime_tx = new JTextField(18);
        prior_tx = new JTextField(18);
        time_tx.setToolTipText("从0开始的正整数，数字越大表示时间越晚");
        ttime_tx.setToolTipText("大于0的正整数");
        prior_tx.setToolTipText("大于0的正整数,数字越小优先级越高");

        bg.add(name_lb);
        bg.add(name_tx);
        bg.add(time_lb);
        bg.add(time_tx);
        bg.add(ttime_lb);
        bg.add(ttime_tx);
        bg.add(prior_lb);
        bg.add(prior_tx);

        JButton summit = new JButton("提交");
        JButton cancel = new JButton("取消");
        bg.add(summit);
        bg.add(cancel);
        summit.addActionListener(e -> {
            String name = name_tx.getText();
            String time = time_tx.getText();
            String ttime = ttime_tx.getText();
            String prior = prior_tx.getText();
            boolean nameVali = name.matches("[^\\s]+");
            boolean timeVali = time.matches("\\d+");
            boolean ttimeVali = ttime.matches("\\d+");
            boolean priorValid = prior.matches("\\d+");
            if (nameVali && timeVali && ttimeVali && priorValid) {
                proc = new PCB(name, Integer.parseInt(time), Integer.parseInt(ttime), Integer.parseInt(prior));
                parent.addProc(proc);
                NewProcDialog.this.dispose();
            } else {
                JOptionPane.showMessageDialog(NewProcDialog.this, "格式错误，请检查后重试", "输入有错", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancel.addActionListener(e -> NewProcDialog.this.dispose());
    }

}
