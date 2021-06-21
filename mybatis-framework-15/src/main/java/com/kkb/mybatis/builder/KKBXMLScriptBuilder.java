package com.kkb.mybatis.builder;

import com.kkb.mybatis.config.KKBConfiguration;
import com.kkb.mybatis.sqlnode.KKBSqlNode;
import com.kkb.mybatis.sqlnode.support.KKBIfSqlNode;
import com.kkb.mybatis.sqlnode.support.KKBMixedSqlNode;
import com.kkb.mybatis.sqlnode.support.KKBStaticTextSqlNode;
import com.kkb.mybatis.sqlnode.support.KKBTextSqlNode;
import com.kkb.mybatis.sqlsource.KKBSqlSource;
import com.kkb.mybatis.sqlsource.support.KKBDynamicSqlSource;
import com.kkb.mybatis.sqlsource.support.KKBRawSqlSource;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 专门解析sql脚本的类
 */
public class KKBXMLScriptBuilder {
    private boolean isDynamic;
    public KKBSqlSource parseScriptNode(Element selectElement) {
        // 解析所有的SqlNode
        KKBMixedSqlNode mixedSqlNode = parseDynamicTags(selectElement);
        // 将所有的SqlNode封装到SqlSource中（思考：面向对象的封装）
        KKBSqlSource sqlSource = null;
        // 如果SQL信息中包含动态标签或者${}，那么DynamicSqlSource
        if (isDynamic){
            sqlSource = new KKBDynamicSqlSource(mixedSqlNode);
        }else{
            sqlSource = new KKBRawSqlSource(mixedSqlNode);
        }
        return sqlSource;
    }

    private KKBMixedSqlNode parseDynamicTags(Element selectElement) {
        List<KKBSqlNode> sqlNodes = new ArrayList<>();

        int nodeCount = selectElement.nodeCount();
        for (int i = 0; i < nodeCount; i++) {
            Node node = selectElement.node(i);
            if (node instanceof Text){
                String text = node.getText().trim();
                if (text == null || "".equals(text)){
                    continue;
                }

                KKBTextSqlNode sqlNode = new KKBTextSqlNode(text);
                if (sqlNode.isDynamic()){
                    isDynamic = true;
                    sqlNodes.add(sqlNode);
                }else {
                    sqlNodes.add(new KKBStaticTextSqlNode(text));
                }

            }else if (node instanceof Element){
                isDynamic = true;
                Element element = (Element) node;
                String elementName = element.getName();
                // TODO 可以使用策略模式优化
                if ("if".equals(elementName)){
                    String test = element.attributeValue("test");
                    KKBMixedSqlNode mixedSqlNode = parseDynamicTags(element);

                    KKBIfSqlNode ifSqlNode = new KKBIfSqlNode(test,mixedSqlNode);
                    sqlNodes.add(ifSqlNode);
                }else if ("where".equals(elementName)){
                    // TODO
                }
            }
        }
        return new KKBMixedSqlNode(sqlNodes);
    }
}
