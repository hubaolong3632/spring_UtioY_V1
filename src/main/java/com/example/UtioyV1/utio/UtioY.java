package com.example.UtioyV1.utio;


import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.LogInfo.LogEntryModel;
import com.example.UtioyV1.utio.UtioClass.*;
import com.example.UtioyV1.utio.model.*;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
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


//    /**
//     * 深拷贝(对集合进行拷贝)
//     */
//    public static <T> List<T> JSON_COPY1(List<T> src) throws IOException, ClassNotFoundException{
//        // 提前指定容量，避免addAll时扩容
//        List<T> batch = new ArrayList<>(src);
//        return  batch;
//    }

    /**
     * 更具提供的头和请求内容进行生成jwt
     */
    public static String JWT_Create(String subject, JWTModel jwtmodel){return JwtUtio.JWTCreate(subject,jwtmodel);}

    /**
     * 获取jwt信息
     * */
    public static JWTModel JWT_getUser(ServletRequest request){
        JWTDatasModel jwtBody = (JWTDatasModel) request.getAttribute(Config.CLAIMS_JWS_ATTRIBUTE);
        return jwtBody.getJwtmodel();
    }

    /**
     * 更具旧的JWT去生成新的JWT 时间默认加一周
     */
    public static String JWT_Create(String jwt){
        return JwtUtio.JWTCreate(jwt);
    }

    /**
     * 解析jwt
     * */
    public static JWTDatasModel JWT_getMessage(String jwt) {
        return JwtUtio.JWTAnalysis(jwt);
    }



    /**获取当前系统时间 格式为 yyyy-MM-dd HH:mm:ss*/
    public static String Date_getDateString(){
       return DateUtio.dateDay_String();
    }

//    /**
//     * 用于表示不带时区的日期时间，例如 2022-05-20T15:30:00。
//     * */
//    public static LocalDateTime Date_LocalDateTime(){
//        return DateUtio.dateDay_Date();
//    }


    /**
     * 获取当前时间戳
     * */
    public static Long Date_getTime(){
        return    System.currentTimeMillis();
    }


//    /**
//     * 获取增加指定时长后的时间 返回Date格式
//     */
//    public static Date Date_AddTime(Long day) {
//        return DateUtio.dateDay_Date(day);
//    }




    /**生成20位字符类型随机数*/
    public static String Random_string20(){
        //生成随机数 20位
        return  UUID.randomUUID().toString().replaceAll("-", "").substring(0,20);
    }

    /**生成时间戳+四位随机数 16位 */
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
    public static ApiUtio API(){
        return api;
    }



//    在加一个saveDao，然后他需要也是给内容，类别，等级（log,error，info,severe）
//    然后保存到数据库，数据库字段对应的id,类别，内容，等级 ，创建时间
//    使用mybatis 在utio层里面些他的语句 ，也要给我对应的数据库表

    /**
     * 获取用户真实IP地址（支持代理）
     * @param request HttpServletRequest
     * @return 用户IP地址
     */
    public static String getClient_IP(HttpServletRequest request) {
     return IPUtio.getClientIP(request);
    }



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