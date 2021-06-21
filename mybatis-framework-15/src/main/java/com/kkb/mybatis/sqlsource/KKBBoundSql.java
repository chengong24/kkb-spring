package com.kkb.mybatis.sqlsource;

import java.util.List;

/**
 * 封装解析之后的SQL语句以及对于的#{}的参数信息
 */
public class KKBBoundSql {
    // JDBC可以直接执行的SQL语句
    private String sql;

    private List<KKBParameterMapping> parameterMappings;

    public KKBBoundSql(String sql, List<KKBParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings ;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<KKBParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void addParameterMapping(KKBParameterMapping parameterMapping) {
        this.parameterMappings.add(parameterMapping);
    }

}
