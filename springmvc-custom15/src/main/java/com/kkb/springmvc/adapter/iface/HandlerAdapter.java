package com.kkb.springmvc.adapter.iface;

import com.kkb.springmvc.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理器适配器接口
 */
public interface HandlerAdapter {
    boolean supports(Object handler);

    ModelAndView handleRequest(Object handler,HttpServletRequest request,HttpServletResponse response) throws Exception;
}
