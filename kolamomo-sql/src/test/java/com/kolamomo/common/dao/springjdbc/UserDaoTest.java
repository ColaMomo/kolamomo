package com.kolamomo.common.dao.springjdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kolamomo.sql.module.User;
import com.kolamomo.sql.springjdbc.UserDao;

/**
 * @author yuanming@staff.sina.com.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/datasource.xml"})
public class UserDaoTest {

	private UserDao userDao;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Before
	public void before(){
	}
	
	@Test
	public void testAccessTokenDao(){
		User user = userDao.getUserByUid(1);
		System.out.println(user.getUid() + "-" + user.getName() + "-" + user.getPhone() + "-" + user.getEmail() );
	}
	
	@After
	public void after(){
	}
	
}
