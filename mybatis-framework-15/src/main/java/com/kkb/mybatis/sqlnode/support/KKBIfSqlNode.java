package com.kkb.mybatis.sqlnode.support;

import com.kkb.mybatis.sqlnode.KKBDynamicContext;
import com.kkb.mybatis.sqlnode.KKBSqlNode;
import com.kkb.mybatis.utils.KKBOgnlUtils;

/**
 * 存储if标签对应的SQL文本信息
 */
public class KKBIfSqlNode implements KKBSqlNode {
    private String test;
    private KKBSqlNode mixedSqlNode;

    public KKBIfSqlNode(String test,KKBSqlNode mixedSqlNode) {
        this.test = test;
        this.mixedSqlNode = mixedSqlNode;
    }

    @Override
    public void apply(KKBDynamicContext context) {
        boolean aBoolean = KKBOgnlUtils.evaluateBoolean(test, context.getBindings().get("_parameter"));

        if (aBoolean){
            mixedSqlNode.apply(context);
        }
    }
}
