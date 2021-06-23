package com.kkb.springmvc.handler;

import com.kkb.springmvc.handler.iface.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class QueryUserHandler implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        response.setContentType("text/plain;charset=utf8");
        response.getWriter().write("id123 : "+ id +"--name123 : "+name);
    }
}
