package com.kkb.mybatis.builder;

import com.kkb.mybatis.config.KKBConfiguration;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.List;

/**
 * 专门解析映射配置文件的类
 */
public class KKBXMLMapperBuilder {
    private KKBConfiguration configuration;

    public KKBXMLMapperBuilder(KKBConfiguration configuration) {
        this.configuration = configuration;
    }

    public void parse(Element rootElement) {
        String namespace = rootElement.attributeValue("namespace");
        // TODO 获取动态SQL标签，比如<sql>
        // TODO 获取其他标签
        List<Element> selectElements = rootElement.elements("select");
        for (Element selectElement : selectElements) {
            KKBXMLStatementBuilder statementBuilder = new KKBXMLStatementBuilder(configuration);
            statementBuilder.parseStatementElement(namespace,selectElement);
        }
    }
}
