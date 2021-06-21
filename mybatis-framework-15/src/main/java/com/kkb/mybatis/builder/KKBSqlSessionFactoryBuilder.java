package com.kkb.mybatis.builder;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.factory.KKBDefaultSqlSessionFactory;
import com.kkb.mybatis.factory.KKBSqlSessionFactory;

import java.io.InputStream;
import java.io.Reader;

public class KKBSqlSessionFactoryBuilder {

    public KKBSqlSessionFactory build(InputStream inputStream){

        KKBXMLConfigBuilder configBuilder = new KKBXMLConfigBuilder();
        KKBConfiguration configuration = configBuilder.parse(inputStream);
        return build(configuration);
    }
    public KKBSqlSessionFactory build(Reader reader){
        return null;
    }

    private KKBSqlSessionFactory build(KKBConfiguration configuration){
        return new KKBDefaultSqlSessionFactory(configuration);
    }
}
