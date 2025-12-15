package com.example.UtioyV1.utio.model;


import com.example.UtioyV1.utio.UtioY;
import lombok.Data;

import java.util.Map;
@Data
//@Accessors(chain = true)
public class ReturnModel {
  private   Map<String,Object> data;

    public ReturnModel(Map<String, Object> data) {
        this.data = data;
    }

    public ReturnModel add(String key,Object value){
        data.put(key,value);
        return this;
    }



    /*
    获取存入的数据
     */
    public Map<String,Object> getData(){
        return data;
    }


    @Override
    public String toString() {
        return UtioY.JSON(data);
    }
}
