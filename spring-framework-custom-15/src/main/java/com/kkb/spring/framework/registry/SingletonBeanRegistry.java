package com.kkb.spring.framework.registry;

/**
 * 针对存储单例Bean集合提供对外的操作功能
 */
public interface SingletonBeanRegistry {

    /**
     * 获取单例Bean
     * @param name
     * @return
     */
    Object getSingleton(String name);

    /**
     * 存储单例Bean
     * @param name
     * @param bean
     */
    void addSingleton(String name,Object bean);
}
