package com.kkb.mybatis.statement;

import com.kkb.mybatis.sqlsource.KKBBoundSql;
import com.kkb.mybatis.sqlsource.KKBParameterMapping;
import com.kkb.mybatis.utils.KKBSimpleTypeRegistry;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class DefaultParameterHandler implements ParameterHandler{
    @Override
    public void setParameter(PreparedStatement preparedStatement, Object param, KKBBoundSql boundSql) throws  Exception{
        if (KKBSimpleTypeRegistry.isSimpleType(param.getClass())) {
            preparedStatement.setObject(1, param);
        } else if (param instanceof Map) {
            Map map = (Map) param;

            List<KKBParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0 ; i <parameterMappings.size();i++) {
                KKBParameterMapping parameterMapping = parameterMappings.get(i);
                // #{}中参数名称
                String name = parameterMapping.getName();
                Object value = map.get(name);
                preparedStatement.setObject(i + 1, value);
            }

        } else {
            // TODO 暂时不处理
        }
    }
}
