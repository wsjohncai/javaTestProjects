package com.sss.view;

import com.sss.datamodel.BaseTableModel;

import javax.swing.*;
import java.awt.*;

public class AddorAlterDialog extends JDialog {
    public static final int ADD = 0;
    public static final int UPDATE = 1;

    private int row;
    private BaseFrame parent;
    private BaseTableModel model;
    private int type;

    AddorAlterDialog(BaseFrame parent, BaseTableModel model, int type) {
        super(parent, type == ADD ? "添加" : "更新", false);
        this.parent = parent;
        this.model = model;
        this.type = type;
    }

    void init() {
        String[] columnNames = model.getColumnString();
        String[] columnIds = model.getColumnIds();
        int len = columnNames.length;
        JPanel bg = new JPanel(new GridLayout(len + 1, 2));
        bg.setPreferredSize(new Dimension(300, len * 25 + 25));
        JTextField[] fields = new JTextField[len];
        for (int i = 0; i < len; i++) {
            JLabel label = new JLabel(columnNames[i] + "(" + columnIds[i] + ")");
            JTextField textField = new JTextField(15);
            if (type == UPDATE)
                textField.setText((String) model.getValueAt(row, i));
            fields[i] = textField;
            bg.add(label);
            bg.add(textField);
        }
        JButton comfirm, cancel;
        comfirm = new JButton("确认");
        cancel = new JButton("取消");
        cancel.addActionListener(e -> {
            AddorAlterDialog.this.dispose();
        });
        comfirm.addActionListener(e -> {
            String table = model.getTableName();
            String[] params;
            if (type == ADD)
                params = new String[len];
            else
                params = new String[len + 1];
            StringBuilder place = new StringBuilder();
            StringBuilder change = new StringBuilder();
            for (int i = 0; i < len; i++) {
                place.append("?,");
                params[i] = fields[i].getText();
                change.append(columnIds[i]).append("=?,");
            }
            String s = place.toString();
            String s1 = change.toString();
            String sql;
            if (type == ADD)
                sql = "insert into " + table + " values("
                        + s.substring(0, s.lastIndexOf(',')) + ")";
            else {
                sql = "update " + table + " set " + s1.substring(0,
                        s1.lastIndexOf(',')) + " where " + columnIds[0] + "=?";
                params[params.length - 1] = params[0];
            }
            if (!model.alterData(sql, params))
                JOptionPane.showMessageDialog(AddorAlterDialog.this, "操作失败");
            else {
                JOptionPane.showMessageDialog(AddorAlterDialog.this, "操作成功");
                AddorAlterDialog.this.dispose();
                model.updateData();
            }
        });
        bg.add(comfirm);
        bg.add(cancel);
        this.add(bg);
        this.pack();
        int x = parent.getX() + parent.getWidth() / 2 - this.getWidth() / 2;
        int y = parent.getY() + parent.getHeight() / 2 - this.getHeight() / 2;
        this.setLocation(x, y);
        this.setVisible(true);
        this.setAutoRequestFocus(true);
        this.validate();
    }

    AddorAlterDialog setSelectedRow(int row) {
        this.row = row;
        return AddorAlterDialog.this;
    }
}
