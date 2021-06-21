package com.kkb.mybatis.test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kkb.mybatis.framework.utils.SimpleTypeRegistry;
import org.junit.Test;

import com.kkb.mybatis.po.User;

/**
 * 解决硬编码问题（properties文件）
 *
 * @author 灭霸詹
 */
public class MybatisV1 {

    /**
     * 存储jdbc.properties文件中的内容
     */
    private Properties properties = new Properties();

    @Test
    public void test() {
        // 加载properties文件
        loadProperties("jdbc.properties");
        // 执行用户查询操作
        // 根据用户名称查询列表信息
//		List<User> users = selectUserList("queryUserById","王五");
        // 根据用户名称+性别查询列表信息（只能传递一个参数对象）
        Map<String, Object> params = new HashMap<>();
        params.put("username", "王五");
        params.put("sex", "男");
//		List<User> users = selectUserList("queryUserById",params);
        List<User> users = selectList("queryUserById", params);
        // 根据用户ID查询信息
//		List<User> users = selectUserList("queryUserById",1);
        System.out.println(users);
    }

    private void loadProperties(String path) {
        InputStream inputStream;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //TODO 流的释放
            // 西北玄天一朵云,乌鸦落在了凤凰群
        }
    }

    private <T> List<T> selectList(String statementId, Object param) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        List<T> results = new ArrayList<>();
        try {
            // 加载数据库驱动
            Class.forName(properties.getProperty("db.driver"));

            // 通过驱动管理类获取数据库链接connection = DriverManager
            connection = DriverManager.getConnection(properties.getProperty("db.url")
                    , properties.getProperty("db.username")
                    , properties.getProperty("db.password"));

            // 定义sql语句 ?表示占位符
            String sql = properties.getProperty("db.sql." + statementId);

            // 获取预处理 statement
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数，第一个参数为 sql 语句中参数的序号（从 1 开始），第二个参数为设置的
            // preparedStatement.setString(1, username);
            // preparedStatement.setObject(1, param);

            if (SimpleTypeRegistry.isSimpleType(param.getClass())) {
                preparedStatement.setObject(1, param);
            } else if (param instanceof Map) {
                Map map = (Map) param;

                String paramnames = properties.getProperty("db.sql." + statementId + ".paramnames");
                String[] names = paramnames.split(",");
                for (int i = 0; i < names.length; i++) {
                    Object value = map.get(names[i]);
                    preparedStatement.setObject(i + 1, value);
                }

            } else {
                // TODO 暂时不处理
            }

            // 向数据库发出 sql 执行查询，查询出结果集
            rs = preparedStatement.executeQuery();

            // 遍历查询结果集
            String resultclassname = properties.getProperty("db.sql." + statementId + ".resultclassname");
            Class<?> returnTypeClass = Class.forName(resultclassname);

            Object result = null;
            while (rs.next()) {
                // 反射创建映射结果对象
                result = returnTypeClass.newInstance();

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                // result实例中要设置的每一个属性的值，都是来自于resultSet结果集中某一行的每一列的值
                for (int i = 1; i <= columnCount; i++) {
                    // 获取的结果集中列的名称
                    String columnName = metaData.getColumnName(i);
                    // 列的名称和属性的名称必须一致
                    Field field = returnTypeClass.getDeclaredField(columnName);
                    field.setAccessible(true);
                    // 通过反射给属性赋值
                    field.set(result, rs.getObject(i));
                }

                results.add((T) result);
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

/*	private List<User> selectUserList(String statementId,Object param) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		List<User> users = new ArrayList<>();
		try {
			// 加载数据库驱动
			Class.forName(properties.getProperty("db.driver"));

			// 通过驱动管理类获取数据库链接connection = DriverManager
			connection = DriverManager.getConnection(properties.getProperty("db.url")
					,properties.getProperty("db.username")
					,properties.getProperty("db.password"));

			// 定义sql语句 ?表示占位符
			String sql = properties.getProperty("db.sql."+statementId);

			// 获取预处理 statement
			preparedStatement = connection.prepareStatement(sql);

			// 设置参数，第一个参数为 sql 语句中参数的序号（从 1 开始），第二个参数为设置的
//			preparedStatement.setString(1, username);
//			preparedStatement.setObject(1, param);

			if (SimpleTypeRegistry.isSimpleType(param.getClass())){
				preparedStatement.setObject(1, param);
			}else if (param instanceof  Map){
				Map map = (Map) param;

				String paramnames = properties.getProperty("db.sql." + statementId + ".paramnames");
				String[] names = paramnames.split(",");
				for (int i = 0; i <names.length ; i++) {
					Object value = map.get(names[i]);
					preparedStatement.setObject(i+1, value);
				}

			}else{
				// TODO 暂时不处理
			}

			// 向数据库发出 sql 执行查询，查询出结果集
			rs = preparedStatement.executeQuery();

			// 遍历查询结果集
			User user = null;
			while (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));

				users.add(user);
			}

			return users;
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
	}*/
}
