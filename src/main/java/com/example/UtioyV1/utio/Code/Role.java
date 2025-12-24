package com.example.UtioyV1.utio.Code;

import com.example.UtioyV1.utio.model.JurisdictionModel;

import java.util.*;

/**
 * 放置权限别名或者是其他别名
 */
public class Role {
   public static String admin="admin"; //管理员
   public static String user="user"; //用户
   public static String visitors="visitors"; //游客
   public static String user_id="user_id"; //用户id
   public static String user_ip="user_ip"; //用户ip
   public static String user_role="user_role"; //用户角色



    public static  Map<String, List<String>> role_list=new HashMap<>(); //查找指定用户有哪个权限


    public static  Map<String, JurisdictionModel> jurisdiction_map = new HashMap<>();// 用于判断是否有权限


    public static boolean role_is(String role_name){
        return  jurisdiction_map.containsKey(role_name);
    }


}