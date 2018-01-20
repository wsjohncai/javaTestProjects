package com.sss.view;

import com.sss.datamodel.BaseTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class BaseTablePanel extends JPanel {

    private JTable table;
    private BaseTableModel model;
    private TableRowSorter<BaseTableModel> sorter;
    private int w, h;

    private void initTable() {
        table = new JTable(model);
        JScrollPane sc = new JScrollPane(table);
        table.setRowSelectionAllowed(true);
        table.setFillsViewportHeight(true);
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sc.setPreferredSize(new Dimension(w, h));
        sc.setBorder(BorderFactory.createTitledBorder("数据表"));
        this.add(sc);
    }

    BaseTablePanel(int w, int h, int tableType) {
        this.w = w;
        this.h = h;
        model = new BaseTableModel(tableType);
        initTable();
    }

    BaseTableModel getModel() {
        return model;
    }

    JTable getTable() {
        return table;
    }

    TableRowSorter<BaseTableModel> getSorter() {
        return sorter;
    }
}
