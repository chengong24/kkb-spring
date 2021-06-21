package com.kkb.spring.test;

import com.kkb.spring.dao.UserDaoImpl;
import com.kkb.spring.ioc.BeanDefinition;
import com.kkb.spring.ioc.PropertyValue;
import com.kkb.spring.ioc.RuntimeBeanReference;
import com.kkb.spring.ioc.TypedStringValue;
import com.kkb.spring.po.User;
import com.kkb.spring.service.UserService;
import com.kkb.spring.service.UserServiceImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  1.使用面向过程的思维去解决使用Bean的人和创建Bean的人进行分离
 *  2.创建Bean的人，要如何满足创建任意Bean的需求
 *
 */
public class TestSpringV1502 {


	// 存储BeanDefinition的集合
	// K:beanName
	// V:BeanDefinition
	// 该集合的数据是在XML或者注解解析的时候，去添加数据
	private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();



	// 存储单例Bean的集合
	// K:beanName
	// V:单例Bean
	// 该集合的数据是在getBean的时候去添加数据
	private Map<String, Object> singletonObjects = new HashMap<>();

	/**
	 * 该方法由该方法由程序员A去编写
	 */
	@Test
	public void test() {
		// UserService userService = getUserServiceBean();

		UserService userService = (UserService) getBean("userService");
		// 查询参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("username", "王五");

		// 用户查询
		List<User> users = userService.queryUsers(param);
		System.out.println(users);
	}

	@Before
	public void before(){
		//完成XML解析，其实就是完成BeanDefinition的注册
		// XML解析，解析的结果，放入beanDefinitions中
		String location = "beans.xml";
		// 获取流对象
		InputStream inputStream = getInputStream(location);
		// 创建文档对象
		Document document = createDocument(inputStream);

		// 按照spring定义的标签语义去解析Document文档
		parseBeanDefinitions(document.getRootElement());
	}

