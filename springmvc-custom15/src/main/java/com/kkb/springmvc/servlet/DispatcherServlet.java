package com.kkb.springmvc.servlet;

import com.kkb.springmvc.adapter.HttpRequestHandlerAdapter;
import com.kkb.springmvc.adapter.SimpleControllerHandlerAdapter;
import com.kkb.springmvc.adapter.iface.HandlerAdapter;
import com.kkb.springmvc.handler.QueryUserHandler;
import com.kkb.springmvc.handler.SaveUserHandler;
import com.kkb.springmvc.handler.iface.HttpRequestHandler;
import com.kkb.springmvc.handler.iface.SimpleControllerHandler;
import com.kkb.springmvc.mapping.BeanNameUrlHandlerMapping;
import com.kkb.springmvc.mapping.SimpleUrlHandlerMapping;
import com.kkb.springmvc.mapping.iface.HandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * springmvc使用DispatcherServlet类统一处理所有Servlet请求
 */
public class DispatcherServlet extends AbstractServlet {

    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    /**
     * Servlet生命周期中的初始化方法
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        handlerAdapters.add(new HttpRequestHandlerAdapter());
        handlerAdapters.add(new SimpleControllerHandlerAdapter());
        handlerMappings.add(new BeanNameUrlHandlerMapping());
        handlerMappings.add(new SimpleUrlHandlerMapping());
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
