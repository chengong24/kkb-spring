package com.kkb.springmvc.adapter;

import com.kkb.springmvc.adapter.iface.HandlerAdapter;
import com.kkb.springmvc.handler.iface.HttpRequestHandler;
import com.kkb.springmvc.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 适配并执行HttpRequestHandler处理器的
 */
public class HttpRequestHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HttpRequestHandler);
    }

    @Override
    public ModelAndView handleRequest(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ((HttpRequestHandler)handler).handleRequest(request,response);
        return null;
    }
}
