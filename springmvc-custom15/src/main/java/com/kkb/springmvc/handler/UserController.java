package com.kkb.springmvc.handler;

import com.kkb.springmvc.annotation.Controller;
import com.kkb.springmvc.annotation.RequestMapping;
import com.kkb.springmvc.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用注解开发的处理器
 * 注意实现：
 * 在springmvc中定义的处理器是一个请求对应一个处理器，但是Controller类中有很多请求对应的方法
 *
 * 所以说，在Controller类中，真正的处理器类，不是Controller本身，而是Controller类中的方法
 *
 * 通过定义一个类来封装Controller中的处理器HandlerMethod（Object controller，Method method）
 */
@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping("query")
    @ResponseBody
    public Map<String,Object> query(Integer id,String name){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("name",name);
        return map;
    }

    @RequestMapping("save")
    @ResponseBody
    public String save(){
        return "UserController---添加成功";
    }
}
