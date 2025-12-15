package com.example.UtioyV1.utio.config;


import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Code.ResultCode;
import com.example.UtioyV1.utio.service.ReturnException;
import com.example.UtioyV1.utio.service.Return_NoShowException;
import com.example.UtioyV1.utio.service.TokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * @description: 全局异常捕获
 */
@ControllerAdvice
public class ExceptionConfig {
    /**
     *
     * 其他异常  返回  -1
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e){
        e.printStackTrace();

        System.err.println("全局异常捕获>>>:"+e);
        return Result.failure(ResultCode.EXCEPTI_ON,"异常:"+e.getMessage());
    }


    /**
     *
     * 路径错误  返回  -2
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseBody
    public Result NoResourceFoundExceptionHandler(NoResourceFoundException e){
        e.printStackTrace();

        System.err.println("路径查找失败>>>:"+e);
        return Result.failure(ResultCode.URL_NO,"当前路径不存在请检查url:"+e.getMessage());
    }




    /**
     *
     * 自定义异常 返回  -4
     */
    @ExceptionHandler(value = ReturnException.class)
    @ResponseBody
    public Result RuntimeExceptionHandler(ReturnException e){
        System.err.println("自定义异常返回>:"+e);
        return Result.failure(ResultCode.SUCCESS_NO,"错误:"+e.getMessage());
    }



    /**
     *
     * 自定义异常/并且不需要弹窗 返回  -10
     */
    @ExceptionHandler(value = Return_NoShowException.class)
    @ResponseBody
    public Result RuntimeExceptionHandler(Return_NoShowException e){
        System.err.println("自定义异常返回(非弹窗)>:"+e);
        return Result.failure(ResultCode.SUCCESS_NO_Show,"错误:"+e.getMessage());
    }



    /**
     *
     * Tonken错误 返回  -5
     */
    @ExceptionHandler(value = TokenException.class)
    @ResponseBody
    public Result RuntimeExceptionHandler(TokenException e){
        System.err.println("token错误:"+e);
        return Result.failure(ResultCode.TONKEN_NO,e.getMessage());
    }



}
