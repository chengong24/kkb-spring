package com.kkb.springmvc.handler.iface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 制定一种处理类的编写规范
 */
public interface HttpRequestHandler {

    public void handleRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
}
