package com.kkb.mybatis.executor;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.config.KKBMappedStatement;

import java.util.List;

public interface KKBExecutor {
    <T> List<T> doQuery(KKBConfiguration configuration, KKBMappedStatement mappedStatement, Object param);
}
