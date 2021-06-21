package com.kkb.mybatis.sqlsource.support;

import com.kkb.mybatis.sqlsource.KKBBoundSql;
import com.kkb.mybatis.sqlsource.KKBParameterMapping;
import com.kkb.mybatis.sqlsource.KKBSqlSource;

import java.util.List;

/**
 * 封装DynamicSqlSource和RawSqlSource解析之后的结果
 */
public class KKBStaticSqlSource implements KKBSqlSource {
    // JDBC可以直接执行的SQL语句
    private String sql;

    private List<KKBParameterMapping> parameterMappings;

    public KKBStaticSqlSource(String sql, List<KKBParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    @Override
    public KKBBoundSql getBoundSql(Object param) {
        return new KKBBoundSql(sql,parameterMappings);
    }
}
