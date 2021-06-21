package com.kkb.mybatis.sqlsource.support;


import com.kkb.mybatis.sqlnode.KKBDynamicContext;
import com.kkb.mybatis.sqlnode.KKBSqlNode;
import com.kkb.mybatis.sqlsource.KKBBoundSql;
import com.kkb.mybatis.sqlsource.KKBSqlSource;
import com.kkb.mybatis.utils.KKBGenericTokenParser;
import com.kkb.mybatis.utils.KKBParameterMappingTokenHandler;

/**
 * 封装解析出来的SqlNode信息（包含动态标签或者${}）
 * 注意事项：${}需要每一次执行getBoundSql的时候，被解析一次
 */
public class KKBDynamicSqlSource implements KKBSqlSource {
    // 解析出来的所有SqlNode节点信息
    private KKBSqlNode rootSqlNode;

    public KKBDynamicSqlSource(KKBSqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    @Override
    public KKBBoundSql getBoundSql(Object param) {
        // 解析所有的SqlNode节点信息，将SQL语句拼接成一条语句(解析了${}，但是没有解析#{})
        KKBDynamicContext context = new KKBDynamicContext(param);
        rootSqlNode.apply(context);
        // 此时拼接成的SQL语句中，可能还包含#{}，所以还要对#{}进行解析
        String sqlText = context.getSql();
        // #{}中的内容被GenericTokenParser解析出来之后，需要TokenHandler进行处理
        KKBParameterMappingTokenHandler tokenHandler = new KKBParameterMappingTokenHandler();
        KKBGenericTokenParser tokenParser = new KKBGenericTokenParser("#{","}",tokenHandler);
        // 得到最终的JDBC可以直接执行的SQL语句
        String sql = tokenParser.parse(sqlText);

        // 将SQL语句和解析#{}产生的参数列表信息封装成BoundSql
        return new KKBBoundSql(sql,tokenHandler.getParameterMappings());
    }
}
