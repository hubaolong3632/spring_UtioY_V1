package com.example.UtioyV1.utio.model;

import lombok.Data;

import java.util.Date;

@Data
/**JWT的各种参数的对象*/
public class JWTDatasModel {

    /**标识用户的唯一标识id*/
   private  String subject;

    /**获取过期时间*/
   private  Date expiration;

    /**获取颁布者*/
   private  String issuer;

    /**获取jwt参数里面放入的值*/
   private JWTModel jwtmodel;


}
