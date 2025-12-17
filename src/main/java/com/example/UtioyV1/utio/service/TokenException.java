package com.example.UtioyV1.utio.service;

/**
 * @Description : 如果权限异常运行的时候需要抛出异常选择这个
 * @Param :
 * @Return :
 * @Author : l-jiahui
 * @Date : 2020-10-11
 */
public class TokenException extends RuntimeException  {

    public TokenException(String message) {
        super(message);
    }

}