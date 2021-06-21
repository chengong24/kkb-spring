package com.kkb.spring.framework.factory.support;

import com.kkb.spring.framework.config.BeanDefinition;
import com.kkb.spring.framework.registry.BeanDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * spring真正管理Bean和BeanDefinition的工厂
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {
    private Map<String,BeanDefinition> beanDefinitions = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitions.get(name);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitions.put(name,beanDefinition);
    }
}
