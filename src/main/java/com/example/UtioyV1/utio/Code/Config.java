package com.example.UtioyV1.utio.Code;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
@ConfigurationProperties(prefix = "config")
@Data  // 添加 Lombok
public class Config {



    // static 字段（给其他地方直接用）

    public static String CLAIMS_JWS_ATTRIBUTE = "CLAIMS_JWS_ATTRIBUTE";
    public static String currentPath = System.getProperty("user.dir") + "/file"; //相对路径


    public static String FILE_URL = "https://smartfarmservice.00000.work";
    public  String FILE_URL1 = "https://smartfarmservice.00000.work";



    //    数据库配置
    public static Map<String,String> DAO_VALUE = new HashMap<>(50);
    public static Boolean IS_DAO =false; //判断数据库是否已经加载完成



    //    通过key拿到value
    public static String  getDaoValue(String key){
        return DAO_VALUE.get(key);
    }
}