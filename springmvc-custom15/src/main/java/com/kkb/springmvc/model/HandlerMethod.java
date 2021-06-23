package com.kkb.springmvc.model;

import java.lang.reflect.Method;

/**
 * 用来封装Controller实例和里面处理请求的方法对象
 */
public class HandlerMethod {
    private Object controller;

    private Method method;

    public HandlerMethod(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
