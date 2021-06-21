package com.kkb.spring.framework.resource.support;

import com.kkb.spring.framework.resource.Resource;

import java.io.InputStream;

/**
 * 存储了classpath路径下的资源信息
 */
public class ClasspathResource implements Resource {
    private String resource;

    public ClasspathResource(String resource) {
        this.resource = resource;
    }

    @Override
    public InputStream getResource() {
        return this.getClass().getClassLoader().getResourceAsStream(resource);
    }
}
