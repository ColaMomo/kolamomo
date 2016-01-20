package com.kolamomo.sql.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kolamomo.sql.module.User;

/**
 * userDao: 用户表dao层，使用spring jdbc
 * 
 * @author jay
 *
 */
public class UserDao {
	private String GET_USER_BY_UID_SQL = "select uid, name, phone, email from user where uid = ?";
	DataSource dataSource = new DataSource("jdbc:mysql://127.0.0.1:3306/kolamomo", "root", "123456");

	public User getUserByUid(int uid) {
		User user = new User();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(GET_USER_BY_UID_SQL);
			ps.setInt(1, uid);
			rs = ps.executeQuery();
			if (rs.next()) {
				user.setUid(rs.getInt("uid"));
				user.setName(rs.getString("name"));
				user.setPhone(rs.getString("phone"));
				user.setEmail(rs.getString("email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			user = null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	
	public static void main(String args[]) {
		UserDao userDao = new UserDao();
		User user = userDao.getUserByUid(1);
		if(user != null) {
			System.out.println(user.getUid() + "-" + user.getName() + "-" + user.getPhone() + "-" + user.getEmail());
		}
	}

}