package com.kkb.spring.test;

import com.kkb.spring.dao.UserDaoImpl;
import com.kkb.spring.service.UserServiceImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 主要通过V1版本来暴露Bean的创建问题（我们只是想使用Bean，而不是想去创建Bean）
 * * 原因：
 * 	创建Bean需要去关注该Bean的创建细节。
 */
public class TestSpringV1501 {

	@Test
	public void test() {
		// 使用UserService的程序员自己负责构造对应的实例（？？？）
		UserServiceImpl userService = new UserServiceImpl();
		UserDaoImpl userDao	= new UserDaoImpl();
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://111.231.106.221:3306/kkb");
		dataSource.setUsername("kkb");
		dataSource.setPassword("kkb111111");
		userDao.setDataSource(dataSource);
		userService.setUserDao(userDao);

		// 查询参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("username", "王五");

		// 用户查询
		userService.queryUsers(param);
	}
}
