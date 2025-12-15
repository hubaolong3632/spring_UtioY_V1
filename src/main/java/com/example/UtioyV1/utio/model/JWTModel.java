package com.example.UtioyV1.utio.model;


//import lombok.Data;

//import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
/**用来放入jwt里面的参数  可以进行自定义*/
public class JWTModel {


    private Integer id; //用户数据库id
    private String username; //用户账号
    private String name; //用户姓名
    private String openid; //用户唯一id
    private String jurisdiction; //权限
    private List<String> jurisdiction_list=new ArrayList<>(); //权限列表
    private Long past_time; //过期时间


    public JWTModel() {
    }

    public JWTModel(Integer id, String name, String jurisdiction) {
        this.id = id;
        this.name = name;
        this.jurisdiction = jurisdiction;
    }


    /**
     * 校验是否拥有任意一个指定权限
     * @param permissions 需要校验的权限列表
     * @return true-拥有其中任意一个权限, false-一个都没有
     */
    public boolean is(String... permissions) {
        if (permissions == null || jurisdiction_list == null) {
            return false;
        }
        for (String permission : permissions) {
            if (jurisdiction_list.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验是否拥有所有指定权限
     * @param permissions 需要校验的权限列表
     * @return true-拥有所有权限, false-缺少至少一个权限
     */
    public boolean is_all(String... permissions) {
        if (permissions == null || jurisdiction_list == null) {
            return false;
        }
        for (String permission : permissions) {
            if (!jurisdiction_list.contains(permission)) {
                return false;
            }
        }
        return true;
    }
}
