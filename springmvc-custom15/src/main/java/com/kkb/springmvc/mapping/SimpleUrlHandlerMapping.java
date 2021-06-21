package com.kkb.springmvc.mapping;

import com.kkb.springmvc.handler.QueryUserHandler;
import com.kkb.springmvc.handler.SaveUserHandler;
import com.kkb.springmvc.mapping.iface.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过xml中的bean标签进行映射关系的维护的
 */
public class SimpleUrlHandlerMapping implements HandlerMapping {
    private Map<String,Object> urlHandlers = new HashMap<>();

    public SimpleUrlHandlerMapping(){
        // TODO 需要读取XML来获取映射关系
        urlHandlers.put("/saveUser",new SaveUserHandler());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return urlHandlers.get(uri);
    }
}
