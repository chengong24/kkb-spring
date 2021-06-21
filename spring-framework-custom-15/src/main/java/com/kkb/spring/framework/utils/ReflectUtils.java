package com.kkb.spring.framework.utils;

import com.kkb.spring.framework.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {
    public static void setProperty(Object bean,String name,Object valueToUse,Class clazzType)  {
        Field field = null;
        try {
            field = clazzType.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean,valueToUse);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void invokeMethod(Object bean,String initMethod,Class clazzType){
        Method method = null;
        try {
            method = clazzType.getDeclaredMethod(initMethod);
            method.invoke(bean,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  Object createInstance(Class<?> clazzType) {
        try {
            Constructor<?> constructor = clazzType.getDeclaredConstructor();
            Object bean = constructor.newInstance();
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getTypeByFieldName(String beanClassName, String name) {
        try {
            Class<?> clazz = Class.forName(beanClassName);
            Field field = clazz.getDeclaredField(name);
            return field.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
