package com.johncai;

import java.sql.*;

public class DBTest {

	public static void main(String[] args) {
		String user = "wsjohncai";
		String passwd = "1092czg";
		String url = "jdbc:mysql://localhost:3306/sys?useSSL=false";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(url, user, passwd);
			if(con!=null) {
				st = con.createStatement();
				String sql = "show columns in sys_config;";
				rs = st.executeQuery(sql);
				while(rs.next())
				System.out.println(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
