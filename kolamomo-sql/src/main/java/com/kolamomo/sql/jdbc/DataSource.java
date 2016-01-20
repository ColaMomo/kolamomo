package com.kolamomo.sql.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource  {
	private String url;
	private String user;
	private String password;
	
	public DataSource() {}
	
	public DataSource(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}
		
	public Connection getConnection() {
		Connection conn = null;
	    try{   
	    	Class.forName("com.mysql.jdbc.Driver") ;   
	    }catch(ClassNotFoundException e){   
	    	System.out.println("找不到驱动程序类 ，加载驱动失败！");   
	    	e.printStackTrace() ;   
	    }   
	     try{   
	    	 conn =  DriverManager.getConnection(url , user , password ) ;   
	     }catch(SQLException se){   
	    	 System.out.println("数据库连接失败！");   
	    	 se.printStackTrace() ;   
	     }   
	     return conn;
	}
	
	public static void main(String args[]) {
		DataSource dataSource = new DataSource("jdbc:mysql://127.0.0.1:3306/kolamomo", "root", "123456");
		dataSource.getConnection();
	}
}
