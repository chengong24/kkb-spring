package com.kkb.spring.framework.resolver;

import com.kkb.spring.framework.config.BeanDefinition;
import com.kkb.spring.framework.config.RuntimeBeanReference;
import com.kkb.spring.framework.config.TypedStringValue;
import com.kkb.spring.framework.factory.BeanFactory;

public class BeanDefinitionValueResolver {
    private BeanFactory beanFactory;

    public BeanDefinitionValueResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValue(Object value, BeanDefinition bd) {
        if (value instanceof RuntimeBeanReference){
            RuntimeBeanReference beanReference = (RuntimeBeanReference) value;
            String ref = beanReference.getRef();
            return beanFactory.getBean(ref);
        }else if (value instanceof TypedStringValue){
            TypedStringValue typedStringValue = (TypedStringValue) value;
            String stringValue = typedStringValue.getValue();
            Class<?> targetType = typedStringValue.getTargetType();
            if (targetType == Integer.class){
                return Integer.parseInt(stringValue);
            }else if (targetType == String.class){
                return stringValue;
            }
        }
        return null;
    }
}
