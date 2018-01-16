package com.sss.view;

import com.sss.datamodel.BaseTableModel;

import javax.swing.*;
import java.awt.*;

public class BaseTable extends JPanel {

    private JTable table;
    private BaseTableModel model;
    private int w, h;

    protected void initTable() {
        table = new JTable(model);
        JScrollPane sc = new JScrollPane(table);
        table.setRowSelectionAllowed(true);
        table.setFillsViewportHeight(true);
        sc.setPreferredSize(new Dimension(w, h));
        this.add(sc);
    }

    BaseTable(int w, int h, int tableType) {
        this.w = w;
        this.h = h;
        model = new BaseTableModel();
    }

    public BaseTableModel getModel() {
        return model;
    }
}
