package com.kkb.mybatis.statement;


import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.config.KKBMappedStatement;
import com.kkb.mybatis.sqlsource.KKBBoundSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KKBPreparedStatementHandler implements KKBStatementHandler {
    private ParameterHandler parameterHandler;
    private ResultSetHandler resultSetHandler;

    public KKBPreparedStatementHandler(KKBConfiguration configuration) {
        this.parameterHandler = configuration.newParameterHandler();
        this.resultSetHandler = configuration.newResultSetHandler();
    }

    @Override
    public Statement prepare(Connection connection,String sql) throws Exception{
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterize(Statement statement, Object param, KKBBoundSql boundSql)  throws Exception{
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        parameterHandler.setParameter(preparedStatement,param,boundSql);
    }

    @Override
    public <T> List<T> doQuery(Statement statement, KKBMappedStatement mappedStatement)  throws Exception{
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        ResultSet resultSet = preparedStatement.executeQuery();
        List<T> results = new ArrayList<>();
        resultSetHandler.handleResultSet(resultSet, results, mappedStatement);
        return results;
    }
}
