package com.kkb.mybatis.test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kkb.mybatis.builder.KKBSqlSessionFactoryBuilder;
import com.kkb.mybatis.factory.KKBSqlSessionFactory;
import com.kkb.mybatis.io.KKBResources;
import com.kkb.mybatis.po.User;
import com.kkb.mybatis.sqlsession.KKBSqlSession;
import org.junit.Before;
import org.junit.Test;


/**
 * 1.以面向对象的思维去改造mybatis手写框架 2.将手写的mybatis的代码封装一个通用的框架（java工程）给程序员使用
 *
 * @author 灭霸詹
 *
 */
public class MybatisV3 {
    private KKBSqlSessionFactory sqlSessionFactory;

    @Before
    public void init(){
        InputStream inputStream = KKBResources.getResourceAsStream("mybatis-config.xml");
        // SqlSessionFactory的创建流程
        sqlSessionFactory = new KKBSqlSessionFactoryBuilder().build(inputStream);
    }

	@Test
    public void test(){
	    // 调用SqlSession完成增删改查操作
        // 注意：SqlSessionFactory全局只需要创建一次即可
        // 注意：SqlSession每次执行CRUD都需要至少创建一次
        KKBSqlSession sqlSession = sqlSessionFactory.openSession();
        // 根据用户性别和用户名称查询用户信息
        Map<String,Object> param = new HashMap<>();
        param.put("username","王五");
        param.put("sex","男");
        List<User> users = sqlSession.selectList("test.queryUserById", param);
        System.out.println(users);
    }
}