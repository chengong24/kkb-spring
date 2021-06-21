package com.kkb.mybatis.sqlnode.support;

import com.kkb.mybatis.sqlnode.KKBDynamicContext;
import com.kkb.mybatis.sqlnode.KKBSqlNode;

/**
 * 存储带有#{}或者没有特殊字符的SQL文本信息
 */
public class KKBStaticTextSqlNode implements KKBSqlNode {
    private String sqlText;

    public KKBStaticTextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }
    @Override
    public void apply(KKBDynamicContext context) {
        context.appendSql(sqlText);
    }
}
