package com.kkb.mybatis.config;

import com.kkb.mybatis.executor.KKBCachingExecutor;
import com.kkb.mybatis.executor.KKBExecutor;
import com.kkb.mybatis.executor.KKBSimpleExecutor;
import com.kkb.mybatis.statement.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.logging.SimpleFormatter;

/**
 * 用来封装整个xml配置文件的信息
 */
public class KKBConfiguration {
    private DataSource dataSource;

    private Map<String, KKBMappedStatement> mappedStatements = new HashMap<>();

    private boolean useCache = true;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public KKBMappedStatement getMappedStatementById(String statementId) {
        return mappedStatements.get(statementId);
    }

    public void addMappedStatement(String statementId, KKBMappedStatement mappedStatement) {
        this.mappedStatements.put(statementId, mappedStatement);
    }

    public KKBExecutor newExecutor(String type) {
        KKBExecutor executor = null;
        type = type == null ? "simple" : type;
        //真正的执行器是谁，需要通过配置来指定，如果不指定，默认就是SimpleExecutor
        if (type.equals("simple")){
            executor = new KKBSimpleExecutor();
        }

        if (useCache){
            executor = new KKBCachingExecutor(executor);
        }
        return executor;
    }

    public KKBStatementHandler newStatementHandler(String statementType) {
        KKBStatementHandler statementHandler = null;
        if (statementType.equals("prepared")){
            statementHandler = new KKBPreparedStatementHandler(this);
        }
        return statementHandler;
    }

    public ParameterHandler newParameterHandler() {
        return new DefaultParameterHandler();
    }

    public ResultSetHandler newResultSetHandler() {
        return new DefaultResultSetHandler();
    }
}
