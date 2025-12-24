package com.example.UtioyV1.utio.controller;

import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.UtioClass.JwtUtio;
import com.example.UtioyV1.utio.UtioY;
import com.example.UtioyV1.utio.model.JWTModel;
import com.example.UtioyV1.utio.model.ReturnModel;
import com.example.UtioyV1.utio.model.UserMsgModel;
import com.example.UtioyV1.utio.model.UserRole;
import com.example.UtioyV1.utio.service.ReturnException;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/api/auth")
public class UserController  extends UserMsgModel {


    /**
     * 注册用户
     * @param b1
     * @return
     */
    @RequestMapping("/login")
    public Result text1(boolean b1){

/// /

        Date expiration = new Date(System.currentTimeMillis() + JwtUtio.EXPIRATION_TIME); //当前时间加一周
        String jwt = JwtUtio.JWTCreate(expiration,new JWTModel("1233", "zs", "admin"));


        ReturnModel add = UtioY.Ret().add("jwt", jwt).add("time", expiration);
        return Result.success(add);
    }

    /**
     * 权限测试
     * @param user_id
     * @param b1
     * @return
     */
    @RequestMapping("/text1")
    @UserRole(value = {"material:from","v2:text2"},and = {"material:update","material:delete"}) //用户需要的权限
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
//    @UserRole( {"cc","aa","bb"})
    public Result role(String user_role){


        List<String> strings = Role.role_list.get(user_role);

        if(strings==null||strings.isEmpty()){
            return Result.failure("没有该用户:"+user_role);
        }

        return Result.success(strings);
    }





    /**
     * 更新JWT
     * @param
     * @return
     */
    @RequestMapping("/jwt_update")
    public Result text1(@RequestHeader("Authorization") String authorization){
        Log.debug(authorization);
        Date expiration = new Date(System.currentTimeMillis() + JwtUtio.EXPIRATION_TIME); //当前时间加一周
        String jwt = JwtUtio.JWTUpdate(expiration,authorization);

        return Result.success(jwt);
    }


}