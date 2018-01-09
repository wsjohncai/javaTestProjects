package com.mmrd.view;

import com.mmrd.common.JCB;
import com.mmrd.tools.JobModel;

import javax.swing.*;
import java.awt.*;

public class AddJobDialog extends JDialog{
    private JPanel bg;
    private JTextField name_tx, size_tx;
    private JCB jcb;

    AddJobDialog(MainFrame parent) {
        super(parent, "添加作业", true);
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
        bg.setPreferredSize(new Dimension(200, 90));
        // bg.setMinimumSize(bg.getPreferredSize());

        JLabel name_lb = new JLabel("作业名  ");
        JLabel size_lb = new JLabel("作业大小");
        name_tx = new JTextField(18);
        size_tx = new JTextField(18);

        bg.add(name_lb);
        bg.add(name_tx);
        bg.add(size_lb);
        bg.add(size_tx);

        JButton summit = new JButton("确定");
        JButton cancel = new JButton("取消");
        bg.add(summit);
        bg.add(cancel);
        summit.addActionListener(e -> {
            String name = name_tx.getText();
            String size = size_tx.getText();
            boolean nameVali = name.matches("[^\\s]+");
            boolean sizeVali = size.matches("\\d+");
            if (nameVali && sizeVali) {
                jcb = new JCB(name, Integer.parseInt(size));
                JobModel.addJob(jcb);
                AddJobDialog.this.dispose();
            } else {
                JOptionPane.showMessageDialog(AddJobDialog.this, "格式错误，请检查后重试", "输入有错", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancel.addActionListener(e -> AddJobDialog.this.dispose());
    }

}
