package com.kkb.mybatis.framework.sqlsource;

public interface SqlSource {
    /**
     * 解析封装好的SqlNode信息
     * @param param
     * @return
     */
    BoundSql getBoundSql(Object param) ;
}