	public void parseBeanDefinitions(Element rootElement) {
		// 获取<bean>和自定义标签（比如mvc:interceptors）
		List<Element> elements = rootElement.elements();
		for (Element element : elements) {
			// 获取标签名称
			String name = element.getName();
			if (name.equals("bean")) {
				// 解析默认标签，其实也就是bean标签
				parseDefaultElement(element);
			} else {
				// 解析自定义标签，比如aop:aspect标签
				parseCustomElement(element);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void parseDefaultElement(Element beanElement) {
		try {
			if (beanElement == null)
				return;
			// 获取id属性
			String id = beanElement.attributeValue("id");

			// 获取name属性
			String name = beanElement.attributeValue("name");

			// 获取class属性
			String clazzName = beanElement.attributeValue("class");
			if (clazzName == null || "".equals(clazzName)) {
				return;
			}

			// 获取init-method属性
			String initMethod = beanElement.attributeValue("init-method");
			// 获取scope属性
			String scope = beanElement.attributeValue("scope");
			scope = scope != null && !scope.equals("") ? scope : "singleton";

			// 获取beanName
			String beanName = id == null ? name : id;
			Class<?> clazzType = Class.forName(clazzName);
			beanName = beanName == null ? clazzType.getSimpleName() : beanName;
			// 创建BeanDefinition对象
			// 此次可以使用构建者模式进行优化
			BeanDefinition beanDefinition = new BeanDefinition(clazzName, beanName);
			beanDefinition.setInitMethod(initMethod);
			beanDefinition.setScope(scope);
			// 获取property子标签集合
			List<Element> propertyElements = beanElement.elements();
			for (Element propertyElement : propertyElements) {
				parsePropertyElement(beanDefinition, propertyElement);
			}

			// 注册BeanDefinition信息
			this.beanDefinitions.put(beanName, beanDefinition);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void parsePropertyElement(BeanDefinition beanDefination, Element propertyElement) {
		if (propertyElement == null)
			return;

		// 获取name属性
		String name = propertyElement.attributeValue("name");
		// 获取value属性
		String value = propertyElement.attributeValue("value");
		// 获取ref属性
		String ref = propertyElement.attributeValue("ref");

		// 如果value和ref都有值，则返回
		if (value != null && !value.equals("") && ref != null && !ref.equals("")) {
			return;
		}

		/**
		 * PropertyValue就封装着一个property标签的信息
		 */
		PropertyValue pv = null;

		if (value != null && !value.equals("")) {
			// 因为spring配置文件中的value是String类型，而对象中的属性值是各种各样的，所以需要存储类型
			TypedStringValue typeStringValue = new TypedStringValue(value);

			Class<?> targetType = getTypeByFieldName(beanDefination.getClazzName(), name);
			typeStringValue.setTargetType(targetType);

			pv = new PropertyValue(name, typeStringValue);
			beanDefination.addPropertyValue(pv);
		} else if (ref != null && !ref.equals("")) {

			RuntimeBeanReference reference = new RuntimeBeanReference(ref);
			pv = new PropertyValue(name, reference);
			beanDefination.addPropertyValue(pv);
		} else {
			return;
		}
	}

	private Class<?> getTypeByFieldName(String beanClassName, String name) {
		try {
			Class<?> clazz = Class.forName(beanClassName);
			Field field = clazz.getDeclaredField(name);
			return field.getType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void parseCustomElement(Element element) {
		// TODO Auto-generated method stub

	}

	private InputStream getInputStream(String location) {
		return this.getClass().getClassLoader().getResourceAsStream(location);
	}
	private Document createDocument(InputStream inputStream) {
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(inputStream);
			return document;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 以下的代码可以实现一个方法产生任意Bean实例的作用，但是该方法需要写的很庞大
	 * 而且会违反开闭原则（七大设计原则的最基础要求）
	 * @param beanName
	 * @return
	 */
	/*private Object getBean(String beanName) {
		if ("userService".equals(beanName)){
			UserServiceImpl userService = new UserServiceImpl();
			UserDaoImpl userDao	= new UserDaoImpl();
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://111.231.106.221:3306/kkb");
			dataSource.setUsername("kkb");
			dataSource.setPassword("kkb111111");
			userDao.setDataSource(dataSource);
			userService.setUserDao(userDao);

			return userService;
		}else if ("orderService".equals(beanName)){
			// TODO
		}else{
			// TODO
		}
		return null;
	}*/


	private Object getBean(String beanName) {
		// 注意事项
		// 1.是否每次调用getBean都需要创建一个新的Bean？其实可以使用单例方式去管理Bean。
		// 2.XML配置文件需要被解析几次，是不是每次调用getBean时才进行XML解析呢？答案是只需要解析一次，且在getBean之前就完成解析

		// 改造思路：
		// 1.先去管理Bean的集合缓存中查找对应beanName的bean实例
		Object bean = this.singletonObjects.get(beanName);
		// 2.如果找到，则直接返回该bean
		if (bean != null) return bean;
		// 3.如果没有找到，则需要创建对应的bean实例，此时要判断该bean想以单例方式创建还是原型方式创建
		// 3.1.可以使用XML配置文件的方式来配置beanName和Bean实例对应的关系，同时可以配置Bean被创建需要依赖注入的参数
		// 3.2.一次性解析XML配置文件，形成对应的配置对象（BeanDefinition集合）

		BeanDefinition bd = this.beanDefinitions.get(beanName);
		if (bd == null) return null;

		// 单例
		if(bd.isSingleton()){
			// 3.3.根据beanName找到对应的配置对象（BeanDefinition），来创建Bean实例
			bean = doCreateBean(bd);
			// 4.将创建完成的bean实例放入管理Bean的集合缓存
			this.singletonObjects.put(beanName,bean);
		}else if(bd.isPrototype()){
			// 原型（多例）
			bean = doCreateBean(bd);
		}

		return bean;
	}

	private Object doCreateBean(BeanDefinition bd) {
		// 1.bean的实例化（new对象）
		Object bean = null;
		try {
			bean = createBeanInstance(bd);
			// 2.bean的依赖注入（setter方法注入）
			populateBean(bean,bd);
			// 3.bean的初始化(调用初始化方法)
			initializeBean(bean,bd);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bean;
	}

	private void initializeBean(Object bean, BeanDefinition bd) {
		// TODO 对Aware接口，进行处理
		invokeInitMethod(bean,bd);
	}

	private void invokeInitMethod(Object bean, BeanDefinition bd) {
		// TODO 对于实现了InitializingBean接口的类进行处理

		// 对于配置了init-method标签属性的方法进行调用
		String initMethod = bd.getInitMethod();
		if (initMethod == null ||"".equals(initMethod)) return;
		Class<?> clazzType = bd.getClazzType();
		Method method = null;
		try {
			method = clazzType.getDeclaredMethod(initMethod);
			method.invoke(bean,null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void populateBean(Object bean, BeanDefinition bd) throws Exception {
		Class<?> clazzType = bd.getClazzType();
		List<PropertyValue> propertyValues = bd.getPropertyValues();
		for (PropertyValue pv : propertyValues) {
			String name = pv.getName();
			// 此时的value是不可以直接依赖注入的
			Object value = pv.getValue();

			// 已经处理过之后的了
			Object valueToUse = resolveValue(value,bd);

			Field field = clazzType.getDeclaredField(name);
			field.setAccessible(true);
			field.set(bean,valueToUse);
		}
	}

	private Object resolveValue(Object value, BeanDefinition bd) {
		if (value instanceof RuntimeBeanReference){
			RuntimeBeanReference beanReference = (RuntimeBeanReference) value;
			String ref = beanReference.getRef();
			return getBean(ref);
		}else if (value instanceof TypedStringValue){
			TypedStringValue typedStringValue = (TypedStringValue) value;
			String stringValue = typedStringValue.getValue();
			Class<?> targetType = typedStringValue.getTargetType();
			if (targetType == Integer.class){
				return Integer.parseInt(stringValue);
			}else if (targetType == String.class){
				return stringValue;
			}
		}
		return null;
	}

	private Object createBeanInstance(BeanDefinition bd) throws Exception{
		// TODO 通过静态工厂
		// TODO 通过工厂方法

		// 通过构造方法
		Class<?> clazzType = bd.getClazzType();


		Constructor<?> constructor = clazzType.getDeclaredConstructor();
		Object bean = constructor.newInstance();

		return bean;
	}


	/**
	 * 该方法由程序员B去编写
	 * @return
	 */
	private UserService getUserServiceBean() {
		UserServiceImpl userService = new UserServiceImpl();
		UserDaoImpl userDao	= new UserDaoImpl();
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://111.231.106.221:3306/kkb");
		dataSource.setUsername("kkb");
		dataSource.setPassword("kkb111111");
		userDao.setDataSource(dataSource);
		userService.setUserDao(userDao);

		return userService;
	}

}
