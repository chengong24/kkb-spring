package com.kkb.mybatis.test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import com.kkb.mybatis.framework.config.Configuration;
import com.kkb.mybatis.framework.config.MappedStatement;
import com.kkb.mybatis.framework.sqlnode.SqlNode;
import com.kkb.mybatis.framework.sqlnode.support.IfSqlNode;
import com.kkb.mybatis.framework.sqlnode.support.MixedSqlNode;
import com.kkb.mybatis.framework.sqlnode.support.StaticTextSqlNode;
import com.kkb.mybatis.framework.sqlnode.support.TextSqlNode;
import com.kkb.mybatis.framework.sqlsource.BoundSql;
import com.kkb.mybatis.framework.sqlsource.ParameterMapping;
import com.kkb.mybatis.framework.sqlsource.SqlSource;
import com.kkb.mybatis.framework.sqlsource.support.DynamicSqlSource;
import com.kkb.mybatis.framework.sqlsource.support.RawSqlSource;
import com.kkb.mybatis.framework.utils.SimpleTypeRegistry;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.kkb.mybatis.po.User;


/**
 * 1.properties配置文件升级为XML配置文件 2.使用面向过程思维去优化代码
 * 
 * @author 灭霸詹
 *
 */
public class MybatisV2 {
    private Configuration configuration = new Configuration();

    private String namespace;

    private boolean isDynamic;
    @Test
    public void test(){
        // 加载XML文件，将数据封装到Configuration对象中
        loadXML("mybatis-config.xml");
        // 执行用户查询操作
        // 根据用户名称查询列表信息
        Map<String, Object> params = new HashMap<>();
        params.put("sex", "男");
        params.put("username", "王五");
		List<User> users = selectList("test.queryUserById",params);

        System.out.println(users);
    }

    private void loadXML(String s) {
        // 根据xml的路径，获取对应的输入流
        InputStream inputStream = getResourceAsStream(s);
        // 将流对象，转换成Document对象
        Document document = createDocument(inputStream);
        // 针对Document对象，按照Mybatis的语义去解析Document
        parseConfiguration(document.getRootElement());
    }

    private void parseConfiguration(Element rootElement) {
        Element environments = rootElement.element("environments");
        parseEnvironments(environments);
        Element mappers = rootElement.element("mappers");
        parseMappers(mappers);
    }

    /**
     * 解析<mappers></>
     * @param mappers
     */
    private void parseMappers(Element mappers) {
        List<Element> list = mappers.elements("mapper");
        for (Element element : list) {
            String resource = element.attributeValue("resource");
            // 根据xml的路径，获取对应的输入流
            InputStream inputStream = getResourceAsStream(resource);
            // 将流对象，转换成Document对象
            Document document = createDocument(inputStream);
            // 针对Document对象，按照Mybatis的语义去解析Document
            parseMapper(document.getRootElement());
        }
    }

    private void parseMapper(Element rootElement) {
        namespace = rootElement.attributeValue("namespace");
        // TODO 获取动态SQL标签，比如<sql>
        // TODO 获取其他标签
        List<Element> selectElements = rootElement.elements("select");
        for (Element selectElement : selectElements) {
            parseStatementElement(selectElement);
        }
    }

    private void parseStatementElement(Element selectElement) {
        String statementId = selectElement.attributeValue("id");

        if (statementId == null || statementId.equals("")) {
            return;
        }
        // 一个CURD标签对应一个MappedStatement对象
        // 一个MappedStatement对象由一个statementId来标识，所以保证唯一性
        // statementId = namespace + "." + CRUD标签的id属性
        statementId = namespace + "." + statementId;

        // 注意：parameterType参数可以不设置也可以不解析
        String parameterType = selectElement.attributeValue("parameterType");
        Class<?> parameterClass = resolveType(parameterType);

        String resultType = selectElement.attributeValue("resultType");
        Class<?> resultClass = resolveType(resultType);

        String statementType = selectElement.attributeValue("statementType");
        statementType = statementType == null || statementType == "" ? "prepared" : statementType;

        // SqlSource的封装过程
        SqlSource sqlSource = createSqlSource(selectElement);

        // TODO 建议使用构建者模式去优化
        MappedStatement mappedStatement = new MappedStatement(statementId, parameterClass, resultClass, statementType,
                sqlSource);
        configuration.addMappedStatement(statementId, mappedStatement);
    }

    private SqlSource createSqlSource(Element selectElement) {
        SqlSource sqlSource = parseScriptNode(selectElement);
        return sqlSource;
    }

    private SqlSource parseScriptNode(Element selectElement) {
        // 解析所有的SqlNode
        MixedSqlNode mixedSqlNode = parseDynamicTags(selectElement);
        // 将所有的SqlNode封装到SqlSource中（思考：面向对象的封装）
        SqlSource sqlSource = null;
        // 如果SQL信息中包含动态标签或者${}，那么DynamicSqlSource
        if (isDynamic){
            sqlSource = new DynamicSqlSource(mixedSqlNode);
        }else{
            sqlSource = new RawSqlSource(mixedSqlNode);
        }
        return sqlSource;
    }

