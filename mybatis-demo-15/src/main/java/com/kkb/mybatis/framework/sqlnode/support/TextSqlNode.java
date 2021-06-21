package com.kkb.mybatis.framework.sqlnode.support;

import com.kkb.mybatis.framework.sqlnode.DynamicContext;
import com.kkb.mybatis.framework.sqlnode.SqlNode;
import com.kkb.mybatis.framework.utils.*;

/**
 * 存储带有${}的SQL文本信息
 */
public class TextSqlNode implements SqlNode {
    private String sqlText;

    public TextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }

    public boolean isDynamic(){
        if (sqlText.indexOf("${")>-1){
            return true;
        }
        return false;
    }

    @Override
    public void apply(DynamicContext context) {
        // #{}中的内容被GenericTokenParser解析出来之后，需要TokenHandler进行处理
        BindingTokenHandler tokenHandler = new BindingTokenHandler(context);
        GenericTokenParser tokenParser = new GenericTokenParser("${","}",tokenHandler);
        // 得到最终的JDBC可以直接执行的SQL语句
        String sql = tokenParser.parse(sqlText);
        context.appendSql(sql);
    }

    /**
     * 用来处理${}中的参数问题
     */
    class BindingTokenHandler implements TokenHandler{

        private DynamicContext context;

        public BindingTokenHandler(DynamicContext context) {
            this.context = context;
        }

        /**
         *
         * @param content ${}中的参数名称
         * @return ${} 对应的参数值（如果是简单类型，则直接返回，如果是Map类型，需要特殊处理）
         */
        @Override
        public String handleToken(String content) {
            // 获取入参对象
            Object parameter = context.getBindings().get("_parameter");
            // 判断入参类型
            if (parameter == null){
                return "";
            }else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())){
                // 如果入参对象中只传递一个简单类型的参数，其实不需要关心${}中写的是什么
                // 如果是简单类型，则直接返回对应的值
                return parameter.toString();
            }

            // 如果不是简单类型，比如说是Map类型或者User对象类型，都需要通过OGNL表达式去获取对应的值
            Object value = OgnlUtils.getValue(content, parameter);

            return value == null ? "" : value.toString();
        }
    }
}
