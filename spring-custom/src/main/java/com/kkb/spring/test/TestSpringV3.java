package com.kkb.spring.test;


public class TestSpringV3 {

	/*@Test
	public void test() throws Exception {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		// 读取XML的BeanDefinition阅读器
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

		Resource resource = new ClasspathResource("beans.xml");
		InputStream inputStream = resource.getResource();

		beanDefinitionReader.loadBeanDefinitions(inputStream);

		// 根据用户名称查询用户信息
		UserService userService = (UserService) beanFactory.getBean("userService");

		// 入参对象
		Map<String, Object> param = new HashMap<>();
		param.put("username", "王五");
		// 根据用户名称查询用户信息
		List<User> users = userService.queryUsers(param);
		System.out.println(users);
	}*/
}
