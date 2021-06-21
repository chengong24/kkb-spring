package com.kkb.mybatis.factory;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.sqlsession.KKBDefaultSqlSession;
import com.kkb.mybatis.sqlsession.KKBSqlSession;

public class KKBDefaultSqlSessionFactory implements KKBSqlSessionFactory {
    private KKBConfiguration configuration;

    public KKBDefaultSqlSessionFactory(KKBConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public KKBSqlSession openSession() {
        return new KKBDefaultSqlSession(configuration);
    }
}
