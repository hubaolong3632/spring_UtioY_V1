package com.example.UtioyV1.utio.model;


import com.example.UtioyV1.utio.UtioY;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数
 */
@Data
public class ApiDataModel {
  private   Map<String,String> data;

    public ApiDataModel() {
        this.data = new HashMap<>();
    }

    public ApiDataModel data(String key, String value) {
        data.put(key, value);
        return this; // 返回当前实例，实现链式调用
    }

    /*
    获取存入的数据
     */
    public Map<String,String> getData(){
        return data;
    }


    @Override
    public String toString() {
        return UtioY.JSON(data);
    }
}