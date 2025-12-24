package com.example.UtioyV1.utio.controller;

import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.UtioY;
import com.example.UtioyV1.utio.model.JWTModel;
import com.example.UtioyV1.utio.model.UserMsgModel;
import com.example.UtioyV1.utio.model.UserRole;
import com.example.UtioyV1.utio.service.ReturnException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/api/auth")
public class UserController  extends UserMsgModel {
    @RequestMapping("/login")
    public Result text1(boolean b1){

        String s = UtioY.JWT_Create("jwt", new JWTModel("1233", "zs", "22"));

        return Result.success(s);
    }


    @RequestMapping("/text1")
    @UserRole({"material:delete","material:delete"}) //用户需要的权限
    public Result text11(Integer user_id,boolean b1){

        Log.error("瞅瞅"+user_id);

        Log.info("用户id"+user_id);
        System.out.println("执行成功");
        if(b1==true){
            throw new ReturnException("错误测试");
        }
        return Result.success("成功");
    }


    /**
     *  查询用户有哪些权限
     * @param user_role
     * @return
     */
    @RequestMapping("/get_role")
    @UserRole( {"cc","aa","bb"})
    public Result role(String user_role){


        List<String> strings = Role.role_list.get(user_role);

        if(strings==null||strings.isEmpty()){
            return Result.failure("没有该用户:"+user_role);
        }

        return Result.success(strings);
    }


}