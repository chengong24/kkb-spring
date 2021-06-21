package com.kkb.spring.framework.factory.support;

import com.kkb.spring.framework.config.BeanDefinition;
import com.kkb.spring.framework.config.PropertyValue;
import com.kkb.spring.framework.config.RuntimeBeanReference;
import com.kkb.spring.framework.config.TypedStringValue;
import com.kkb.spring.framework.factory.BeanFactory;
import com.kkb.spring.framework.registry.support.DefaultSingletonBeanRegistry;
import com.kkb.spring.framework.resolver.ValueResolver;
import com.kkb.spring.framework.utils.ReflectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


/**
 * createBean
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected Object doCreateBean(BeanDefinition bd) {
        // 1.bean的实例化（new对象）
        Object bean = null;
        try {
            bean = createBeanInstance(bd);
            // 2.bean的依赖注入（setter方法注入）
            populateBean(bean, bd);
            // 3.bean的初始化(调用初始化方法)
            initializeBean(bean, bd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bean;
    }

    private void initializeBean(Object bean, BeanDefinition bd) {
        // TODO 对Aware接口，进行处理
        invokeInitMethod(bean, bd);
    }

    private void invokeInitMethod(Object bean, BeanDefinition bd) {
        // TODO 对于实现了InitializingBean接口的类进行处理

        // 对于配置了init-method标签属性的方法进行调用
        String initMethod = bd.getInitMethod();
        if (initMethod == null || "".equals(initMethod)) return;
        Class<?> clazzType = bd.getClazzType();

        ReflectUtils.invokeMethod(bean, initMethod, clazzType);

    }

    private void populateBean(Object bean, BeanDefinition bd) throws Exception {
        Class<?> clazzType = bd.getClazzType();
        List<PropertyValue> propertyValues = bd.getPropertyValues();
        for (PropertyValue pv : propertyValues) {
            String name = pv.getName();
            // 此时的value是不可以直接依赖注入的
            Object value = pv.getValue();

            // 已经处理过之后的了
            ValueResolver valueResolver = new ValueResolver(this);
            Object valueToUse = valueResolver.resolveValue(value, bd);

            ReflectUtils.setProperty(bean, name, valueToUse, clazzType);
        }
    }


    private Object createBeanInstance(BeanDefinition bd) throws Exception {
        // TODO 通过静态工厂
        // TODO 通过工厂方法

        // 通过构造方法
        Object bean = ReflectUtils.createInstance(bd.getClazzType());

        return bean;
    }

}
