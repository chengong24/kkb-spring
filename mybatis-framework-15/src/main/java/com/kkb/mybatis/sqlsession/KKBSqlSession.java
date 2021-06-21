package com.kkb.mybatis.sqlsession;

import java.util.List;

public interface KKBSqlSession {
    /**
     * 查询集合信息
     * @param statementId
     * @param param
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String statementId,Object param);

    /**
     * 查询单个信息
     * @param statementId
     * @param param
     * @param <T>
     * @return
     */
    <T> T selectOne(String statementId,Object param);
}
