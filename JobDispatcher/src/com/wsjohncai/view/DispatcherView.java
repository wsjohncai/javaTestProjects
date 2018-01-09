package com.wsjohncai.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;

import com.wsjohncai.common.JCB;
import com.wsjohncai.tool.DispatcherThread;
import com.wsjohncai.tool.MyTableModel;

public class DispatcherView extends JFrame implements ActionListener {

    private static final long serialVersionUID = 119748115546105132L;
    private static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static int TIME = 0;
    private JPanel waiting_pane, op_pane, btn_pane;
    private JTable waiting_table;
    private JScrollPane waiting_scroll;
    private JButton summit, execute, stop, pause, delete, speedup;
    private JLabel ta, wta, time;
    private MyTableModel model;
    public boolean isPaused = false;
    public boolean isStopped = true;
    private DispatcherThread dispatcher;

    // 设置界面风格
    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        createUI();
    }

    private void createUI() {
        this.setTitle("作业调度模拟");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(width / 5 * 2, 310);
        this.setResizable(false);

        model = new MyTableModel();
        waiting_table = new JTable(model);
        waiting_table.setPreferredScrollableViewportSize(new Dimension(this.getWidth() - 40, 150));
        waiting_table.getColumnModel().getColumn(0).setPreferredWidth(50);
        waiting_table.getColumnModel().getColumn(4).setPreferredWidth(40);
        waiting_table.getColumnModel().getColumn(6).setPreferredWidth(70);
        waiting_table.getColumnModel().getColumn(7).setPreferredWidth(100);
        waiting_table.setRowSelectionAllowed(true);
        waiting_table.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        // waiting_table.setRowSelectionAllowed(true);
        waiting_scroll = new JScrollPane(waiting_table);
        // waiting_scroll.setPreferredSize(new Dimension(this.getWidth() - 20,
        // this.getHeight() - 50));
        waiting_scroll.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        waiting_scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "作业队列"));
        waiting_pane = new JPanel();
        waiting_pane.add(waiting_scroll);
        // waiting_scroll.setBounds(3, 25, this.getWidth() - 20,
        // this.getHeight() - 50);
        this.add(waiting_pane, BorderLayout.NORTH);

        ta = new JLabel("平均周转时间：");
        wta = new JLabel("平均带权周转时间：");
        time = new JLabel("当前时间：0");
        time.setFont(new Font("等线体", Font.PLAIN, 12));
        ta.setFont(new Font("等线体", Font.PLAIN, 12));
        wta.setFont(new Font("等线体", Font.PLAIN, 12));
        summit = new JButton("提交");
        summit.setToolTipText("提交一个作业到等待列表");
        summit.setActionCommand("summit");
        summit.addActionListener(this);
        execute = new JButton("开始");
        execute.setToolTipText("开始执行作业调度");
        execute.setActionCommand("exec");
        execute.addActionListener(this);
        stop = new JButton("停止");
        stop.setActionCommand("stop");
        stop.addActionListener(this);
        pause = new JButton("暂停");
        pause.setActionCommand("pause");
        pause.addActionListener(this);
        delete = new JButton("删除");
        delete.setToolTipText("删除选定的作业");
        delete.setActionCommand("del");
        delete.addActionListener(this);
        speedup = new JButton("加速 X1");
        speedup.setActionCommand("speedup");
        speedup.addActionListener(this);
        btn_pane = new JPanel();
        op_pane = new JPanel(null);
        op_pane.setPreferredSize(new Dimension(this.getWidth() - 50, this.getHeight() - waiting_pane.getHeight() - 20));
        op_pane.add(time);
        time.setBounds(10, 5, this.getWidth() / 5, 30);
        time.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        op_pane.add(ta);
        ta.setBounds(10 + this.getWidth() / 5 + 5, 5, this.getWidth() / 5 * 2 - 20, 30);
        ta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        op_pane.add(wta);
        wta.setBounds(this.getWidth() / 5 * 3, 5, this.getWidth() / 5 * 2 - 20, 30);
        wta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btn_pane.add(summit);
        btn_pane.add(delete);
        btn_pane.add(execute);
        btn_pane.add(pause);
        btn_pane.add(stop);
        btn_pane.add(speedup);
        op_pane.add(btn_pane);
        btn_pane.setBounds(0, 32, this.getWidth(), 40);
        this.add(op_pane, BorderLayout.CENTER);

        this.setLocation(width / 2 - this.getWidth() / 2, height / 2 - this.getHeight() / 2);
        this.setVisible(true);
        this.validate();
    }

    public void updateAfterAJob() {
        Vector<JCB> q = model.getQueue();
        double t1 = 0;
        double t2 = 0;
        int count = 0;
        for (JCB j : q) {
            if (j.getStatus() != JCB.FINISHED)
                continue;
            t1 += j.getTurnaround_time();
            t2 += j.getWeigh_turnaround_time();
            count++;
        }
        ta.setText("平均周转时间：" + t1 * 1.0 / count);
        wta.setText("平均带权周转时间：" + t2 * 1.0 / count);
    }

    public void setStopped() {
        isStopped = true;
        speedup.setText("加速 X1");
        speedup.setEnabled(false);
        dispatcher = null;
        execute.setEnabled(true);
    }

    public void updateCurTime() {
        SwingUtilities.invokeLater(() -> {
            time.setText("当前时间: " + TIME);
            model.fireTableDataChanged();
        });
    }

    void addJob(JCB job) {
        if (job != null)
            model.addJob(job);
    }

    void startDispatch(int al) {
        dispatcher = new DispatcherThread(this, model.getQueue(), al);
        dispatcher.start();
        isStopped = false;
        execute.setEnabled(false);
        speedup.setEnabled(true);
    }

    private void setSpeed() {
        if(dispatcher != null ) {
            int speed = dispatcher.getProcessSpeed();
            if (speed == 8) {
                dispatcher.setProcessSpeed(1);
                speedup.setText("加速 X1");
            } else {
                int f = speed * 2;
                dispatcher.setProcessSpeed(f);
                speedup.setText("加速 X" + f);
            }
        }
    }

    public static void main(String[] args) {
        DispatcherView view = new DispatcherView();
        view.init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "exec":
                isStopped = false;
                new StartDialog(this);
                break;
            case "summit":
                new NewJobDialog(this);
                model.fireTableDataChanged();
                break;
            case "del":
                int row = waiting_table.getSelectedRow();
                if (model.deleteJob(row))
                    model.fireTableDataChanged();
                else
                    JOptionPane.showMessageDialog(this, "无法删除，作业在运行中", "无法删除", JOptionPane.ERROR_MESSAGE);
                break;
            case "pause":
                if (isPaused) {
                    pause.setText("暂停");
                } else
                    pause.setText("继续");
                isPaused = !isPaused;
                break;
            case "stop":
                setStopped();
                model.setDataToDefault();
                TIME = 0;
                updateCurTime();
                break;
            case "speedup":
                setSpeed();
                break;
            default:
        }

        this.requestFocus();
    }

}
