package com.kkb.spring.framework.aware;

import com.kkb.spring.framework.factory.BeanFactory;

/**
 * 可以给实现了该接口的类，依赖注入一个BeanFactory的实例
 * 
 * @author 灭霸詹
 *
 */
public interface BeanFactoryAware extends Aware {

	void setBeanFactory(BeanFactory beanFactory);
}
