package com.kkb.spring.test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kkb.spring.framework.resource.support.ClasspathResource;
import org.junit.Test;

import com.kkb.spring.framework.factory.support.DefaultListableBeanFactory;
import com.kkb.spring.framework.reader.XmlBeanDefinitionReader;
import com.kkb.spring.framework.resource.Resource;
import com.kkb.spring.po.User;
import com.kkb.spring.service.UserService;

/**
 * 以面向对象的思维去实现IoC的相应功能 IoC思想：调用者，只需要负责bean的使用，不负责bean的创建
 * 
 * @author 灭霸詹
 *
 */
public class TestSpringV1403 {

	@Test
	public void test() {
		
		// XML解析，解析的结果，放入beanDefinitions中
		String location = "beans.xml";
		// 获取流对象
		Resource resource = new ClasspathResource(location);
		InputStream inputStream = resource.getResource();

		//定义Spring容器对象，它本身也是一个BeanDefinitionRegistry
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

		// 按照spring定义的标签语义去解析Document文档
		beanDefinitionReader.registerBeanDefinitions(inputStream);

		UserService userService = (UserService) beanFactory.getBean("userService");

		// 查询参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("username", "王五");

		// 用户查询
		List<User> list = userService.queryUsers(param);
		System.out.println(list);
	}
}
