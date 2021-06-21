package com.kkb.mybatis.framework.sqlnode;

/**
 * 提供对封装的SQL脚本信息进行处理操作
 */
public interface SqlNode {
    void apply(DynamicContext context);
}
