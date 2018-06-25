package com.dark.api.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BaseDao {
	
	public static void main(String[] args) {
		Connection conn=null;
		if (conn == null) {
			try {
				String url="jdbc:mysql://rm-bp118g807a1i5wk86.mysql.rds.aliyuncs.com:3306/kstore_v2?user=cloudlifecd_ht16&password=shyhUO-7d_G0hd45&useUnicode=true&amp;characterEncoding=utf-8";
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url);
				System.out.println("chenggong");
			} catch (SQLException e) {
				System.err.println("shi");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.err.println("shi");
				e.printStackTrace();
			}
		}
	}
	
}
