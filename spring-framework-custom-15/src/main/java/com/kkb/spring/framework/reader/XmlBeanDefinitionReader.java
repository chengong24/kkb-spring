package com.kkb.spring.framework.reader;

import com.kkb.spring.framework.registry.BeanDefinitionRegistry;
import com.kkb.spring.framework.utils.DocumentUtils;
import org.dom4j.Document;

import java.io.InputStream;

/**
 * 专门用来解析xml中的BeanDefinition信息
 */
public class XmlBeanDefinitionReader {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void registerBeanDefinitions(InputStream inputStream){
        Document document = DocumentUtils.createDocument(inputStream);

        XmlBeanDefinitionDocumentReader documentReader = new XmlBeanDefinitionDocumentReader(beanDefinitionRegistry);
        documentReader.loadBeanDefinitions(document.getRootElement());
    }
}
