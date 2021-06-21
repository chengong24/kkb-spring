package com.kkb.mybatis.sqlsource;

public interface KKBSqlSource {
    /**
     * 解析封装好的SqlNode信息
     * @param param
     * @return
     */
    KKBBoundSql getBoundSql(Object param) ;
}
