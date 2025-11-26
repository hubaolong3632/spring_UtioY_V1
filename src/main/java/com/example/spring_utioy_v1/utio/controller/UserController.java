package com.example.spring_utioy_v1.utio.controller;

import com.example.spring_utioy_v1.utio.Code.Result;
import com.example.spring_utioy_v1.utio.UtioY;
import com.example.spring_utioy_v1.utio.model.JWTModel;
import com.example.spring_utioy_v1.utio.service.ReturnException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @RequestMapping("/login")
    public Result text1(boolean b1){

        String s = UtioY.JWT_Create("jwt", new JWTModel(1, "zs", "22"));

        return Result.success(s);
    }


    @RequestMapping("/text1")
    public Result text11(boolean b1){
        System.out.println("执行成功");
        if(b1==true){
            throw new ReturnException("错误测试");
        }
        return Result.success("成功");
    }

}
