package com.kkb.mybatis.framework.sqlnode.support;

import com.kkb.mybatis.framework.sqlnode.DynamicContext;
import com.kkb.mybatis.framework.sqlnode.SqlNode;
import com.kkb.mybatis.framework.utils.OgnlUtils;

import java.util.List;

/**
 * 存储if标签对应的SQL文本信息
 */
public class IfSqlNode implements SqlNode {
    private String test;
    private SqlNode mixedSqlNode;

    public IfSqlNode(String test, SqlNode mixedSqlNode) {
        this.test = test;
        this.mixedSqlNode = mixedSqlNode;
    }

    @Override
    public void apply(DynamicContext context) {
        boolean aBoolean = OgnlUtils.evaluateBoolean(test, context.getBindings().get("_parameter"));

        if (aBoolean){
            mixedSqlNode.apply(context);
        }
    }
}
