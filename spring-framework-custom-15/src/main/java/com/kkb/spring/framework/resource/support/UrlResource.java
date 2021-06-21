package com.kkb.spring.framework.resource.support;

import com.kkb.spring.framework.resource.Resource;

import java.io.InputStream;

/**
 * 存储了网络URL路径下的资源信息
 */
public class UrlResource implements Resource {
    @Override
    public InputStream getResource() {
        return null;
    }
}
