package com.example.spring_utioy_v1.utio.model;


//import lombok.Data;

//import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
/**用来放入jwt里面的参数  可以进行自定义*/
public class JWTModel {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id; //用户数据库id
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username; //用户账号
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name; //用户姓名
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String openid; //用户唯一id
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jurisdiction; //权限



    public JWTModel() {
    }

    /**
     * 提供姓名  用户id  权限
     * @param name
     * @param openid
     * @param jurisdiction
     */
    public JWTModel(String name, String openid, String jurisdiction) {
        this.name = name;
        this.openid = openid;
        this.jurisdiction = jurisdiction;
    }
}
