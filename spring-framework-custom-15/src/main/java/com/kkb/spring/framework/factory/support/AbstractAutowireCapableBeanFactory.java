package com.kkb.spring.framework.factory.support;

import com.kkb.spring.framework.aware.Aware;
import com.kkb.spring.framework.aware.BeanFactoryAware;
import com.kkb.spring.framework.config.BeanDefinition;
import com.kkb.spring.framework.config.PropertyValue;
import com.kkb.spring.framework.init.InitializingBean;
import com.kkb.spring.framework.resolver.BeanDefinitionValueResolver;
import com.kkb.spring.framework.utils.ReflectUtils;

import java.util.List;


/**
 * 该方法对BeanFactory的方法进行了实现，但是只是定义了getBean的步骤，而细节部分需要交给子类去实现
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected Object doCreateBean(BeanDefinition bd) {
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
        // TODO 处理Aware接口（标记）
        if (bean instanceof Aware){
            if (bean instanceof BeanFactoryAware){
                ((BeanFactoryAware) bean).setBeanFactory(this);
                //当 A类可以生产BeanFactory的实例
                // C类需要BeanFactory的实例，但是A不会直接调用C类，所以说A无法直接给C注入一个BeanFactory
            }
        }
        // TODO 处理InitializingBean的初始化操作

        invokeInitMethod(bean,bd);
    }

    private void invokeInitMethod(Object bean, BeanDefinition bd) {
        // TODO 对于实现了InitializingBean接口的类进行处理
        if (bean instanceof InitializingBean){
            ((InitializingBean)bean).afterPropertiesSet();
        }
        // 对于配置了init-method标签属性的方法进行调用
        String initMethod = bd.getInitMethod();
        if (initMethod == null ||"".equals(initMethod)) return;
        Class<?> clazzType = bd.getClazzType();

        ReflectUtils.invokeMethod(bean,initMethod,clazzType);

    }

    private void populateBean(Object bean, BeanDefinition bd) throws Exception {
        Class<?> clazzType = bd.getClazzType();
        List<PropertyValue> propertyValues = bd.getPropertyValues();
        for (PropertyValue pv : propertyValues) {
            String name = pv.getName();
            // 此时的value是不可以直接依赖注入的
            Object value = pv.getValue();

            // 已经处理过之后的了
            BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
            Object valueToUse = valueResolver.resolveValue(value,bd);

            ReflectUtils.setProperty(bean,name,valueToUse,clazzType);
        }
    }



    private Object createBeanInstance(BeanDefinition bd) throws Exception{
        // TODO 通过静态工厂
        // TODO 通过工厂方法

        // 通过构造方法
        Object bean = ReflectUtils.createInstance(bd.getClazzType());

        return bean;
    }

}
