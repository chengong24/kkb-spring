package com.kkb.spring.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

import com.kkb.spring.dao.UserDaoImpl;
import com.kkb.spring.po.User;
import com.kkb.spring.service.UserServiceImpl;

public class TestSpringV1 {

	@Test
	public void test() throws Exception {
		// 创建UserServiceImpl对象
		UserServiceImpl userService = new UserServiceImpl();
		// 创建UserDaoImpl对象
		UserDaoImpl userDao = new UserDaoImpl();
		// 创建BasicDataSource对象
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://111.231.106.221:3306/kkb");
		dataSource.setUsername("kkb");
		dataSource.setPassword("kkb111111");

		// 对UserDaoImpl对象依赖注入BasicDataSource实例
		userDao.setDataSource(dataSource);
		// 对UserServiceImpl对象依赖注入UserDaoImpl实例
		userService.setUserDao(userDao);

		// 入参对象
		Map<String, Object> param = new HashMap<>();
		param.put("username", "王五");
		// 根据用户名称查询用户信息
		List<User> users = userService.queryUsers(param);
		System.out.println(users);
	}
}
