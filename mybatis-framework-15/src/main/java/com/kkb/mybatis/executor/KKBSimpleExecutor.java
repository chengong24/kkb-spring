package com.kkb.mybatis.executor;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.config.KKBMappedStatement;
import com.kkb.mybatis.sqlsource.KKBBoundSql;
import com.kkb.mybatis.statement.KKBStatementHandler;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KKBSimpleExecutor extends KKBBaseExecutor {
    @Override
    public <T> List<T> queryFromDataSource(KKBConfiguration configuration, KKBMappedStatement mappedStatement, Object param, KKBBoundSql boundSql) {
        Connection connection = null;

        List<T> results = new ArrayList<>();
        try {
            // 获取连接
            connection = getConnection(configuration);

            KKBStatementHandler statementHandler = configuration.newStatementHandler(mappedStatement.getStatementType());
            Statement statement = statementHandler.prepare(connection,boundSql.getSql());
            statementHandler.parameterize(statement,param,boundSql);
            results = statementHandler.doQuery(statement,mappedStatement);

            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    private Connection getConnection(KKBConfiguration configuration) {
        try {
            DataSource dataSource = configuration.getDataSource();
            Connection connection = dataSource.getConnection();
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
