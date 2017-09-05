package com.jktd.ipms.custom.initialize;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

@Component
public class DataBase implements InitializingBean{
    private Properties properties;

    public DataBase(){
        properties=new Properties();
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        File cfgFile = ResourceUtils.getFile("classpath:application.properties");
        FileInputStream fileInputStream=new FileInputStream(cfgFile);
        properties.load(fileInputStream);
        properties.setProperty("spring.datasource.schema","");
        properties.setProperty("spring.datasource.data","");
        FileOutputStream fileOutputStream=new FileOutputStream(cfgFile);
        properties.store(fileOutputStream,"change by dreamlyn");
    }
}
