package com.kkb.mybatis.sqlnode.support;

import com.kkb.mybatis.sqlnode.KKBDynamicContext;
import com.kkb.mybatis.sqlnode.KKBSqlNode;

import java.util.List;

/**
 * 存储i同一级别的SQL文本信息
 */
public class KKBMixedSqlNode implements KKBSqlNode {
    private List<KKBSqlNode> sqlNodes;

    public KKBMixedSqlNode(List<KKBSqlNode> sqlNodes) {
        this.sqlNodes = sqlNodes;
    }

    @Override
    public void apply(KKBDynamicContext context) {
        for (KKBSqlNode sqlNode : sqlNodes) {
            sqlNode.apply(context);
        }
    }
}
