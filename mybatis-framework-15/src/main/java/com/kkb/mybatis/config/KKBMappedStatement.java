package com.kkb.mybatis.config;

import com.kkb.mybatis.sqlsource.KKBSqlSource;

public class KKBMappedStatement {
    private String statementId;
    private KKBSqlSource sqlSource;

    private String statementType;

    private Class<?> parameterTypeClass;
    private Class<?> resultTypeClass;

    public KKBMappedStatement(String statementId, Class<?> parameterTypeClass, Class<?> resultTypeClass,
                              String statementType, KKBSqlSource sqlSource) {
        this.statementId = statementId;
        this.parameterTypeClass = parameterTypeClass;
        this.resultTypeClass = resultTypeClass;
        this.statementType = statementType;
        this.sqlSource = sqlSource;
    }

    public KKBSqlSource getSqlSource() {
        return sqlSource;
    }

    public void setSqlSource(KKBSqlSource sqlSource) {
        this.sqlSource = sqlSource;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public Class<?> getParameterTypeClass() {
        return parameterTypeClass;
    }

    public void setParameterTypeClass(Class<?> parameterTypeClass) {
        this.parameterTypeClass = parameterTypeClass;
    }

    public Class<?> getResultTypeClass() {
        return resultTypeClass;
    }

    public void setResultTypeClass(Class<?> resultTypeClass) {
        this.resultTypeClass = resultTypeClass;
    }
}
