package com.example.spring_utioy_v1.utio.service;

/**
 * @Description : 弹窗如果不需要执行弹窗的界面，需要前端自行处理
 * @Param :
 * @Return :
 * @Author : l-jiahui
 * @Date : 2020-10-11
 */
public class Return_NoShowException extends RuntimeException  {

    public Return_NoShowException(String message) {
        super(message);
    }

}
