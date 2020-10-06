package com.scaler7;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 此类用于直接从springIOC容器中获取需要的类
 */
@Component
@Lazy(false)
public class SpringContextUtil implements ApplicationContextAware {

    public static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz){
        return  applicationContext.getBean(clazz);
    }

    public static ApplicationContext getContext(){
        return applicationContext;
    }
}
