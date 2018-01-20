package com.sss.view;

import com.sss.datamodel.BaseTableModel;
import com.sss.datamodel.DataBaseTool;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {
    private String id;
    private int[] table_type;

    BaseFrame(String id) {
        this.id = id;
        init();
        initUI();
    }

    void init() {
        DataBaseTool db = new DataBaseTool();
        String sql = "select dept from stuff where s_id=?";
        String job = db.queryForOneResult(sql, id);
        if (job.equals("m01")) {
            table_type = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        } else if (job.equals("r11")) {
            table_type = new int[]{2, 6, 7};
        } else if (job.equals("s15")) {
            table_type = new int[]{0, 1, 2, 3, 4};
        } else if (job.equals("s24")) {
            table_type = new int[]{1, 4, 8, 11};
        }
    }

    private void initUI() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int sw = getToolkit().getScreenSize().width;
        int sh = getToolkit().getScreenSize().height;
        int w = sw/2, h = 600;
        this.setSize(w, h);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(w,h));
        for (int i : table_type) {
            tabbedPane.addTab(BaseTableModel.NAMES[i], new InfoPanel(w-40, h-50, i, this));
        }
        this.add(tabbedPane);
        w = this.getWidth();
        h = this.getHeight();
        this.setLocation((sw-w)/2,(sh-h)/2);
        this.setVisible(true);
        this.validate();
    }
}
