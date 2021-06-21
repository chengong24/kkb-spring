package com.kkb.mybatis.builder;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.io.KKBResources;
import com.kkb.mybatis.utils.KKBDocumentUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 专门解析全局配置文件的类
 */
public class KKBXMLConfigBuilder {
    private KKBConfiguration configuration;

    public KKBXMLConfigBuilder() {
        this.configuration = new KKBConfiguration();
    }

    public KKBConfiguration parse(InputStream inputStream) {

        // 将流对象，转换成Document对象
        Document document = KKBDocumentUtils.createDocument(inputStream);
        // 针对Document对象，按照Mybatis的语义去解析Document
        parseConfiguration(document.getRootElement());

        return configuration;
    }

    private void parseConfiguration(Element rootElement) {

        Element environments = rootElement.element("environments");
        parseEnvironments(environments);
        Element mappers = rootElement.element("mappers");
        parseMappers(mappers);

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



    /**
     * 解析<mappers></>
     * @param mappers
     */
    private void parseMappers(Element mappers) {
        List<Element> list = mappers.elements("mapper");
        for (Element element : list) {
            String resource = element.attributeValue("resource");
            // 根据xml的路径，获取对应的输入流
            InputStream inputStream = KKBResources.getResourceAsStream(resource);
            // 将流对象，转换成Document对象
            Document document = KKBDocumentUtils.createDocument(inputStream);
            // 针对Document对象，按照Mybatis的语义去解析Document
            KKBXMLMapperBuilder mapperBuilder = new KKBXMLMapperBuilder(configuration);
            mapperBuilder.parse(document.getRootElement());
        }
    }
}
