package com.kkb.mybatis.statement;

import com.kkb.mybatis.sqlsource.KKBBoundSql;

import java.sql.PreparedStatement;

public interface ParameterHandler {
    void setParameter(PreparedStatement preparedStatement, Object param, KKBBoundSql boundSql)throws  Exception;
}
