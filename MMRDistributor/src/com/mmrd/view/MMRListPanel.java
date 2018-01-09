package com.mmrd.view;

import com.mmrd.tools.MMRModel;

import javax.swing.*;
import java.awt.*;

public class MMRListPanel extends JPanel {

    private MainFrame frame;
    private MMRModel model;
    private int height, width;

    protected MMRListPanel(MainFrame frame, MMRModel model) {
        this.frame = frame;
        this.model = model;
        height = frame.getHeight() / 5 * 3;
        width = frame.getWidth() / 2 - 20;
        init();
    }

    private JTable table;
    private JScrollPane scrollPane;

    private void init() {
        //初始化列表面板
        table = new JTable(model);
        table.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(width, height));
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        scrollPane.setBorder(BorderFactory.createTitledBorder("空闲内存链表"));
        this.add(scrollPane);
    }

    /**
     * 获得在列表中选中行的索引值
     *
     * @return 选中位置的序号
     */
    protected int getSelecedIndex() {
        return table.getSelectedRow();
    }
}
