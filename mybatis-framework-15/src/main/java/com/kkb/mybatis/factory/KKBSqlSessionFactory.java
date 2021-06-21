package com.kkb.mybatis.factory;

import com.kkb.mybatis.sqlsession.KKBSqlSession;

public interface KKBSqlSessionFactory {

    KKBSqlSession openSession();
}
