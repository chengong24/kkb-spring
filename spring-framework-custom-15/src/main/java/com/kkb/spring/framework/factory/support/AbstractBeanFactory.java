package com.kkb.spring.framework.factory.support;

import com.kkb.spring.framework.config.BeanDefinition;
import com.kkb.spring.framework.factory.BeanFactory;
import com.kkb.spring.framework.registry.support.DefaultSingletonBeanRegistry;


/**
 * 该方法对BeanFactory的方法进行了实现，但是只是定义了getBean的步骤，而细节部分需要交给子类去实现
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String name) {
        // 注意事项
        // 1.是否每次调用getBean都需要创建一个新的Bean？其实可以使用单例方式去管理Bean。
        // 2.XML配置文件需要被解析几次，是不是每次调用getBean时才进行XML解析呢？答案是只需要解析一次，且在getBean之前就完成解析

        // 改造思路：
        // 1.先去管理Bean的集合缓存中查找对应beanName的bean实例
        Object bean = getSingleton(name);
        // 2.如果找到，则直接返回该bean
        if (bean != null) return bean;
        // 3.如果没有找到，则需要创建对应的bean实例，此时要判断该bean想以单例方式创建还是原型方式创建
        // 3.1.可以使用XML配置文件的方式来配置beanName和Bean实例对应的关系，同时可以配置Bean被创建需要依赖注入的参数
        // 3.2.一次性解析XML配置文件，形成对应的配置对象（BeanDefinition集合）

        BeanDefinition bd = getBeanDefinition(name);
        if (bd == null) return null;

        // 单例
        if(bd.isSingleton()){
            // 3.3.根据beanName找到对应的配置对象（BeanDefinition），来创建Bean实例
            bean = doCreateBean(bd);
            // 4.将创建完成的bean实例放入管理Bean的集合缓存
            addSingleton(name,bean);
        }else if(bd.isPrototype()){
            // 原型（多例）
            bean = doCreateBean(bd);
        }

        return bean;
    }

    /**
     * 使用抽象模板方法，将具体创建Bean实例的功能延迟到子类去实现
     * @param bd
     * @return
     */
    protected abstract Object doCreateBean(BeanDefinition bd);

    /**
     * 使用抽象模板方法，将获取BeanDefinition的功能延迟到子类去实现
     * @param name
     * @return
     */
    protected abstract BeanDefinition getBeanDefinition(String name);
}
