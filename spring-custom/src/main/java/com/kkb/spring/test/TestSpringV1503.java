package com.kkb.spring.test;

import com.kkb.spring.framework.factory.support.DefaultListableBeanFactory;
import com.kkb.spring.framework.reader.XmlBeanDefinitionReader;
import com.kkb.spring.framework.resource.Resource;
import com.kkb.spring.framework.resource.support.ClasspathResource;
import com.kkb.spring.po.User;
import com.kkb.spring.service.UserService;
import org.junit.Before;
import org.junit.Test;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  1.使用面向对象的思维去解决使用Bean的人和创建Bean的人进行分离
 *
 *
 */
public class TestSpringV1503 {

	private DefaultListableBeanFactory beanFactory;

	@Before
	public void before(){

	}

	@Test
	public void  test(){

		//完成XML解析，其实就是完成BeanDefinition的注册
		// XML解析，解析的结果，放入beanDefinitions中
		String location = "beans.xml";
		// 获取流对象
		Resource resource = new ClasspathResource(location);
		InputStream inputStream = resource.getResource();
		// 按照spring定义的标签语义去解析Document文档
		beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
		beanDefinitionReader.registerBeanDefinitions(inputStream);

		UserService userService = (UserService) beanFactory.getBean("userService");
		// 查询参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("username", "王五");

		// 用户查询
		List<User> users = userService.queryUsers(param);
		System.out.println(users);
	}

}
