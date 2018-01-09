package com.mmrd.view;

import com.mmrd.common.MMRBlock;
import com.mmrd.tools.MMRModel;

import javax.swing.*;
import java.awt.*;

public class AddMmrDialog extends JDialog {
    private static final long serialVersionUID = -3183179636765136074L;
    private JPanel bg;
    private JTextField start_tx, length_tx;
    private MMRBlock mmr;

    AddMmrDialog(MainFrame parent) {
        super(parent, "添加空闲内存块", true);
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

        JLabel start_lb = new JLabel("起始地址  ");
        JLabel length_lb = new JLabel("内存大小");
        start_tx = new JTextField(18);
        length_tx = new JTextField(18);

        bg.add(start_lb);
        bg.add(start_tx);
        bg.add(length_lb);
        bg.add(length_tx);

        JButton summit = new JButton("确定");
        JButton cancel = new JButton("取消");
        bg.add(summit);
        bg.add(cancel);
        summit.addActionListener(e -> {
            String start = start_tx.getText();
            String length = length_tx.getText();
            boolean startVali = start.matches("\\d+");
            boolean lengthVali = length.matches("\\d+");
            if (startVali && lengthVali) {
                mmr = new MMRBlock(Integer.parseInt(start), Integer.parseInt(length));
                if (!MMRModel.addMMR(mmr)) {
                    JOptionPane.showMessageDialog(AddMmrDialog.this,
                            "内存块范围错误，请确保内存块没有重叠情况",
                            "内存块叠加", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    MMRModel.reorganize();
                    AddMmrDialog.this.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(AddMmrDialog.this,
                        "格式错误，请检查后重试", "输入有错", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancel.addActionListener(e -> AddMmrDialog.this.dispose());
    }

}
