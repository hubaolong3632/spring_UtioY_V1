package com.example.UtioyV1.utio.Code;

import com.example.UtioyV1.utio.model.JurisdictionModel;

import java.security.PublicKey;
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


//    服务器公钥
    public static  String JwtKeyConfig ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp49w7qqY31OiE+UQ1dtsoOsXJIBe7fVqLPTIaMBB1C23VlSiNHN57X+6ltxMivHvRO4Eo9eEWwAvrS0Q8CJb4aRNXV+IuRjFxZbZGPVtbZoZ70pXldfUjzlGhXIlMkaaZsk7TUkSLqjukyb2CqOpnV0RvQXodO1FUPg3Z4RcLXXO0yeY97GCCLhxPCrprswIH9sRVp7gzr5EtFo9RJrZjH4go84SUgqDoEtXjxoHcFL30Xvt92W4015Yu7g4bjDltwpwm2SJ5/4wjrrSaRJeMLR0JdvZ/lRCWbrunnvx/qGL8vBhSN1Nq+C/aoU/K7gKNkGuz1sloxwVNh/953eI5wIDAQAB";// 服务器的公钥
//    解析的公钥对象
    public static PublicKey publicKey;



    public static  Map<String, List<String>> role_list=new HashMap<>(); //查找指定用户有哪个权限


    public static  Map<String, JurisdictionModel> jurisdiction_map = new HashMap<>();// 用于判断是否有权限


    public static boolean role_is(String role_name){
        return  jurisdiction_map.containsKey(role_name);
    }


}