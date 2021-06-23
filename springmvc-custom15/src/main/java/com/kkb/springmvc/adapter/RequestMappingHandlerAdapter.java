package com.kkb.springmvc.adapter;

import com.kkb.springmvc.adapter.iface.HandlerAdapter;
import com.kkb.springmvc.annotation.ResponseBody;
import com.kkb.springmvc.model.HandlerMethod;
import com.kkb.springmvc.model.ModelAndView;
import com.kkb.springmvc.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 专门用来执行注解方式的处理器方法
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HandlerMethod);
    }

    @Override
    public ModelAndView handleRequest(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod hm = (HandlerMethod) handler;
        Object controller = hm.getController();
        Method method = hm.getMethod();

        // 将请求URL中String类型的value数据，转换成Controller方法参数的实际类型数据
        Object[] args = handleParameters(request,method);
        // 执行处理器方法
        Object returnValue = method.invoke(controller, args);

        // 处理返回值@ResponseBoddy
        handleReturnValue(returnValue,method,response);

        return null;
    }

    /**
     * 将返回值根据类型进行处理
     * @param returnValue
     * @param method
     * @param response
     */
    private void handleReturnValue(Object returnValue, Method method, HttpServletResponse response) throws Exception{
        boolean annotationPresent = method.isAnnotationPresent(ResponseBody.class);
        if (annotationPresent){
            if (returnValue instanceof String){
                response.setContentType("text/plain;charset=utf8");
                response.getWriter().write(returnValue.toString());
            }else if (returnValue instanceof Map){
                response.setContentType("application/json;charset=utf8");
                // 将map对象使用工具，转成json格式字符串
                response.getWriter().write(JsonUtils.object2Json(returnValue));
            }else{
                // TODO
            }
        }else {
            // TODO 视图处理
        }
    }

    /**
     * 将请求URL中的KV数据，封装到Controller方法中的参数对象中
     * @param request
     * @param method
     * @return
     */
    private Object[] handleParameters(HttpServletRequest request, Method method) throws Exception{
        List parameterList = new ArrayList();

        // 取出请求中的所有参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 取出Method对象中的参数集合
        Parameter[] parameters = method.getParameters();
        // 遍历Method中的集合参数
        for (Parameter parameter : parameters) {

            // 取出来方法参数名称(注意：此处需要特殊处理，否则取出来的name名称是arg0,arg1)
            String name = parameter.getName();
            // 根据参数名称去Request请求参数集合中获取对应的值（请求中的key和Controller方法参数名称要一致）
            String[] stringValues = parameterMap.get(name);
            // 将String类型的值，进行转换
            Class<?> type = parameter.getType();
            Object valueToUse = resolveParameterType(stringValues,type);

            // 将转换之后的值添加到集合中存储
            parameterList.add(valueToUse);
        }


        return parameterList.toArray();
    }

    private Object resolveParameterType(String[] stringValues, Class<?> type) throws Exception{
        if (type == Integer.class){
            return Integer.parseInt(stringValues[0]);
        }else if (type == String.class){
            return stringValues[0];
        }else if (type == List.class){
            // TODO
        }else if (type == Map.class){
            // TODO
        }else{
            // TODO
        }
        return null;
    }
}
