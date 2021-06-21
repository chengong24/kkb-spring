package com.kkb.mybatis.framework.sqlsource.support;

import com.kkb.mybatis.framework.sqlnode.DynamicContext;
import com.kkb.mybatis.framework.sqlnode.SqlNode;
import com.kkb.mybatis.framework.sqlsource.BoundSql;
import com.kkb.mybatis.framework.sqlsource.SqlSource;
import com.kkb.mybatis.framework.utils.GenericTokenParser;
import com.kkb.mybatis.framework.utils.ParameterMappingTokenHandler;

/**
 * 封装解析出来的SqlNode信息（包含非动态标签或者#{}）
 * 注意事项：#{}只需要被解析一次就可以了
 */
public class RawSqlSource implements SqlSource {

    private SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {
        // 解析所有的SqlNode节点信息，将SQL语句拼接成一条语句(解析了${}，但是没有解析#{})
        DynamicContext context = new DynamicContext(null);
        rootSqlNode.apply(context);
        // 此时拼接成的SQL语句中，可能还包含#{}，所以还要对#{}进行解析
        String sqlText = context.getSql();
        // #{}中的内容被GenericTokenParser解析出来之后，需要TokenHandler进行处理
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser tokenParser = new GenericTokenParser("#{","}",tokenHandler);
        // 得到最终的JDBC可以直接执行的SQL语句
        String sql = tokenParser.parse(sqlText);

        // 将SQL语句和解析#{}产生的参数列表信息封装成BoundSql
        sqlSource = new StaticSqlSource(sql,tokenHandler.getParameterMappings());
    }

    @Override
    public BoundSql getBoundSql(Object param) {
        return sqlSource.getBoundSql(param);
    }
}
