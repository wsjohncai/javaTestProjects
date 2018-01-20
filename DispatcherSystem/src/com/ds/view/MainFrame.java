package com.ds.view;

import com.ds.common.JCB;
import com.ds.tools.DataModel;
import com.ds.tools.DispatcherThread;
import com.ds.tools.TimeHelper;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    private JPanel lPane, rPane;
    private JTable jobList;
    private DataModel jobModel, finModel, inModel, runModel;
    private JLabel ta_lb, wta_lb;
    private JTextField time_tf;
    private JButton start, stop, pause, speedup, add, del, mod;
    private String time = "10:00";
    public boolean isPaused = false;
    public boolean isStopped = true;
    private DispatcherThread thread;
    private int w = 700, h = 400;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initLeftPanel() {
        JScrollPane sc_start, sc_fin;
        jobModel = new DataModel(DataModel.TABLE_NOT_START);
        jobList = new JTable(jobModel);
        sc_start = new JScrollPane(jobList);
        sc_start.setPreferredSize(new Dimension(w / 2, h / 2 - 40));
        sc_start.setBorder(BorderFactory.createTitledBorder("作业队列"));
        jobList.setFillsViewportHeight(true);
        jobList.setRowSelectionAllowed(true);
        jobList.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        finModel = new DataModel(DataModel.TABLE_FINISHED);
        JTable finList = new JTable(finModel);
        sc_fin = new JScrollPane(finList);
        sc_fin.setPreferredSize(new Dimension(w / 2, h / 2 - 40));
        sc_fin.setBorder(BorderFactory.createTitledBorder("已完成作业"));
        finList.setFillsViewportHeight(true);
        finList.setRowSelectionAllowed(false);
        finList.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel showPane = new JPanel(new GridLayout(2, 2));
        showPane.setPreferredSize(new Dimension(w / 2, 60));
        showPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        JLabel ta, wta;
        ta = new JLabel("平均周转时间");
        wta = new JLabel("平均带权周转时间");
        ta_lb = new JLabel();
        wta_lb = new JLabel();
        ta.setFont(new Font("宋体", Font.PLAIN, 15));
        wta.setFont(new Font("宋体", Font.PLAIN, 15));
        wta_lb.setFont(new Font("微软雅黑", Font.BOLD, 18));
        ta_lb.setFont(new Font("微软雅黑", Font.BOLD, 18));
        ta_lb.setForeground(Color.BLUE);
        wta_lb.setForeground(Color.BLUE);
        showPane.add(ta);
        showPane.add(ta_lb);
        showPane.add(wta);
        showPane.add(wta_lb);

        lPane = new JPanel();
        lPane.setPreferredSize(new Dimension(w / 2, h));
        lPane.add(sc_start);
        lPane.add(sc_fin);
        lPane.add(showPane);
    }

    private void initRightPane() {
        JScrollPane sc_commited, sc_run;
        inModel = new DataModel(DataModel.TABLE_COMMITTED);
        JTable inList = new JTable(inModel);
        sc_commited = new JScrollPane(inList);
        sc_commited.setPreferredSize(new Dimension(w / 2 - 20, h / 2 - 60));
        sc_commited.setBorder(BorderFactory.createTitledBorder("就绪队列"));
        inList.setFillsViewportHeight(true);
        inList.setRowSelectionAllowed(false);
        inList.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        runModel = new DataModel(DataModel.TABLE_RUNNING);
        JTable runList = new JTable(runModel);
        sc_run = new JScrollPane(runList);
        sc_run.setPreferredSize(new Dimension(w / 2 - 20, h / 2 - 80));
        sc_run.setBorder(BorderFactory.createTitledBorder("调度进程"));
        runList.setFillsViewportHeight(true);
        runList.setRowSelectionAllowed(false);
        runList.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel btnPane = new JPanel(new GridLayout(2, 3));
        btnPane.setPreferredSize(new Dimension(w / 2 - 20, 70));
        start = new JButton("运行");
        stop = new JButton("停止");
        pause = new JButton("暂停");
        add = new JButton("添加作业");
        del = new JButton("删除");
        speedup = new JButton("加速 X1");
        speedup.setEnabled(false);
        btnPane.add(add);
        btnPane.add(del);
        btnPane.add(speedup);
        btnPane.add(start);
        btnPane.add(pause);
        btnPane.add(stop);

        JPanel showTime = new JPanel(new GridLayout(1, 3));
        showTime.setPreferredSize(new Dimension(w / 2 - 20, 20));
        JLabel time_lb = new JLabel("当前时间");
        time_tf = new JTextField(15);
        mod = new JButton("修改");
        time_tf.setHorizontalAlignment(SwingConstants.CENTER);
        time_tf.setText(time);
        time_lb.setFont(new Font("宋体", Font.PLAIN, 15));
        time_tf.setFont(new Font("宋体", Font.PLAIN, 15));
        mod.setFont(new Font("宋体", Font.PLAIN, 15));
        time_tf.setEditable(false);
        showTime.add(time_lb);
        showTime.add(time_tf);
        showTime.add(mod);

        rPane = new JPanel();
        rPane.setPreferredSize(new Dimension(w / 2, h));
        rPane.add(sc_commited);
        rPane.add(sc_run);
        rPane.add(btnPane);
        rPane.add(showTime);
    }

    void addJob(JCB job) {
        if (job != null)
            jobModel.addJob(job);
    }

    private void init() {
        this.setSize(w, h);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initLeftPanel();
        initRightPane();
        this.add(lPane, BorderLayout.WEST);
        this.add(rPane, BorderLayout.EAST);
        registerListener();

        this.pack();
        this.setLocation((getToolkit().getScreenSize().width - w) / 2,
                (getToolkit().getScreenSize().height - h) / 2);
        this.setVisible(true);
        this.validate();
    }

    //注册监听
    private void registerListener() {
        start.setActionCommand("start");
        stop.setActionCommand("stop");
        pause.setActionCommand("pause");
        speedup.setActionCommand("speedup");
        add.setActionCommand("add");
        del.setActionCommand("del");
        mod.setActionCommand("mod");

        start.addActionListener(this);
        stop.addActionListener(this);
        pause.addActionListener(this);
        speedup.addActionListener(this);
        add.addActionListener(this);
        del.addActionListener(this);
        mod.addActionListener(this);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //手动修改当前时间
    private void modifyTime() {
        if (mod.getText().equals("修改")) {
            time_tf.setEditable(true);
            mod.setText("确认");
            start.setEnabled(false);
        } else if (mod.getText().equals("确认")) {
            if (TimeHelper.isTimeFormat(time_tf.getText())) {
                time = time_tf.getText();
                time_tf.setEditable(false);
                mod.setText("修改");
                if (!stop.getText().equals("重置"))
                    start.setEnabled(true);
                this.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "时间格式有错");
                time_tf.requestFocus();
            }
        }

    }

    /**
     * 计算平均周转时间和平均带权周转时间
     */
    public void countAVG() {
        if (finModel.getRowCount() == 0)
            return;
        float totalf = 0f;
        int total = 0;
        int rows = finModel.getRowCount();
        for (int i = 0; i < rows; i++) {
            total += (int) finModel.getValueAt(i, 3);
        }
        for (int i = 0; i < rows; i++) {
            totalf += (double) finModel.getValueAt(i, 4);
        }
        ta_lb.setText((total * 1.0f / rows) + "");
        wta_lb.setText(totalf / rows + "");
    }

    /**
     * 更新显示时间的JLabel和刷新Table中的数据
     */
    public void refreshView() {
        time_tf.setText(time);
        jobModel.fireTableDataChanged();
        inModel.fireTableDataChanged();
        runModel.fireTableDataChanged();
        finModel.fireTableDataChanged();
    }

    //停止调度，恢复为未运行状态
    public void setStopped() {
        isStopped = true;
        speedup.setText("加速 X1");
        speedup.setEnabled(false);
        thread = null;
        mod.setEnabled(true);
        stop.setText("重置");
    }

    //新开一个线程进行调度，设置为运行状态
    private void startDispatch() {
        thread = new DispatcherThread(this);
        new Thread(thread).start();
        isStopped = false;
        start.setEnabled(false);
        speedup.setEnabled(true);
        mod.setEnabled(false);
        stop.setText("停止");
    }

    //设置运行速度
    private void setSpeed() {
        if (thread != null) {
            float speed = thread.getSpeed();
            if (speed == 8f) {
                thread.setSpeed(0.25f);
                speedup.setText("加速 X0.25");
            } else {
                float f = speed * 2;
                thread.setSpeed(f);
                speedup.setText("加速 X" + f);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "start":
                startDispatch();
                break;
            case "stop":
                //停止调度，并将数据恢复为初试状态
                if (isStopped) {
                    jobModel.setDataToDefault();
                    inModel.setDataToDefault();
                    runModel.setDataToDefault();
                    finModel.setDataToDefault();
                    refreshView();
                    start.setEnabled(true);
                } else {
                    setStopped();
                    refreshView();
                }
                break;
            case "pause":
                //处于运行状态时，暂停调度
                if (isPaused) {
                    pause.setText("暂停");
                } else
                    pause.setText("继续");
                isPaused = !isPaused;
                break;
            case "speedup":
                setSpeed();
                break;
            case "add":
                new NewJobDialog(this);
                jobModel.fireTableDataChanged();
                break;
            case "del":
                int row = jobList.getSelectedRow();
                JCB j = DataModel.getQueue(DataModel.TABLE_NOT_START).get(row);
                if (!jobModel.deleteJob(row)) {
                    JOptionPane.showMessageDialog(this, "不能删除就绪或者运行态的作业！");
                    break;
                }
                finModel.deleteJob(DataModel.getQueue(DataModel.TABLE_FINISHED).indexOf(j));
                jobModel.fireTableDataChanged();
                finModel.fireTableDataChanged();
                countAVG();
                break;
            case "mod":
                modifyTime();
                break;
        }
    }

    public static void main(String[] args) {
        new MainFrame().init();
    }

}
