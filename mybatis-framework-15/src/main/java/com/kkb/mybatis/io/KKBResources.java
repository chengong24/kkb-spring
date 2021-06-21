package com.kkb.mybatis.io;

import java.io.InputStream;

public class KKBResources {
    public static InputStream getResourceAsStream(String resource){
        return KKBResources.class.getClassLoader().getResourceAsStream(resource);
    }
}
