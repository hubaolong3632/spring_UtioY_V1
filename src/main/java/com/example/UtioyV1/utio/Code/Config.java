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

    //是否开启微服务auth2.0 此方法不能从数据库中拿取
    public static Boolean MicroService =true;

//    远程微服务oauth2地址(但是这个应该是注册中心的地址)
    public static String service_url ="http://127.0.0.1:1111";



    // static 字段（给其他地方直接用）

    public static String CLAIMS_JWS_ATTRIBUTE = "CLAIMS_JWS_ATTRIBUTE";
    public static String currentPath = System.getProperty("user.dir") + "/file"; //相对路径


    public static String FILE_URL = "https://smartfarmservice.00000.work";
    public  String FILE_URL1 = "https://smartfarmservice.00000.work";



    //    数据库配置
    public static Map<String,String> DAO_VALUE = new HashMap<>(50);
    public static Boolean IS_DAO =false; //判断数据库是否已经加载完成




//    public static Double d1;
//    public static Integer t1;
//    public static int  ll;


    //    通过key拿到value
    public static String  getDaoValue(String key){
        return DAO_VALUE.get(key);
    }
}