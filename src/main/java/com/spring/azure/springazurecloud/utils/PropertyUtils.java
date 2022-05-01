package com.spring.azure.springazurecloud.utils;

import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

public class PropertyUtils {
    private static PropertyUtils propertyUtils;
    public Properties properties = new Properties();

    public PropertyUtils() {
        ClassPathResource classPathResource = new ClassPathResource("/properties/dev.properties");
        try {
            properties.load(classPathResource.getInputStream());
            LogHelper.logInfo("In Properties File");
        }catch (Exception e){
            LogHelper.logError("Error Reading Properties File : ", e);
        }
    }

    public static PropertyUtils getInstance(){
        if(propertyUtils == null) propertyUtils = new PropertyUtils();
        return propertyUtils;
    }

}
