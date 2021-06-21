package com.kkb.springmvc.mapping.iface;

import javax.servlet.http.HttpServletRequest;

/**
 * 接口是对外提供查找处理的功能的
 * 接口的实现类需要维护处理器和请求的映射关系
 */
public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);
}
