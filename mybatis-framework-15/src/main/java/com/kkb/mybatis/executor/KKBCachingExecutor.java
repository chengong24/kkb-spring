package com.kkb.mybatis.executor;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.config.KKBMappedStatement;

import java.util.List;

public class KKBCachingExecutor implements KKBExecutor {
    private KKBExecutor delegate;

    public KKBCachingExecutor(KKBExecutor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> List<T> doQuery(KKBConfiguration configuration, KKBMappedStatement mappedStatement, Object param) {
        // TODO 二级缓存处理
        return delegate.doQuery(configuration, mappedStatement,param);
    }
}
