package com.kkb.mybatis.statement;

import com.kkb.mybatis.config.KKBMappedStatement;

import java.sql.ResultSet;
import java.util.List;

public interface ResultSetHandler {
    <T> void handleResultSet(ResultSet resultSet, List<T> results, KKBMappedStatement mappedStatement) throws  Exception;
}
