package com.kkb.mybatis.statement;

import com.kkb.mybatis.config.KKBMappedStatement;
import com.kkb.mybatis.sqlsource.KKBBoundSql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public interface KKBStatementHandler {
    Statement prepare(Connection connection,String sql) throws Exception;

    void parameterize(Statement statement, Object param, KKBBoundSql boundSql) throws Exception;

    <T> List<T> doQuery(Statement statement, KKBMappedStatement mappedStatement) throws Exception;
}
