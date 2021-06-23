package com.kkb.spring.framework.factory;

import java.util.List;

/**
 * 可以对Bean工厂进行列表化操作
 */
public interface ListableBeanFactory extends BeanFactory{

    //TODO 在SpringMVC的时候去添加接口方法
    /**
     * 可以根据参数获取它和它子类型的实例，比如传递Object.class，则说明获取的是所有的实例对象
     * @param clazz
     * @return
     */
    <T> List<T> getBeansByType(Class<?> clazz);
}
