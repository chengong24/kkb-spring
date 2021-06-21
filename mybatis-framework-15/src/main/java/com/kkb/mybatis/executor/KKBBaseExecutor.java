package com.kkb.mybatis.executor;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.config.KKBMappedStatement;
import com.kkb.mybatis.sqlsource.KKBBoundSql;
import com.kkb.mybatis.sqlsource.KKBSqlSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class KKBBaseExecutor implements KKBExecutor {
    private Map<String,List> oneLevelCache = new HashMap();
    @Override
    public <T> List<T> doQuery(KKBConfiguration configuration, KKBMappedStatement mappedStatement, Object param) {
        KKBSqlSource sqlSource = mappedStatement.getSqlSource();
        KKBBoundSql boundSql = sqlSource.getBoundSql(param);
        String sql = boundSql.getSql();
        // 一级缓存处理
        List<Object> list = oneLevelCache.get(sql);
        if (list==null){
            list = queryFromDataSource(configuration,mappedStatement,param,boundSql);
        }
        oneLevelCache.put(sql,list);

        return (List<T>) list;
    }

    public abstract <T> List<T> queryFromDataSource(KKBConfiguration configuration, KKBMappedStatement mappedStatement, Object param, KKBBoundSql boundSql) ;
}
