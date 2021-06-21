package com.kkb.mybatis.builder;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.config.KKBMappedStatement;
import com.kkb.mybatis.sqlsource.KKBSqlSource;
import com.kkb.mybatis.utils.KKBReflectUtils;
import org.dom4j.Element;

import java.io.InputStream;

/**
 * 专门解析SELECT等statement标签的类
 */
public class KKBXMLStatementBuilder {
    private KKBConfiguration configuration;
    public KKBXMLStatementBuilder(KKBConfiguration configuration) {
        this.configuration = configuration;
    }

    public void parseStatementElement(String namespace, Element selectElement) {
        String statementId = selectElement.attributeValue("id");

        if (statementId == null || statementId.equals("")) {
            return;
        }
        // 一个CURD标签对应一个MappedStatement对象
        // 一个MappedStatement对象由一个statementId来标识，所以保证唯一性
        // statementId = namespace + "." + CRUD标签的id属性
        statementId = namespace + "." + statementId;

        // 注意：parameterType参数可以不设置也可以不解析
        String parameterType = selectElement.attributeValue("parameterType");
        Class<?> parameterClass = KKBReflectUtils.resolveType(parameterType);

        String resultType = selectElement.attributeValue("resultType");
        Class<?> resultClass = KKBReflectUtils.resolveType(resultType);

        String statementType = selectElement.attributeValue("statementType");
        statementType = statementType == null || statementType == "" ? "prepared" : statementType;

        // SqlSource的封装过程
        KKBSqlSource sqlSource = createSqlSource(selectElement);

        // TODO 建议使用构建者模式去优化
        KKBMappedStatement mappedStatement = new KKBMappedStatement(statementId, parameterClass, resultClass, statementType,
                sqlSource);
        configuration.addMappedStatement(statementId, mappedStatement);
    }
    private KKBSqlSource createSqlSource(Element selectElement) {
        KKBXMLScriptBuilder scriptBuilder = new KKBXMLScriptBuilder();
        KKBSqlSource sqlSource = scriptBuilder.parseScriptNode(selectElement);
        return sqlSource;
    }
}
