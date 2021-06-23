package com.kkb.spring.framework.registry;

import com.kkb.spring.framework.config.BeanDefinition;

import java.util.List;

/**
 * 针对存储单例BeanDefinition集合提供对外的操作功能
 */
public interface BeanDefinitionRegistry {

    /**
     * 获取单例Bean
     * @param name
     * @return
     */
    BeanDefinition getBeanDefinition(String name);

    /**
     * 存储单例Bean
     * @param name
     * @param beanDefinition
     */
    void registerBeanDefinition(String name,BeanDefinition beanDefinition);


    List<BeanDefinition> getBeanDefinitions();
}
