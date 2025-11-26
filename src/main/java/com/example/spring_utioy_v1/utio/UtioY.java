package com.example.spring_utioy_v1.utio;


import com.example.spring_utioy_v1.utio.UtioClass.ApiUtio;
import com.example.spring_utioy_v1.utio.UtioClass.DateUtio;
import com.example.spring_utioy_v1.utio.UtioClass.JsonUtio;
import com.example.spring_utioy_v1.utio.UtioClass.JwtUtio;
import com.example.spring_utioy_v1.utio.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

//所有的方便的代码
public class UtioY {

    /**JSON转成一个类 (传入 json格式 和需要转换的类型)*/
    public static <T> T JSON_ObjectType(String json,Class<T> valueType){
        return  JsonUtio.JSON_ObjectType(json,valueType);
    }


    /**转换成Json格式*/
    public static  String JSON(Object obj){
      return   JsonUtio.JSON(obj);
    }

    /**
     * 深拷贝
     */
    public static <T> T JSON_COPY(T data){
      return   JsonUtio.JSON_COPY(data);
    }

    /**
     * 深拷贝(对集合进行拷贝)
     */
    public static <T> List<T> JSON_COPY(List<T> src) throws IOException, ClassNotFoundException{
      return   JsonUtio.JSON_COPY(src);
    }


    /**
     * 更具提供的头和请求内容进行生成jwt
     */
    public static String JWT_Create(String subject, JWTModel jwtmodel){return JwtUtio.JWTCreate(subject,jwtmodel);}




    /**
     * 更具旧的JWT去生成新的JWT 时间默认加一周
     */
    public static String JWT_Create(String jwt){
        return JwtUtio.JWTCreate(jwt);
    }

    /**
     * 解析jwt
     * */
    public static JWTDatasModel JWT_PAnalysis(String jwt) {
        return JwtUtio.JWTAnalysis(jwt);
    }



    /**获取当前系统时间 格式为 yyyy-MM-dd HH:mm:ss*/
    public static String Date_getDate(){
       return DateUtio.dateDay_String();
    }

    /**
     * 用于表示不带时区的日期时间，例如 2022-05-20T15:30:00。
     * */
    public static LocalDateTime Date_LocalDateTime(){
        return DateUtio.dateDay_Date();
    }


    /**
     * 获取当前时间戳
     * */
    public static Long Date_TimeCurrent(){
        return    System.currentTimeMillis();
    }


    /**
     * 获取增加指定时长后的时间 返回Date格式
     */
    public static Date Date_AddTime(Long day) {
        return DateUtio.dateDay_Date(day);
    }




    /**生成20位字符类型随机数*/
    public static String Random_string20(){
        //生成随机数 20位
        return  UUID.randomUUID().toString().replaceAll("-", "").substring(0,20);
    }



    /**生成数字类型随机数  16位 */
    public static String Random_number16(){
        Random random = new Random();
        long time = System.currentTimeMillis();
        int fourDigitNumber = random.nextInt(9000) + 1000;

       String s1= time+""+ fourDigitNumber;

        return s1;
    }

    /**
     *返回参数
     * */
    public static ReturnModel Ret(){
        return new ReturnModel(new HashMap<>());
    }

    /**
     *Api的请求头简写
     * */
    public static ApiHreadModel API_hread(){
        return new ApiHreadModel();
    }
    /**
     *Api请求体简写
     * */
    public static ApiDataModel API_date(){
        return new ApiDataModel();
    }


    /**
     * jsoup的get和post
     * */
   private static final ApiUtio api= new ApiUtio();
    public static ApiUtio API_GPST(){
        return api;
    }


    /***/


    /***/


    /***/


    /***/


    /***/


    /***/


    /***/




}
/*
项目需要的依赖

  <!--解决找不到 JWT-->
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.1</version>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.0</version>
        </dependency>


        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>2.0.0</version>
        </dependency>



        <!-- jsoup获取-->
        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.11.2</version>
        </dependency>

 */