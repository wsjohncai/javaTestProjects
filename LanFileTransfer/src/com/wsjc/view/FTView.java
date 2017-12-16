package com.wsjc.view;

import com.wsjc.connection.FileSend;
import com.wsjc.connection.ListenBC;
import com.wsjc.connection.SendDataPkg;
import com.wsjc.connection.UserSeeker;
import com.wsjc.data.Data;
import com.wsjc.data.User;
import com.wsjc.tools.ThreadMgr;
import com.wsjc.tools.UserMgr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

/**
 * This application is used to transfer files in a LAN network
 *
 * @author WSJohnCai
 * @version 1.1
 */

public class FTView extends JFrame implements ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // Variables are defined here
    private JPanel bg_pl, recv_pl, fc_pl, tfinfo_pl; // 定义面板
    private JTextField file_tf;
    private JComboBox<String> recv_list, addr_list;
    private JButton fc_btn, send_btn;
    private JTextArea info_ta;
    private JScrollPane info_sc;
    private Box backPane;
    private int SW = getToolkit().getScreenSize().width, SH = getToolkit().getScreenSize().height;
    private HashMap<String, ProgressBar> bars = new HashMap<>();
    private boolean isRunning = true;
    private InetAddress selecetedAddress = null;
    private NetworkInterface selectefNetworkInterface = null;

    private void initUI() {
        if (backPane == null) {
            this.setTitle("FileTransfer Helper");
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    FTView f = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
                    if (ThreadMgr.threadSize() > 3) {
                        int op = JOptionPane.showConfirmDialog(f, "还有任务正在进行，是否要退出？", "关闭", JOptionPane.YES_NO_OPTION);
                        if (op == JOptionPane.YES_OPTION) {
                            shutdown();
                        }
                    } else {
                        shutdown();
                    }
                }

            });
            bg_pl = new JPanel();
            backPane = Box.createVerticalBox();
            backPane.add(Box.createVerticalStrut(3));

            // 文件选择面板
            fc_pl = new JPanel();
            file_tf = new JTextField();
            fc_btn = new JButton("浏览");
            fc_btn.addActionListener(this);
            file_tf.setText("请选择一个文件发送");
            file_tf.setEditable(false);
            file_tf.setPreferredSize(new Dimension(300, 28));
            file_tf.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            fc_pl.add(file_tf, SwingConstants.CENTER, 0);
            fc_pl.add(fc_btn, SwingConstants.CENTER, 1);
            backPane.add(fc_pl);
            backPane.add(Box.createVerticalStrut(3));

            // 可用发送方下拉栏
            recv_pl = new JPanel();
            recv_pl.setPreferredSize(new Dimension(360, 60));
            recv_list = new JComboBox<>();
            send_btn = new JButton("发送");
            send_btn.setEnabled(false);
            send_btn.addActionListener(this);
            recv_list.setPreferredSize(new Dimension(300, 25));
            addr_list = new JComboBox<>();
            addr_list.setPreferredSize(new Dimension(300, 25));
            addr_list.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    String s = (String) addr_list.getSelectedItem();
                    try {
                        selecetedAddress = InetAddress.getByName(s.split(":")[2]);
                        selectefNetworkInterface = NetworkInterface.getByName(s.split(":")[0]);
                    } catch (UnknownHostException | SocketException e1) {
                        e1.printStackTrace();
                    }
                    addr_list.setToolTipText((String) addr_list.getSelectedItem());
                    ListenBC listen = (ListenBC) ThreadMgr.getThread(ThreadMgr.BGTHREAD);
                    if (listen != null)
                        listen.init();
                }
            });
            getInterfaces();

            addr_list.setFont(new Font("Consolas", Font.PLAIN, 10));
            recv_list.setFont(new Font("Consolas", Font.BOLD, 14));
            recv_pl.add(recv_list);
            recv_pl.add(send_btn);
            recv_pl.add(addr_list);
            backPane.add(recv_pl);
            backPane.add(Box.createVerticalStrut(3));

            // 文件传输信息显示区域
            info_ta = new JTextArea();
            info_sc = new JScrollPane(info_ta);
            tfinfo_pl = new JPanel();
            info_ta.setEditable(false);
            info_ta.setLineWrap(true);
            info_sc.setPreferredSize(new Dimension(350, 150));
            info_sc.setAutoscrolls(true);
            tfinfo_pl.add(info_sc);
            backPane.add(tfinfo_pl);
            backPane.add(Box.createVerticalStrut(3));
        } else {
            backPane.removeAll();
            bg_pl.remove(backPane);
            this.remove(bg_pl);
            backPane = Box.createVerticalBox();
            backPane.add(Box.createVerticalStrut(3));
            backPane.add(fc_pl);
            backPane.add(Box.createVerticalStrut(3));
            backPane.add(recv_pl);
            backPane.add(Box.createVerticalStrut(3));
            backPane.add(tfinfo_pl);
            backPane.add(Box.createVerticalStrut(3));
            refreshUser();
            Set<String> keys = bars.keySet();
            for (String key : keys) {
                ProgressBar bar = bars.get(key);
                backPane.add(Box.createVerticalStrut(3));
                backPane.add(bar);
            }
        }

        bg_pl.add(backPane);
        bg_pl.setSize(backPane.getSize());
        this.add(bg_pl);
        this.setResizable(false);
        this.setVisible(true);
        this.validate();
        this.pack();
        this.setLocation((SW - this.getWidth()) / 2, (SH - this.getHeight()) / 2);
    }

    public InetAddress getSelecetedAddress() {
        return selecetedAddress;
    }

    public NetworkInterface getSelectefNetworkInterface() {
        return selectefNetworkInterface;
    }

    /**
     * 关闭并退出
     */
    private void shutdown() {
        isRunning = false;
        dispose();
        LogoutMsg();
        ThreadMgr.removeAll();
        System.exit(0);
    }

    /**
     * 发送离线消息
     */
    private void LogoutMsg() {
        Data d = null;
        try {
            d = new Data(Data.LOG_OUT, InetAddress.getLocalHost().getHostName());
            SendDataPkg sender = (SendDataPkg) ThreadMgr.getThread("SendDataPkg");
            if (sender == null) {
                sender = new SendDataPkg();
                ThreadMgr.add("SendDataPkg", sender);
            }
            sender.sendBrocast(d);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 用于从外部调用，刷新用户下拉单
     */
    public void refreshUser() {
        HashMap<String, User> users = UserMgr.getUsers();
        send_btn.setEnabled(true);
        Set<String> list = users.keySet();
        SwingUtilities.invokeLater(() -> {
            recv_list.removeAllItems();

            for (String item : list) {
                if (item != null) {
                    recv_list.addItem(users.get(item).getAddr() + "-" + users.get(item).getHostName());
                }
            }
        });
        if (users.size() == 0)
            send_btn.setEnabled(false);
    }

    private void getInterfaces() {
        try {
            Enumeration<NetworkInterface> ins = NetworkInterface.getNetworkInterfaces();
            while (ins.hasMoreElements()) {
                NetworkInterface in = ins.nextElement();
                Enumeration<InetAddress> addrs = in.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    addr_list.addItem(in.getName() + ":" + in.getDisplayName() + ":" + addr.getHostAddress());
                }
            }
            addr_list.setSelectedIndex(0);
            String s = addr_list.getItemAt(0);
            selecetedAddress = InetAddress.getByName(s.split(":")[2]);
            selectefNetworkInterface = NetworkInterface.getByName(s.split(":")[0]);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于弹出文件接收确认的界面
     *
     * @param info 含有文件名称以及大小信息
     * @return true 接收文件\nfalse 不接收文件
     */
    public boolean recvFile(String[] info) {
        long length = Long.parseLong(info[1]);
        double size;
        double t;
        String ssize;
        if ((t = length * 10.0 / 1024 / 1024 / 1024) >= 1) {
            int i = (int) t;
            size = i * 1.0 / 10;
            ssize = size + "GB";
        } else if ((t = length * 10.0 / 1024 / 1024) >= 1) {
            int i = (int) t;
            size = i * 1.0 / 10;
            ssize = size + "MB";
        } else if ((t = length * 10.0 / 1024) >= 1) {
            int i = (int) t;
            size = i * 1.0 / 10;
            ssize = size + "KB";
        } else {
            ssize = length + "B";
        }
        String msg = "文件名：" + info[0] + "\n大小：" + ssize;
        int op = JOptionPane.showConfirmDialog(this, msg, "接收文件", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            // 测试
            appendText("同意接收文件");
            return true;
        }
        return false;
    }

    public File saveFile(String filename) {
        JFileChooser choose = new JFileChooser();
        File file = null;
        choose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        choose.setSelectedFile(new File("%username%\\" + filename));
        int op = choose.showSaveDialog(this);
        if (op == JFileChooser.APPROVE_OPTION) { // 选择好了文件夹
            File temp = choose.getSelectedFile();
            if (temp.isDirectory()) // 如果选择的是文件夹
                file = new File(temp.getAbsolutePath() + "\\" + filename);
            else
                file = temp;
            if (file.exists()) { // 若文件存在
                int ot = JOptionPane.showConfirmDialog(this, "文件已存在，是否覆盖？", "警告", JOptionPane.YES_NO_OPTION);
                if (ot == JOptionPane.YES_OPTION)
                    return file;
                else {
                    saveFile(filename);
                }
            } else { // 若选择的文件不存在
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return file;
            }
        } else { // 取消了文件选择框
            return null;
        }
        appendText("选择文件出错！");
        return null;
    }

    /**
     * 输出一行文字
     *
     * @param text
     */
    public void appendText(String text) {
        info_ta.append(text + "\n");
        info_ta.setCaretPosition(info_ta.getText().length());
    }

    /**
     * 更新文件传输进度
     */
    public void updateProgress(File file, String text) {
        ProgressBar bar = bars.get(file.getName());
        if (bar == null) {
            int type;
            if (text.contains("发送")) {
                type = ProgressBar.SEND_FILE;
            } else
                type = ProgressBar.ACCEPT_FILE;
            bar = new ProgressBar(type, file, this.getWidth());
            bars.put(file.getName(), bar);
            backPane.add(Box.createVerticalStrut(3));
            backPane.add(bar);
        }
        bar.setLabelText(text);
        refreshUI();
    }

    // 刷新整个界面
    private void refreshUI() {
        SwingUtilities.invokeLater(() -> {
            FTView view = (FTView) ThreadMgr.getThread(ThreadMgr.FTVIEW);
            view.validate();
            view.pack();
            view.setLocation((SW - view.getWidth()) / 2, (SH - view.getHeight()) / 2);
        });
    }

    // 删除指定的bar条
    public void cancelProgress(String progress) {
        ProgressBar bar = bars.get(progress);
        if (bar != null) {
            bars.remove(progress, bar);
            initUI();
        }
    }

    public boolean getRunning() {
        return isRunning;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fc_btn) {
            JFileChooser choose = new JFileChooser();
            choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
            String path = file_tf.getText();
            if (path != null) {
                File f = new File(path);
                if (f.exists())
                    choose.setCurrentDirectory(f);
            }
            int r = choose.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                file_tf.setText(choose.getSelectedFile().getAbsolutePath());
            }
        }
        if (e.getSource() == send_btn) {
            String[] user = recv_list.getSelectedItem().toString().split("-");
            File file = new File(file_tf.getText());
            if (file.exists()) { // 如果文件存在，则构建数据包并向选定用户发送请求
                if (bars.containsKey(file.getName())) {
                    ProgressBar bar = bars.get(file.getName());
                    bar.requestFocus();
                    return;
                }
                Data data = new Data(Data.SEND_FILE, file.getName() + ";" + file.length());
                try {
                    InetAddress ip = InetAddress.getByName(user[0]);
                    //测试
                    System.out.println(ip.getHostAddress() + ", " + user[0] + ", " + data.getTypeName() + ", " + data.getData());
                    FileSend send = new FileSend(ip, file);
                    SendDataPkg sender = new SendDataPkg();
                    sender.sendPacket(data, ip);
                    appendText("等待向" + UserMgr.getUser(ip.getHostAddress()).getHostName() + "确认");
                    ThreadMgr.add("FileSend" + file.getName(), send);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
            } else {
                appendText("发送失败！文件不存在！");
            }
        }

        this.requestFocus();
    }

    public static void main(String[] args) {
        FTView view = new FTView();
        view.initUI();
        ThreadMgr.add(ThreadMgr.FTVIEW, view);
        ListenBC bgThread = new ListenBC();
        bgThread.init();
        ThreadMgr.add(ThreadMgr.BGTHREAD, bgThread);
        UserSeeker seeker = new UserSeeker();
        seeker.start();
    }

}


