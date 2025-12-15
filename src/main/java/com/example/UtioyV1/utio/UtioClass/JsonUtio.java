package com.example.UtioyV1.utio.UtioClass;

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.example.UtioyV1.utio.UtioY;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;

//JSON格式的操作指南
public class JsonUtio {
    public static String JSON(Object obj) { //传输json格式
        String json = com.alibaba.fastjson2.JSON.toJSONString(obj); //序列化
        return json;
    }

    public static <T> T JSON_COPY(T data) { //深拷贝不支持集合
        if (data == null) {
            return null;
        }
        try {
            // 1. 将对象序列化为JSON字符串
            String json = JsonUtio.JSON(data);

            // 2. 获取原对象的实际类型Class
            Class<T> clazz = (Class<T>) data.getClass();


            // 2. 处理集合类型（通过TypeReference保留泛型）
//            TypeReference<T> typeReference = new TypeReference<T>() {};
            if (List.class.isAssignableFrom(clazz)) {
                System.out.println("是List集合，具体实现类：" + clazz.getName()); // 如 "java.util.ArrayList"

            }


            // 3. 将JSON反序列化为原对象类型的新实例（深拷贝）
//            return com.alibaba.fastjson.JSON.parseObject(json, String.class);
            return JsonUtio.JSON_ObjectType(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//   深拷贝集合版
    public static <T> List<T> JSON_COPY(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();


        String json = UtioY.JSON(src);
        List<T> list = UtioY.JSON_ObjectType(json, List.class);
        System.out.println(list);
        return list;
    }


    public static <T> T JSON_ObjectType(String json,Class<T> valueType){
        try{
            //如果遇到没有不填充值  和@JsonIgnoreProperties(ignoreUnknown = true) 一样
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, valueType);

//            ObjectMapper objectMapper = new ObjectMapper();
//            return  objectMapper.readValue(json, valueType);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }

    }



    public static String JSONNoDate(Object obj) { //传输json格式 并且不带时间的格式
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("date_day"); // 过滤参数
        filter.getExcludes().add("sum"); // 过滤参数

        String json = com.alibaba.fastjson2.JSON.toJSONString(obj,filter); //序列化
        return json;
    }
}