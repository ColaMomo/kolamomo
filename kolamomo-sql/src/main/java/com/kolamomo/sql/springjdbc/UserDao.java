package com.kolamomo.sql.springjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.core.RowMapper;

import com.kolamomo.common.util.ApiLogger;
import com.kolamomo.sql.module.User;

/**
 * userDao: 用户表dao层，使用spring jdbc
 * @author jay
 *
 */
public class UserDao extends JdbcDaoSupport {
	private String GET_USER_BY_UID_SQL = "select uid, name, phone, email from user where uid = ?";

	public User getUserByUid(int uid) {
		User user = new User();
		try {
			this.getJdbcTemplate().query(GET_USER_BY_UID_SQL, new Object[] { uid }, new UserRowMapper(user));
		} catch (Exception ex) {
			System.out.println("error when select from db: e: " + ex.getMessage());
			user = null;
		}
		return user;
	}

	private class UserRowMapper implements RowMapper<User>
	{
		private User user;

		public UserRowMapper(User user) {
			this.user = user;
		}

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				user.setUid(rs.getInt("uid"));
				user.setName(rs.getString("name"));
				user.setPhone(rs.getString("phone"));
				user.setEmail(rs.getString("email"));
			} catch (SQLException e) {
				ApiLogger.error("error when map row.");
			} catch (Exception e) {
				ApiLogger.error("error when map row.");
			}
			return null;
		}
	}
}