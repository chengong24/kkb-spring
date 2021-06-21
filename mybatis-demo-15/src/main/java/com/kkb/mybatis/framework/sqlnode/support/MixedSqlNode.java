package com.kkb.mybatis.framework.sqlnode.support;

import com.kkb.mybatis.framework.sqlnode.DynamicContext;
import com.kkb.mybatis.framework.sqlnode.SqlNode;

import java.util.List;

/**
 * 存储i同一级别的SQL文本信息
 */
public class MixedSqlNode implements SqlNode {
    private List<SqlNode> sqlNodes;

    public MixedSqlNode(List<SqlNode> sqlNodes) {
        this.sqlNodes = sqlNodes;
    }

    @Override
    public void apply(DynamicContext context) {
        for (SqlNode sqlNode : sqlNodes) {
            sqlNode.apply(context);
        }
    }
}
