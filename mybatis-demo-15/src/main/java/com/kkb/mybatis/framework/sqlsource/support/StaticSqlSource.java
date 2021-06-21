package com.kkb.mybatis.framework.sqlsource.support;

import com.kkb.mybatis.framework.sqlsource.BoundSql;
import com.kkb.mybatis.framework.sqlsource.ParameterMapping;
import com.kkb.mybatis.framework.sqlsource.SqlSource;

import java.util.List;

/**
 * 封装DynamicSqlSource和RawSqlSource解析之后的结果
 */
public class StaticSqlSource implements SqlSource {
    // JDBC可以直接执行的SQL语句
    private String sql;

    private List<ParameterMapping> parameterMappings;

    public StaticSqlSource(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    @Override
    public BoundSql getBoundSql(Object param) {
        return new BoundSql(sql,parameterMappings);
    }
}
