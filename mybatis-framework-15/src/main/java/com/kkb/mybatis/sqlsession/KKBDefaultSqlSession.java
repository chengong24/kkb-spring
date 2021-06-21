package com.kkb.mybatis.sqlsession;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.config.KKBMappedStatement;
import com.kkb.mybatis.executor.KKBExecutor;

import java.util.List;

public class KKBDefaultSqlSession implements KKBSqlSession {
    private KKBConfiguration configuration;

    public KKBDefaultSqlSession(KKBConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> List<T> selectList(String statementId, Object param) {
        KKBMappedStatement mappedStatement = configuration.getMappedStatementById(statementId);
        // 真正的JDBC操作，SqlSession是委托给Executor去完成，为什么要委托给Executor去完成呢？
        KKBExecutor executor = configuration.newExecutor(null);
        return executor.doQuery(configuration,mappedStatement,param);
    }

    @Override
    public <T> T selectOne(String statementId, Object param) {
        List<Object> list = this.selectList(statementId, param);
        if (list!=null&& list.size() == 1){
            return (T) list.get(0);
        }else {
            // TODO 抛出异常
        }
        return null;
    }
}
