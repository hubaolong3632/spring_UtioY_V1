package com.example.spring_utioy_v1.utio.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 用户信息基类 - 其他实体类可继承此类
 */
@Data
@SuperBuilder
public class UserMsgModel {
    protected Integer id;
    protected String name;
    protected Integer password;
    protected Integer haha;

    public UserMsgModel() {}

    public void log() {
        System.out.println("id: " + id + " name: " + name + " password: " + password + " haha: " + haha);
    }
}
