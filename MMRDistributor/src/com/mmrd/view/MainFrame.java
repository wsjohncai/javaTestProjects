package com.mmrd.view;

import com.mmrd.common.JCB;
import com.mmrd.tools.AllotAlgorithm;
import com.mmrd.tools.JobModel;
import com.mmrd.tools.MMRModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.mmrd.tools.AllotAlgorithm.*;

public class MainFrame extends JFrame implements ActionListener {

    private static int al_sel = FF;//选择的算法，默认为首次适应算法
    private MMRListPanel mmrList;
    private JobListPanel jobList;
    private MMRModel mmrModel;
    private JobModel jobModel;
    private JButton rcyAll, rcy, addMmr, addJob,
            allotAll, allot, delMmr, delJob, genRam;
    private JRadioButton rff, ff, opf, wf;//循环首次适应，首次适应，最佳适应，最坏适应选项
    private int w = 600, h = 600, sw = getToolkit().getScreenSize().width,
            sh = getToolkit().getScreenSize().height;

    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(w, h);
        this.setTitle("内存分配程序");

        //初始化列表面板
        mmrModel = new MMRModel();
        mmrList = new MMRListPanel(this, mmrModel);
        jobModel = new JobModel();
        jobList = new JobListPanel(this, jobModel);
        JPanel listPane = new JPanel();
        listPane.add(mmrList);
        listPane.add(jobList);

        //初始化算法选择和按钮面板
        rff = new JRadioButton("循环首次适应算法");
        ff = new JRadioButton("首次适应算法");
        opf = new JRadioButton("最佳适应算法");
        wf = new JRadioButton("最坏适应算法");
        ButtonGroup group = new ButtonGroup();
        group.add(ff);
        group.add(rff);
        group.add(opf);
        group.add(wf);
        ff.setSelected(true);
        JPanel alPane = new JPanel(new GridLayout(2, 2));
//        alPane.setPreferredSize(new Dimension(280, 90));
        alPane.add(ff);
        alPane.add(rff);
        alPane.add(opf);
        alPane.add(wf);
        alPane.setBorder(BorderFactory.createTitledBorder("选择一种算法"));
        rcy = new JButton("回收选中作业");
        rcyAll = new JButton("回收全部内存");
        addJob = new JButton("添加作业");
        addMmr = new JButton("添加空闲内存");
        delJob = new JButton("删除选中作业");
        delMmr = new JButton("删除选定内存");
        allot = new JButton("分配选定作业");
        allotAll = new JButton("分配所有作业");
        genRam = new JButton("随机生成数据");
        JPanel btnPane = new JPanel(new GridLayout(3, 3));
        btnPane.add(rcy);
        btnPane.add(allotAll);
        btnPane.add(addJob);
        btnPane.add(rcyAll);
        btnPane.add(allot);
        btnPane.add(delJob);
        btnPane.add(addMmr);
        btnPane.add(delMmr);
        btnPane.add(genRam);
        btnPane.setBorder(BorderFactory.createTitledBorder("操作"));
        JPanel bmPane = new JPanel();
        bmPane.add(alPane);
        bmPane.add(btnPane);
        addListener();

        this.add(listPane, BorderLayout.CENTER);
        this.add(bmPane, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
        this.setLocation((sw - w) / 2, (sh - h) / 2);
        this.validate();
    }

    private void addListener() {
        ff.setActionCommand("ff");
        rff.setActionCommand("rff");
        opf.setActionCommand("opf");
        wf.setActionCommand("wf");
        rcy.setActionCommand("rcy");
        rcyAll.setActionCommand("rcyAll");
        allotAll.setActionCommand("allotAll");
        allot.setActionCommand("allot");
        delJob.setActionCommand("delJob");
        delMmr.setActionCommand("delMmr");
        addJob.setActionCommand("addJob");
        addMmr.setActionCommand("addMmr");
        genRam.setActionCommand("genRam");

        ff.addActionListener(this);
        rff.addActionListener(this);
        wf.addActionListener(this);
        opf.addActionListener(this);
        rcy.addActionListener(this);
        rcyAll.addActionListener(this);
        addJob.addActionListener(this);
        addMmr.addActionListener(this);
        delMmr.addActionListener(this);
        delJob.addActionListener(this);
        allot.addActionListener(this);
        allotAll.addActionListener(this);
        genRam.addActionListener(this);
    }

    public static int getAl_Sel() {
        return al_sel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "ff":
                al_sel = FF;
                jobModel.recycleAll();
                break;
            case "rff":
                al_sel = RFF;
                jobModel.recycleAll();
                break;
            case "opf":
                al_sel = OPF;
                jobModel.recycleAll();
                break;
            case "wf":
                al_sel = WF;
                AllotAlgorithm.setSeqToDefault();
                jobModel.recycleAll();
                break;
            case "rcy":
                int idx = jobList.getSelecedIndex();
                JCB job = JobModel.getQueue().get(idx);
                if (job.getStore_start_addr() == JCB.NOT_STORE) {
                    JOptionPane.showMessageDialog(this, "该作业未分配内存"
                            , "错误", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                jobModel.recycleJob(idx);
                break;
            case "rcyAll":
                jobModel.recycleAll();
                break;
            case "allotAll":
                for (JCB j1 : JobModel.getQueue()) {
                    call(j1);
                }
                break;
            case "allot":
                job = JobModel.getQueue().get(jobList.getSelecedIndex());
                if (job.getStore_start_addr() != JCB.NOT_STORE) {
                    JOptionPane.showMessageDialog(this, "该作业已分配内存"
                            , "错误", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                call(job);
                break;
            case "delJob":
                jobModel.deleteJob(jobList.getSelecedIndex());
                break;
            case "delMmr":
                mmrModel.deleteMMR(mmrList.getSelecedIndex());
                break;
            case "addJob":
                new AddJobDialog(this);
                break;
            case "addMmr":
                new AddMmrDialog(this);
                break;
            case "genRam":
                mmrModel.initData();
                jobModel.initData();
                break;
            default:
                break;
        }
        MMRModel.reorganize();
        jobModel.fireTableDataChanged();
        mmrModel.fireTableDataChanged();
    }

    public static void main(String[] args) {
        new MainFrame().init();
    }
}
