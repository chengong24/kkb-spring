package com.kkb.mybatis.statement;

import com.kkb.mybatis.config.KKBMappedStatement;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

public class DefaultResultSetHandler implements ResultSetHandler{
    @Override
    public <T> void handleResultSet(ResultSet resultSet, List<T> results, KKBMappedStatement mappedStatement) throws  Exception{
        // 遍历查询结果集
        Class<?> resultTypeClass = mappedStatement.getResultTypeClass();

        Object result = null;
        while (resultSet.next()) {
            // 反射创建映射结果对象
            result = resultTypeClass.newInstance();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // result实例中要设置的每一个属性的值，都是来自于resultSet结果集中某一行的每一列的值
            for (int i = 1; i <= columnCount ; i++) {
                // 获取的结果集中列的名称
                String columnName = metaData.getColumnName(i);
                // 列的名称和属性的名称必须一致
                Field field = resultTypeClass.getDeclaredField(columnName);
                field.setAccessible(true);
                // 通过反射给属性赋值
                field.set(result,resultSet.getObject(i));
            }

            results.add((T) result);
        }
    }

}
