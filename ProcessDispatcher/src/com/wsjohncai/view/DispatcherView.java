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

import com.wsjohncai.common.PCB;
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
    private JLabel time, round; //显示时间和时间片大小
    private MyTableModel model; //自定义的Table数据模型
    public boolean isPaused = false; //用于表示调度是否处于暂停状态
    public boolean isStopped = true; //用于表示是否处于调度状态
    int roundTime = 1; //默认时间片为1
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
        this.setTitle("进程调度模拟");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(width / 5 * 2, 310);
        this.setResizable(false);

        //初始化JTable及其模型
        model = new MyTableModel();
        waiting_table = new JTable(model);
        waiting_table.setPreferredScrollableViewportSize(new Dimension(this.getWidth() - 40, 150));
        waiting_table.setRowSelectionAllowed(true);
        waiting_table.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        waiting_scroll = new JScrollPane(waiting_table);
        waiting_scroll.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        waiting_scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "作业队列"));
        waiting_pane = new JPanel();
        waiting_pane.add(waiting_scroll);
        this.add(waiting_pane, BorderLayout.NORTH);

        //设置其他界面组件
        time = new JLabel("当前时间：0");
        round = new JLabel("当前时间片长度：" + roundTime);
        time.setFont(new Font("等线体", Font.PLAIN, 12));
        round.setFont(new Font("等线体", Font.PLAIN, 12));
        summit = new JButton("提交");
        summit.setToolTipText("提交一个进程到等待列表");
        summit.setActionCommand("summit");
        summit.addActionListener(this);
        execute = new JButton("开始");
        execute.setToolTipText("开始执行进程调度");
        execute.setActionCommand("exec");
        execute.addActionListener(this);
        stop = new JButton("停止");
        stop.setActionCommand("stop");
        stop.addActionListener(this);
        pause = new JButton("暂停");
        pause.setActionCommand("pause");
        pause.addActionListener(this);
        delete = new JButton("删除");
        delete.setToolTipText("删除选定的进程");
        delete.setActionCommand("del");
        delete.addActionListener(this);
        speedup = new JButton("加速 X1");
        speedup.setActionCommand("speedup");
        speedup.addActionListener(this);
        op_pane = new JPanel(null);
        btn_pane = new JPanel();
        op_pane.setPreferredSize(new Dimension(this.getWidth() - 50, this.getHeight() - waiting_pane.getHeight() - 20));
        op_pane.add(time);
        time.setBounds(30, 5, this.getWidth() / 5, 30);
        time.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        op_pane.add(round);
        round.setBounds(this.getWidth() / 5 + 35, 5, this.getWidth() / 5, 30);
        round.setBorder(BorderFactory.createLineBorder(Color.GRAY));
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

    /**
     * 获得时间片长度
     */
    public int getRound() {
        return roundTime;
    }

    /**
     * 设置时间片长度为t
     */
    public void setRound(int t) {
        roundTime = t;
        round.setText("当前时间片长度：" + t);
    }

    /**
     * 更新显示时间的JLabel和刷新Table中的数据
     */
    public void updateCurTime() {
        time.setText("当前时间: " + TIME);
        model.fireTableDataChanged();
    }

    //添加一个新进程
    void addProc(PCB proc) {
        if (proc != null)
            model.addProc(proc);
    }

    //停止调度，恢复为未运行状态
    public void setStopped() {
        isStopped = true;
        speedup.setText("加速 X1");
        speedup.setEnabled(false);
        dispatcher = null;
        execute.setEnabled(true);
    }

    //新开一个线程进行调度，设置为运行状态
    void startDispatch(int al) {
        dispatcher = new DispatcherThread(this, model.getQueue(), al);
        dispatcher.start();
        isStopped = false;
        execute.setEnabled(false);
        speedup.setEnabled(true);
    }

    //设置运行速度
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
                //打开算法选择对话框
                new StartDialog(this);
                break;
            case "summit":
                //打开添加新进程的对话框
                new NewProcDialog(this);
                model.fireTableDataChanged();
                break;
            case "del":
                //删除选定的进程
                int row = waiting_table.getSelectedRow();
                if (model.deleteProc(row))
                    model.fireTableDataChanged();
                else
                    JOptionPane.showMessageDialog(this, "无法删除，在运行中", "无法删除", JOptionPane.ERROR_MESSAGE);
                break;
            case "pause":
                //处于运行状态时，暂停调度
                if (isPaused) {
                    pause.setText("暂停");
                } else
                    pause.setText("继续");
                isPaused = !isPaused;
                break;
            case "stop":
                //停止调度，并将数据恢复为初试状态
                setStopped();
                model.setDataToDefault();
                TIME = 0;
                updateCurTime();
                break;
            case "speedup":
                //加速按钮
                setSpeed();
                break;
            default:
        }

        this.requestFocus();
    }

}
