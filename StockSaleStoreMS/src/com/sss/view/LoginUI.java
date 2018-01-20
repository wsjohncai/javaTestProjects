package com.sss.view;

import com.sss.datamodel.DataBaseTool;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class LoginUI implements MouseListener, MouseMotionListener {

    private int first_x, first_y;
    private ImageIcon min, min_sel,close,close_sel,max,login_png;
    private JFrame loginframe;
    private JPanel login_title;
    private JLabel lb_min;
    private JLabel lb_close;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginUI login = new LoginUI();
        login.init();
    }

    private ImageIcon getImage(String path) {
        ImageIcon icon = null;
        try {
            ImageInputStream io = ImageIO.createImageInputStream(
                    getClass().getResourceAsStream(path));
            Image image = ImageIO.read(io);
            icon = new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }

    private void init() {
        min = getImage("/opwbtn/min.png");
        min_sel = getImage("/opwbtn/min_sel.png");
        close = getImage("/opwbtn/close.png");
        close_sel = getImage("/opwbtn/close_sel.png");
        max = getImage("/opwbtn/max_nor.png");
        login_png = getImage("/opwbtn/login.png");
        loginSurface();
    }

    private void loginSurface() {
        loginframe = new JFrame();
        loginframe.setSize(450, 278);
        loginframe.setUndecorated(true);
        loginframe.setLayout(null);

        login_title = new JPanel();
        login_title.setBackground(Color.CYAN);
        login_title.setLayout(null);
        login_title.addMouseListener(this);
        login_title.addMouseMotionListener(this);
        JLabel title = new JLabel("进销存管理系统");
        title.setFont(new Font("宋体", Font.BOLD, 18));
        title.setForeground(Color.BLUE);
        login_title.add(title);
        title.setBounds(10, 5, 200, 23);
        lb_min = new JLabel(min);
        JLabel lb_max = new JLabel(max);
        lb_close = new JLabel(close);
        login_title.add(lb_min);
        login_title.add(lb_max);
        login_title.add(lb_close);
        lb_min.setBounds(loginframe.getWidth() - 90, 1, 30, 23);
        lb_max.setBounds(loginframe.getWidth() - 60, 1, 30, 23);
        lb_close.setBounds(loginframe.getWidth() - 30, 1, 30, 23);
        lb_min.addMouseListener(this);
        lb_max.addMouseListener(this);
        lb_close.addMouseListener(this);
        loginframe.add(login_title);
        login_title.setBounds(0, 0, 450, 30);

        JPanel login_body = new JPanel();
        login_body.setLayout(null);
        login_body.setBackground(new Color(48, 201, 222));
        JLabel name = new JLabel("用户名:");
        JLabel group = new JLabel("密码:");
        name.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        group.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        name.setOpaque(false);
        group.setOpaque(false);
        login_body.add(name);
        name.setBounds(75, 60, 100, 30);
        login_body.add(group);
        group.setBounds(75, 120, 100, 30);
        JTextField ntext = new JTextField();
        JPasswordField passwdf = new JPasswordField();
        login_body.add(ntext);
        ntext.setBounds(175, 60, 200, 30);
        login_body.add(passwdf);
        passwdf.setBounds(175, 120, 200, 30);
        JButton login = new JButton();
        login.setIcon(login_png);
        login.addActionListener(e -> {
            String id,passwd;
            id = ntext.getText();
            passwd = new String(passwdf.getPassword());
            //检查账户密码是否正确
            DataBaseTool db = new DataBaseTool();
            String sql = "select passwd from user where userId = ?";
            String chv = db.queryForOneResult(sql, id);
            db.closeAll();
            if(chv != null && chv.equals(passwd)) {
                new BaseFrame(id).init();
                loginframe.dispose();
            } else {
                JOptionPane.showMessageDialog(loginframe, "账号或密码错误");
            }
        });
        login_body.add(login);
        login.setBounds(175, 175, 100, 55);
        loginframe.add(login_body);
        login_body.setBounds(0, 31, 450, 247);

        this.loginframe.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login.doClick();
                }
            }
        });
        loginframe.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 225,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 139);
        loginframe.setVisible(true);
        loginframe.validate();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == lb_min) {
            loginframe.setExtendedState(Frame.ICONIFIED);
        }
        if (e.getSource() == lb_close) {
            loginframe.dispose();
            System.exit(0);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == login_title) {
            first_x = e.getX();
            first_y = e.getY();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == lb_min) {
            if (min_sel == null)
                min_sel = getImage("/opwbtn/min_sel.png");
            lb_min.setIcon(min_sel);
        }
        if (e.getSource() == lb_close) {
            if (close_sel == null)
                close_sel =getImage("/close_sel.png");
            lb_close.setIcon(close_sel);
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == lb_min) {
            if (min == null)
                min = getImage("/min.png");
            lb_min.setIcon(min);
        }

        if (e.getSource() == lb_close) {
            if (close == null)
                close = getImage("/close.png");
            lb_close.setIcon(close);
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX() - first_x;
        int y = e.getY() - first_y;
        loginframe.setBounds(loginframe.getX() + x, loginframe.getY() + y, loginframe.getWidth(),
                loginframe.getHeight());

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

}
