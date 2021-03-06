package com.kkb.mybatis.sqlsource;

/**
 * 封装#{}解析出来的参数名称以及对应的参数类型
 */
public class KKBParameterMapping {
    private String name;

    private Class type;

    public KKBParameterMapping(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
