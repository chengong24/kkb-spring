package com.kkb.spring.framework.registry.support;

import com.kkb.spring.framework.registry.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private Map<String,Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String name) {
        return this.singletonObjects.get(name);
    }

    @Override
    public void addSingleton(String name, Object bean) {
        // TODO 双重检查锁机制
        this.singletonObjects.put(name,bean);
    }
}
