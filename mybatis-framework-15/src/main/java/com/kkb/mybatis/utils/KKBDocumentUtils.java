package com.kkb.mybatis.utils;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class KKBDocumentUtils {
    public static Document createDocument(InputStream inputStream){
        try{
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            return  document;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
