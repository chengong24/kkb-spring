package com.kkb.mybatis.framework.sqlnode;

import java.util.HashMap;
import java.util.Map;

/**
 * SqlNode处理过程中的上下文对象
 */
public class DynamicContext {
    // 为了将所有SqlNode处理之后的信息，拼接成一条完整的SQL语句
    private StringBuffer sb = new StringBuffer();

    // 为了SqlNode执行过程中需要的一些信息
    private Map<String,Object> bindings = new HashMap<>();

    public DynamicContext(Object param) {
        this.bindings.put("_parameter",param);
    }

    public String getSql() {
        return sb.toString();
    }

    public void appendSql(String sqlText) {
        this.sb.append(sqlText);
        this.sb.append(" ");
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void addBinding(String name, Object binding) {
        this.bindings.put(name,binding);
    }
}
