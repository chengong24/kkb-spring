package com.kkb.mybatis.framework.sqlsource;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装解析之后的SQL语句以及对于的#{}的参数信息
 */
public class BoundSql {
    // JDBC可以直接执行的SQL语句
    private String sql;

    private List<ParameterMapping> parameterMappings;

    public BoundSql(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings ;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void addParameterMapping(ParameterMapping parameterMapping) {
        this.parameterMappings.add(parameterMapping);
    }

}
