package com.kkb.springmvc.mapping;


import com.kkb.spring.framework.aware.BeanFactoryAware;
import com.kkb.spring.framework.config.BeanDefinition;
import com.kkb.spring.framework.factory.BeanFactory;
import com.kkb.spring.framework.factory.support.DefaultListableBeanFactory;
import com.kkb.spring.framework.init.InitializingBean;
import com.kkb.springmvc.annotation.Controller;
import com.kkb.springmvc.annotation.RequestMapping;
import com.kkb.springmvc.mapping.iface.HandlerMapping;
import com.kkb.springmvc.model.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * 通过@RequestMapping进行映射关系的维护的
 */
public class RequestMappingHandlerMapping implements HandlerMapping , BeanFactoryAware , InitializingBean{
    private Map<String, HandlerMethod> urlHandlers = new HashMap<>();

    // 需要依赖注入的BeanFactory
    private DefaultListableBeanFactory beanFactory;

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
        // 遍历所有的BeanDefinition信息
        List<BeanDefinition> beanDefinitions = beanFactory.getBeanDefinitions();
        for (BeanDefinition bd : beanDefinitions) {
            String beanName = bd.getBeanName();
            // 从BeanDefinition中获取bean的Class类对象
            Class<?> clazzType = bd.getClazzType();
            // 判断该Class对应的类型是否是使用@Controller注解的处理器类的类型
            if (isHandler(clazzType)){
                // 获取该Class中的所有方法对象
                Method[] methods = clazzType.getDeclaredMethods();
                // 遍历Controller中的方法
                for (Method method : methods) {
                    // 判断这些方法是否是使用@RequestMapping注解的方法
                    if (method.isAnnotationPresent(RequestMapping.class)){
                        StringBuffer sb = new StringBuffer();

                        RequestMapping clazzRequestMapping = clazzType.getAnnotation(RequestMapping.class);
                        String clazzUrl = clazzRequestMapping.value();
                        if (clazzUrl != null ){
                            if (clazzUrl.startsWith("/")){
                                sb.append(clazzUrl);
                            }else{
                                sb.append("/").append(clazzUrl);
                            }
                        }

                        // 如果是，则取出Controller类上和方法中的RequestMapping注解中的url，进行合并处理
                        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                        String methodUrl = methodRequestMapping.value();
                        if (methodUrl != null ){
                            if (methodUrl.startsWith("/")){
                                sb.append(methodUrl);
                            }else{
                                sb.append("/").append(methodUrl);
                            }
                        }
                        // 最终是要建立请求URL和HandlerMethod的映射关系
                        HandlerMethod hm = new HandlerMethod(beanFactory.getBean(beanName),method);
                        urlHandlers.put(sb.toString(),hm);
                    }
                }
            }
        }
    }

    private boolean isHandler(Class clazz){
        return (clazz.isAnnotationPresent(Controller.class)  || clazz.isAnnotationPresent(RequestMapping.class));
    }
}
