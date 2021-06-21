package com.kkb.mybatis.sqlnode;

/**
 * 提供对封装的SQL脚本信息进行处理操作
 */
public interface KKBSqlNode {
    void apply(KKBDynamicContext context);
}
