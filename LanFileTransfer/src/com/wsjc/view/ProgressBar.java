package com.wsjc.view;

import com.wsjc.connection.FileRecv;
import com.wsjc.connection.FileSend;
import com.wsjc.tools.ThreadMgr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ProgressBar extends JPanel implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int ACCEPT_FILE = 1;
    public static final int SEND_FILE = 0;
    private JTextArea text;
    private JButton openFile, openDir, close;
    private String progress;
    private String progressPath;
    private File file;
    private FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
    private boolean isFinished = false;
    private int type;

    /**
     * 构造一个可以显示进度的组件
     *
     * @param file 正在传输的文件
     * @param width 文件长度
     * @param type 传输类型
     */
    ProgressBar(int type, File file, int width) {
        this.file = file;
        this.progress = file.getName();
        this.progressPath = file.getAbsolutePath();
        this.type = type;
        this.setMinimumSize(new Dimension(width - 30, 50));
        this.setMaximumSize(new Dimension(width - 30, 90));
        this.setPreferredSize(new Dimension(width - 30, 70));
        this.setBackground(Color.LIGHT_GRAY);
        text = new JTextArea();
        openFile = new JButton("取消");
        openDir = new JButton("打开文件夹");
        close = new JButton("关闭");

        text.setPreferredSize(new Dimension(width - 30, 30));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font("Consolas", Font.PLAIN, 12));
        text.setEditable(false);
        text.setOpaque(false);
        openFile.addActionListener(this);
        openDir.addActionListener(this);
        close.addActionListener(this);

        this.add(text);
        this.add(openFile);
        this.add(openDir);
        this.add(close);

    }

    public void setLabelText(String x) {
        text.setText(x);
        text.setToolTipText(x);
        if (x.equals("发送成功") && type == ProgressBar.SEND_FILE) {
            view.cancelProgress(progress);
            FileSend send = (FileSend) ThreadMgr.getThread("FileSend" + progress);
            send.shutdown();
            ThreadMgr.remove("FileSend" + progress);
            isFinished = true;
            return;
        }
        if (x.equals(file.getAbsolutePath())) {
            openFile.setText("打开文件");
            isFinished = true;
            ThreadMgr.remove("FileRecv" + progress);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openFile) {
            if (openFile.getText().equals("取消")) {
                int op = JOptionPane.showConfirmDialog(view, "是否要取消传输", "取消传输", JOptionPane.YES_NO_OPTION);
                if (op == JOptionPane.YES_OPTION) {
                    if (type == ProgressBar.ACCEPT_FILE) {
                        FileRecv f = (FileRecv) ThreadMgr.getThread("FileRecv" + progress);
                        f.shutdown();
                        openFile.setEnabled(false);
                        ThreadMgr.remove("FileRecv" + progress);
                    } else {
                        FileSend s = (FileSend) ThreadMgr.getThread("FileSend" + progress);
                        s.shutdown();
                        view.cancelProgress(progress);
                        ThreadMgr.remove("FileSend" + progress);
                    }
                }

            } else {
                try {
                    if (file.exists())
                        Runtime.getRuntime().exec("explore.exe " + progressPath);
                    else
                        view.appendText("文件不存在！");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (e.getSource() == openDir) {
            if (file.exists()) {
                String path = progressPath.substring(0, progressPath.lastIndexOf('\\'));
                try {
                    Runtime.getRuntime().exec("explore.exe " + path);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                openDir.setEnabled(false);
                view.appendText("文件不存在！");
            }
        }
        if (e.getSource() == close) {
            if (isFinished)
                view.cancelProgress(progress);
            else {
                int op = JOptionPane.showConfirmDialog(view, "是否要放到到后台传输", "关闭传输窗口", JOptionPane.YES_NO_OPTION);
                if (op == JOptionPane.YES_OPTION)
                    view.cancelProgress(progress);
            }
        }
    }

}
