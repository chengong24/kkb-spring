package com.kkb.springmvc.handler;

import com.kkb.springmvc.handler.iface.HttpRequestHandler;
import com.kkb.springmvc.handler.iface.SimpleControllerHandler;
import com.kkb.springmvc.model.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SaveUserHandler implements SimpleControllerHandler {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=utf8");
        response.getWriter().write("添加成功111111！！！");

        return null;
    }
}