    private MixedSqlNode parseDynamicTags(Element selectElement) {
        List<SqlNode> sqlNodes = new ArrayList<>();

        int nodeCount = selectElement.nodeCount();
        for (int i = 0; i < nodeCount; i++) {
            Node node = selectElement.node(i);
            if (node instanceof Text){
                String text = node.getText().trim();
                if (text == null || "".equals(text)){
                    continue;
                }
               /* if (text.indexOf("{$")>-1){
                    SqlNode textSqlNode = new TextSqlNode(text);
                }else{
                    SqlNode textSqlNode = new StaticTextSqlNode(text);
                }*/
                TextSqlNode sqlNode = new TextSqlNode(text);
                if (sqlNode.isDynamic()){
                    isDynamic = true;
                    sqlNodes.add(sqlNode);
                }else {
                    sqlNodes.add(new StaticTextSqlNode(text));
                }

            }else if (node instanceof Element){
                isDynamic = true;
                Element element = (Element) node;
                String elementName = element.getName();
                // TODO 可以使用策略模式优化
                if ("if".equals(elementName)){
                    String test = element.attributeValue("test");
                    MixedSqlNode mixedSqlNode = parseDynamicTags(element);

                    IfSqlNode ifSqlNode = new IfSqlNode(test,mixedSqlNode);
                    sqlNodes.add(ifSqlNode);
                }else if ("where".equals(elementName)){
                    // TODO
                }
            }
        }
        return new MixedSqlNode(sqlNodes);
    }

    private Class<?> resolveType(String parameterType) {
        try {
            Class<?> clazz = Class.forName(parameterType);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解析<environments></>
     * @param environments
     */
    private void parseEnvironments(Element environments) {
        String aDefault = environments.attributeValue("default");
        List<Element> list = environments.elements("environment");
        for (Element element : list) {
            String id = element.attributeValue("id");
            if (id.equals(aDefault)){
                Element dataSource = element.element("dataSource");
                parseDataSource(dataSource);
            }
        }
    }

    /**
     * <datasource></datasource>
     * @param dataSource
     */
    private void parseDataSource(Element dataSource) {
        String type = dataSource.attributeValue("type");
        if ("DBCP".equals(type)){
            BasicDataSource ds = new BasicDataSource();
            Properties properties = parseProperty(dataSource);
            ds.setDriverClassName(properties.getProperty("driver"));
            ds.setUrl(properties.getProperty("url"));
            ds.setUsername(properties.getProperty("username"));
            ds.setPassword(properties.getProperty("password"));
            configuration.setDataSource(ds);
        }
    }

    private Properties parseProperty(Element dataSource) {
        Properties properties = new Properties();

        List<Element> list = dataSource.elements("property");
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.put(name,value);
        }
        return properties;
    }

    private Document createDocument(InputStream inputStream) {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            return document;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getResourceAsStream(String s) {
        return this.getClass().getClassLoader().getResourceAsStream(s);
    }

    private <T> List<T> selectList(String statementId,Object param) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        List<T> results = new ArrayList<>();
        try {
            MappedStatement mappedStatement = configuration.getMappedStatementById(statementId);
            // 获取连接
            connection = getConnection();

            // 定义sql语句 ?表示占位符
            // SqlSource和SqlNode的执行过程
            SqlSource sqlSource = mappedStatement.getSqlSource();
            // 这一步骤就是完成SqlSource和SqlNode的解析操作
            BoundSql boundSql = sqlSource.getBoundSql(param);
            // 获取可以直接执行的SQL语句
            String sql = boundSql.getSql();

            String statementType = mappedStatement.getStatementType();
            if ("prepared".equals(statementType)){
                // 获取预处理 statement
                preparedStatement = connection.prepareStatement(sql);
                // 设置参数，第一个参数为 sql 语句中参数的序号（从 1 开始），第二个参数为设置的
                // TODO
                setParameters(preparedStatement,param,boundSql);


                // 向数据库发出 sql 执行查询，查询出结果集
                rs = preparedStatement.executeQuery();

                // TODO
                handleResultSet(rs,results,mappedStatement);
            }else if ("callable".equals(statementType)){
                // TODO 存储过程
            }else{
                // TODO
            }

            return results;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }

        return null;
    }

    private <T> void handleResultSet(ResultSet rs, List<T> results, MappedStatement mappedStatement) throws  Exception{
        // 遍历查询结果集
        Class<?> resultTypeClass = mappedStatement.getResultTypeClass();

        Object result = null;
        while (rs.next()) {
            // 反射创建映射结果对象
            result = resultTypeClass.newInstance();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            // result实例中要设置的每一个属性的值，都是来自于resultSet结果集中某一行的每一列的值
            for (int i = 1; i <= columnCount ; i++) {
                // 获取的结果集中列的名称
                String columnName = metaData.getColumnName(i);
                // 列的名称和属性的名称必须一致
                Field field = resultTypeClass.getDeclaredField(columnName);
                field.setAccessible(true);
                // 通过反射给属性赋值
                field.set(result,rs.getObject(i));
            }

            results.add((T) result);
        }
    }

    /**
     *
     * @param preparedStatement
     * @param param
     * @param boundSql 封装了解析后的SQL语句和对应的参数列表信息
     * @throws Exception
     */
    private void setParameters(PreparedStatement preparedStatement, Object param, BoundSql boundSql) throws  Exception{
        if (SimpleTypeRegistry.isSimpleType(param.getClass())) {
            preparedStatement.setObject(1, param);
        } else if (param instanceof Map) {
            Map map = (Map) param;

            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0 ; i <parameterMappings.size();i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                // #{}中参数名称
                String name = parameterMapping.getName();
                Object value = map.get(name);
                preparedStatement.setObject(i + 1, value);
            }

        } else {
            // TODO 暂时不处理
        }
    }

    private Connection getConnection() {
        try {
            DataSource dataSource = configuration.getDataSource();
            Connection connection = dataSource.getConnection();
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}