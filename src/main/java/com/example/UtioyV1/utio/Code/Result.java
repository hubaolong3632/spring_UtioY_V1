package com.example.UtioyV1.utio.Code;


import com.example.UtioyV1.utio.model.ReturnModel;

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;
//
    /**状态码*/
    private Integer code;

    /**用户信息*/
    private String msg;

    /**返回参数*/
    private Object data;

    public Result() {}

    public Result(Integer code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    public static Result success(Object data,String msg, Integer code) { //成功的状态码
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setMsg(msg);
        result.setCode(code);


        if (data instanceof ReturnModel r1) result.setData(r1.getData());
        else result.setData(data);

        return result;
    }

    public static Result success(Object data) { //成功的状态码
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);

        if (data instanceof ReturnModel r1) result.setData(r1.getData());
        else result.setData(data);
        return result;
    }


    //  执行状态成功或者失败 直接返回
    public static Result utio(Boolean bol,String yes,String no) {
        Result result = new Result();
           if(bol==true){
               result.setResultCode(ResultCode.SUCCESS);
               result.setMsg(yes);
           }else {
               result.setResultCode(ResultCode.SUCCESS_NO);
               result.setMsg(no);
           }
        return result;
    }

    // 带一个参数直接返回成功与否
    public static Result utio(Boolean bol,String msg) {
        Result result = new Result();
        if(bol==true){
            result.setResultCode(ResultCode.SUCCESS);
            result.setMsg(msg+"成功");
        }else {
            result.setResultCode(ResultCode.SUCCESS_NO);
            result.setMsg(msg+"失败");
        }
        return result;
    }





//    不需要弹窗的
    public static Result error_noShow() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS_NO_Show);
        result.setMsg("失败");
        return result;
    }

    //    不需要弹窗的
    public static Result error_noShow(String msg) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS_NO_Show);
        result.setMsg(msg);
        return result;
    }

    public static Result failure() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS_NO);
        return result;
    }

    public static Result failure(String msg, Integer code) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS_NO);
        result.setMsg(msg);
        result.setCode(code);
        return result;
    }


    public static Result failure(String msg) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS_NO);
        result.setMsg(msg);
        return result;
    }

    public static Result failure(ResultCode code,String msg) {
        Result result = new Result();
        result.setResultCode(code);
//        result.setMsg(msg);

        result.setData(msg);

        return result;
    }
    public static Result failure(ResultCode resultCode, Object data, String msg) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        result.setMsg(msg);

        if (data instanceof ReturnModel r1) result.setData(r1.getData());
        else result.setData(data);

        return result;
    }


    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}