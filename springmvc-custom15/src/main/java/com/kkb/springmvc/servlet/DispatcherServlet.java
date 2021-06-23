package com.kkb.springmvc.servlet;

import com.kkb.spring.framework.factory.support.DefaultListableBeanFactory;
import com.kkb.spring.framework.reader.XmlBeanDefinitionReader;
import com.kkb.spring.framework.resource.Resource;
import com.kkb.spring.framework.resource.support.ClasspathResource;
import com.kkb.springmvc.adapter.iface.HandlerAdapter;
import com.kkb.springmvc.mapping.iface.HandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * springmvc使用DispatcherServlet类统一处理所有Servlet请求
 */
public class DispatcherServlet extends AbstractServlet {

    private List<HandlerAdapter> handlerAdapters ;
    private List<HandlerMapping> handlerMappings ;

    private DefaultListableBeanFactory beanFactory;

    public static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    /**
     * Servlet生命周期中的初始化方法
     * @param config web.xml中servlet的配置信息
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {

        String location = config.getInitParameter(CONTEXT_CONFIG_LOCATION);
        initSpringContainer(location);
        // 对于spring容器的单例bean一次性初始化
        initSingletonBeans();
        initStrategies();
    }

    private void initSingletonBeans() {
        beanFactory.getBeansByType(Object.class);
    }

    private void initStrategies() {
        handlerMappings = beanFactory.getBeansByType(HandlerMapping.class);
        handlerAdapters = beanFactory.getBeansByType(HandlerAdapter.class);
        /* handlerAdapters.add(new HttpRequestHandlerAdapter());
        handlerAdapters.add(new SimpleControllerHandlerAdapter());
        handlerMappings.add(new BeanNameUrlHandlerMapping());
        handlerMappings.add(new SimpleUrlHandlerMapping());*/
    }

    private void initSpringContainer(String location) {
        beanFactory = new DefaultListableBeanFactory();

        Resource resource = new ClasspathResource(location);
        InputStream inputStream = resource.getResource();

        XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(beanFactory);
        definitionReader.registerBeanDefinitions(inputStream);
    }

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // 根据请求查找对应的处理类（要求该处理器不需要继承或事项Servlet相关的类或接口。如何编写处理类？）
            Object handler = getHandler(request);
            if (handler == null) return;

            /*if(handler instanceof HttpRequestHandler){
                ((HttpRequestHandler)handler).handleRequest(request,response);
            }else if (handler instanceof SimpleControllerHandler){
                ((SimpleControllerHandler)handler).handleRequest(request,response);
            }*/

            // 先要查找对应的处理器适配器
            HandlerAdapter ha = getHandlerAdapter(handler);
            if (ha == null) return;
            // 调用处理类的方法去处理请求
            // 将处理结果响应给浏览器
            // 雪中悍刀行
            ha.handleRequest(handler,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        if (handlerAdapters != null){
            for (HandlerAdapter ha : handlerAdapters) {
                if (ha.supports(handler)){
                    return ha;
                }
            }
        }
        return null;
    }

    private Object getHandler(HttpServletRequest request) {
       /* String uri = request.getRequestURI();
        if ("/queryUser".equals(uri)){
            return new QueryUserHandler();
        }else if("/saveUser".equals(uri)){
            return new SaveUserHandler();
        }*/
        if (handlerMappings != null){
            for (HandlerMapping hm : handlerMappings) {
                Object handler = hm.getHandler(request);
                if (handler != null){
                    return handler;
                }
            }
        }
        return null;
    }
}
