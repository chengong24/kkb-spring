package com.kkb.spring.framework.factory.support;

import com.kkb.spring.framework.config.BeanDefinition;
import com.kkb.spring.framework.factory.ListableBeanFactory;
import com.kkb.spring.framework.registry.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * spring真正管理Bean和BeanDefinition的工厂
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ListableBeanFactory {
    private Map<String,BeanDefinition> beanDefinitions = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitions.get(name);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitions.put(name,beanDefinition);
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        List<BeanDefinition> beanDefinitionList = new ArrayList<>();
        for (BeanDefinition bd:beanDefinitions.values()){
            beanDefinitionList.add(bd);
        }
        return beanDefinitionList;
    }

    @Override
    public <T> List<T> getBeansByType(Class<?> clazz) {
        // TODO
        List<T> results = new ArrayList<T>();
        for (BeanDefinition bd : beanDefinitions.values()) {
            String clazzName = bd.getClazzName();
            Class<?> type = resolveClassName(clazzName);
            if (clazz.isAssignableFrom(type)) {
                Object bean = getBean(bd.getBeanName());
                results.add((T) bean);
            }
        }
        return results;
    }

    private Class<?> resolveClassName(String clazzName) {
        try {
            Class<?> type = Class.forName(clazzName);
            return type;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
