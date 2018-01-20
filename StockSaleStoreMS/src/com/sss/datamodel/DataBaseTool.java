package com.sss.datamodel;

import java.sql.*;

public class DataBaseTool {
    private Connection conn;
    private String url = "jdbc:mysql://localhost:3306/sss_ms?useSSL=false";
    private String user = "wsjohncai";
    private String passwd = "1092czg";
    PreparedStatement prest;

    private Connection getConn() {
        if (conn == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, passwd);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public DataBaseTool() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, passwd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String queryForOneResult(String sql, String... params) {
        ResultSet rs = query(sql, params);
        try {
            if (rs != null && rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet query(String sql, String... params) {
        try {
            prest = getConn().prepareStatement(sql);
            System.out.println(sql);
            if (params != null)
                for (int i = 0; i < params.length; i++) {
                    prest.setString(i + 1, params[i]);
                    System.out.print("params:"+params[i]);
                }
            System.out.println("");
            return prest.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行insert,update,delete操作时调用
     *
     * @return 成功返回true，否则返回false
     */
    public boolean update(String sql, String... params) {
        boolean returrnValue = false;
        try {
            prest = getConn().prepareStatement(sql);
            System.out.println(sql);
            if (params != null)
                for (int i = 0; i < params.length; i++) {
                    prest.setString(i+1, params[i]);
                    System.out.print("params:"+params[i]);
                }
            System.out.println("");
            returrnValue = prest.executeUpdate() > 0;
            prest.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returrnValue;
    }

    public void closeAll() {
        try {
            if (prest != null) {
                prest.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prest != null) {
                    prest.close();
                }
                if (conn != null) {
                    conn.close();
                }
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
