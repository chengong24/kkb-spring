package com.kkb.mybatis.framework.sqlnode.support;

import com.kkb.mybatis.framework.sqlnode.DynamicContext;
import com.kkb.mybatis.framework.sqlnode.SqlNode;

/**
 * 存储带有#{}或者没有特殊字符的SQL文本信息
 */
public class StaticTextSqlNode implements SqlNode {
    private String sqlText;

    public StaticTextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }
    @Override
    public void apply(DynamicContext context) {
        context.appendSql(sqlText);
    }
}
