package com.kkb.springmvc.mapping;


import com.kkb.spring.framework.aware.BeanFactoryAware;
import com.kkb.spring.framework.config.BeanDefinition;
import com.kkb.spring.framework.factory.BeanFactory;
import com.kkb.spring.framework.factory.support.DefaultListableBeanFactory;
import com.kkb.spring.framework.init.InitializingBean;
import com.kkb.springmvc.mapping.iface.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过xml中的bean标签进行映射关系的维护的
 */
public class BeanNameUrlHandlerMapping implements HandlerMapping , BeanFactoryAware , InitializingBean{
    private Map<String,Object> urlHandlers = new HashMap<>();

    // 需要依赖注入的BeanFactory
    private DefaultListableBeanFactory beanFactory;

    // 不能再构造方法中进行urlHandlers集合的处理
    /*public BeanNameUrlHandlerMapping(){
        // TODO 需要读取XML来获取映射关系
        // urlHandlers.put("/queryUser",new QueryUserHandler());
        // 如何获取BeanDefinition呢?只需要获取BeanFactory就可以获取BeanDefinition
    }*/


    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return urlHandlers.get(uri);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        // TODO 需要读取XML来获取映射关系
        // urlHandlers.put("/queryUser",new QueryUserHandler());
        // 如何获取BeanDefinition呢?只需要获取BeanFactory就可以获取BeanDefinition
        List<BeanDefinition> beanDefinitions = beanFactory.getBeanDefinitions();
        for (BeanDefinition bd : beanDefinitions) {
            String beanName = bd.getBeanName();
            if (beanName.startsWith("/")){
                urlHandlers.put(beanName,beanFactory.getBean(beanName));
            }
        }
    }
}
