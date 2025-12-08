package com.example.spring_utioy_v1.utio.UtioClass;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.spring_utioy_v1.utio.UtioY;
import com.example.spring_utioy_v1.utio.model.ApiDataModel;
import com.example.spring_utioy_v1.utio.model.ApiHreadModel;
import com.example.spring_utioy_v1.utio.service.ReturnException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * 请求处理
 */
public class ApiUtio {

    /**
     * get请求区
     */
    public static JSONObject API_get(String url, ApiDataModel date, ApiHreadModel header){return JSON.parseObject(ApiUtio.apiGetString(url, date, header));}

    public static JSONObject API_get(String url, ApiDataModel date){
        return JSON.parseObject(ApiUtio.apiGetString(url, date, null));
    }

    public static JSONObject API_get(String url){
        return JSON.parseObject(ApiUtio.apiGetString(url, null, null));
    }

    public static <T> T API_get(String url, ApiDataModel date, ApiHreadModel header, Class<T> valueType){
        return UtioY.JSON_ObjectType(ApiUtio.apiGetString(url, date, header), valueType);
    }

    public static <T> T API_get(String url, ApiDataModel date, Class<T> valueType){
        return UtioY.JSON_ObjectType(ApiUtio.apiGetString(url, date, null), valueType);
    }

    public static <T> T API_get(String url,Class<T> valueType){
        return UtioY.JSON_ObjectType(ApiUtio.apiGetString(url, null, null), valueType);
    }


    /**
     *  post请求区
     */
    public static <T> T  API_post(String url,Class<T> valueType){
        return UtioY.JSON_ObjectType(ApiUtio.apiPostString(url, null, null), valueType);
    }
    public static JSONObject API_post(String url, Object date, ApiHreadModel header){
        return JSON.parseObject(ApiUtio.apiPostString(url, date, header));
    }
    public static JSONObject API_post(String url, Object date){
        return JSON.parseObject(ApiUtio.apiPostString(url, date, null));
    }
    public static JSONObject API_post(String url){
        return JSON.parseObject(ApiUtio.apiPostString(url, null, null));
    }
    public static <T> T  API_post(String url, Object date, ApiHreadModel header, Class<T> valueType){
        return UtioY.JSON_ObjectType(ApiUtio.apiPostString(url, date, header), valueType);
    }
    public static <T> T  API_post(String url, Object date,Class<T> valueType){
        return UtioY.JSON_ObjectType(ApiUtio.apiPostString(url, date, null), valueType);
    }




    public static String  apiGetString(String url, ApiDataModel date, ApiHreadModel header){
        Connection connection = apiConfig(url, header);
        if(date!=null){
            Map<String, String> data_map = date.getData();
            for (String key : data_map.keySet()){
                    connection.data(key,data_map.get(key));
                 }
             }

        try {
            Document document = connection.get();
            String text = document.body().text();
            JSONPrintf(text);

            return text;  //返回最终结果

        } catch (IOException e) {
            throw new ReturnException(e.getMessage());
        }
    }


    public static String  apiPostString(String url, Object object, ApiHreadModel header){
        Connection connection = apiConfig(url, header);
        String bodyJson = UtioY.JSON(object); //获取对象转换的json

        if(object!=null){
            connection.requestBody(bodyJson);
        }
        try {
            Document document = connection.post();
            String text = document.body().text();
            JSONPrintf(text);
            return text;  //返回最终结果

        } catch (IOException e) {
            throw new ReturnException(e.getMessage());
        }
    }


    /*
        所有请求的默认配置
     */
    private static Connection apiConfig(String url, ApiHreadModel header){

        Connection connection = Jsoup.connect(url)
                .header("Content-Type", "application/json")
                .header("Connection", "keep-alive")
                .ignoreContentType(true) //忽略所有请求参数
                .ignoreHttpErrors(true);   //忽略所有异常

//        请求头配置
        if(header!=null){
//            UUID.randomUUID()
            Map<String, String> data_map = header.getData();
            for (String key : data_map.keySet()){
                connection.header(key,data_map.get(key));
            }
        }

        return connection;
    }

    /**
     * 输出结果
     */
    private static void JSONPrintf(String json){
        System.out.println("json输出:"+json);
//        JSONObject jsonObject = JSON.parseObject(json);
//        Integer errcode = jsonObject.getInteger("errcode");

//        if(errcode!=0){
//            if(errcode==40029){
//                 throw new ReturnException("code已使用过");
//            }
//
////            其他错误
//            throw new ReturnException(jsonObject.getString("errmsg"));
//        }


    }

}
