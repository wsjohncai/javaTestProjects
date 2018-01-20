package com.sss.view;

import com.sss.datamodel.BaseTableModel;

import javax.swing.*;
import java.awt.*;

class InfoPanel extends JPanel {
    private BaseTablePanel tablePanel;
    private JPanel searchP;

    InfoPanel(int w, int h, int table_type, BaseFrame frame) {
        this.setPreferredSize(new Dimension(w, h));
        //查询信息的面板
        tablePanel = new BaseTablePanel(w, h / 6 * 5, table_type);
        this.add(tablePanel);

        JButton add = new JButton("增加");
        JButton del = new JButton("删除");
        JButton alter = new JButton("修改");
        add.setFont(new Font("微软雅黑",Font.PLAIN,18));
        del.setFont(new Font("微软雅黑",Font.PLAIN,18));
        alter.setFont(new Font("微软雅黑",Font.PLAIN,18));
        add.addActionListener(e -> {
            new AddorAlterDialog(frame, getModel(), AddorAlterDialog.ADD)
                    .setSelectedRow(getTable().getSelectedRow()).init();
        });
        del.addActionListener(e -> {
            int row = getTable().getSelectedRow();
            if (row != -1)
                if (getModel().deleteData(row))
                    JOptionPane.showMessageDialog(InfoPanel.this, "删除成功");
                else
                    JOptionPane.showMessageDialog(InfoPanel.this, "删除失败");
            else
                JOptionPane.showMessageDialog(InfoPanel.this, "没有选中行");
        });
        alter.addActionListener(e -> {
            int row = getTable().getSelectedRow();
            if (row != -1) {
                new AddorAlterDialog(frame, getModel(), AddorAlterDialog.UPDATE)
                        .setSelectedRow(row).init();
            } else
                JOptionPane.showMessageDialog(InfoPanel.this, "没有选中行");
        });
        JPanel optPanel = new JPanel();
        optPanel.setPreferredSize(new Dimension(w / 2, h / 12));
        optPanel.add(add);
        optPanel.add(alter);
        optPanel.add(del);
        initSearchBox(w / 2, h / 12);

        this.add(searchP);
        this.add(optPanel);
    }

    private void initSearchBox(int w, int h) {
        searchP = new JPanel();
        searchP.setPreferredSize(new Dimension(w, h));
        JTextField searchBox = new JTextField(15);
        searchBox.setToolTipText("输入需要查找的关键字，如果是在制定列查找，则输入“关键字,列号”");
        JButton search = new JButton("查询");
        search.setFont(new Font("微软雅黑",Font.PLAIN,15));
        searchBox.setFont(new Font("微软雅黑",Font.PLAIN,15));
        searchP.add(searchBox);
        searchP.add(search);
        search.addActionListener(e -> {
            String s = searchBox.getText().trim();
            if (s.length() == 0)
                tablePanel.getSorter().setRowFilter(null);
            else {
                if (s.matches(".+,\\d+")) {
                    String s1[] = s.split(",");
                    tablePanel.getSorter().setRowFilter(RowFilter.regexFilter(s1[0]
                            , Integer.parseInt(s1[1])));
                } else
                    tablePanel.getSorter().setRowFilter(RowFilter.regexFilter(s));
            }
        });
    }

    private BaseTableModel getModel() {
        return tablePanel.getModel();
    }

    private JTable getTable() {
        return tablePanel.getTable();
    }
}
