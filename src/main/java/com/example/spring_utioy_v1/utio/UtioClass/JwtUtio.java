package com.example.spring_utioy_v1.utio.UtioClass;


import com.example.spring_utioy_v1.utio.UtioY;
import com.example.spring_utioy_v1.utio.model.JWTDatasModel;
import com.example.spring_utioy_v1.utio.model.JWTModel;
import com.example.spring_utioy_v1.utio.service.TokenException;
import io.jsonwebtoken.*;

import java.util.Date;
public class JwtUtio {

//    private static final long EXPIRATION_TIME = 31536000000; // 一年
//    private static final long  EXPIRATION_TIME = 2592000000; // 一个月
    private static final long EXPIRATION_TIME = 604800000; // 一周
//    private static final long EXPIRATION_TIME =   86400000; // 一天
    private static  String SECRET = "8520b3d2d13b0d8ace53d6a81f1ca25c834234234234"; // 秘钥 绝密
    private static  String ISSUER = "SK-00000.work"; // 签发者




    //放入密钥
    public static void setJWTKey(String secret, String issuer) {
        if(!secret.isEmpty()){
            JwtUtio.SECRET=secret;
        }
        if(!issuer.isEmpty()){
            JwtUtio.ISSUER =issuer;
        }


    }


    //    生成JWT
    public static String JWTCreate(String subject, JWTModel jwtmodel) { //提交面向的对象  和需要保存的数据
        // 设置过期时间
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME); //当前时间加一周
        // 创建 JWT
        String jwt = Jwts.builder()
                .setSubject(subject)  //jwt所面向的对象
                .setExpiration(expiration) //过期时间
//                .setNotBefore(expiration) //设置在当前时间都是过期的 就是不让使用 没事别打开
                .setIssuer(ISSUER)    //定义签发者
                .claim("jwtmodel", UtioY.JSON(jwtmodel)) //对象方式 放入需要保存的参数
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return jwt;
    }


    // 解析 JWT
    public static JWTDatasModel JWTAnalysis(String jwt) {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET) //放入秘钥
                    .parseClaimsJws(jwt) //放入需要解析的串
                    .getBody();

            JWTDatasModel jwtDatasModel = new JWTDatasModel();
            jwtDatasModel.setSubject(claims.getSubject());  //标识用户的唯一标识id
            jwtDatasModel.setExpiration(claims.getExpiration()); //标识过期时间
            jwtDatasModel.setIssuer(claims.getIssuer());  //标识颁布者
            jwtDatasModel.setJwtmodel(UtioY.JSON_ObjectType(claims.get("jwtmodel",String.class),JWTModel.class));  //获取jwt参数里面放入的值


            return jwtDatasModel;

        } catch (ExpiredJwtException e) {
            // 令牌已过期
            throw new TokenException("令牌已过期，请重新登录");
        } catch (SignatureException e) {
            // 签名验证失败（如秘钥不匹配）
            throw new TokenException("令牌签名无效");
        } catch (MalformedJwtException e) {
            // 令牌格式错误
            throw new TokenException("令牌格式不正确");
        } catch (InvalidClaimException e) {
            // 令牌中的声明无效
            throw new TokenException("令牌内容无效");
        } catch (Exception e) {
            // 其他未知错误
            throw new TokenException("认证失败，请检查令牌");
        }
    }


    /**
     *提供旧的jwt 对当前jwt进行增长时间
     */
    public static String JWTCreate(String jwt) {

        jwt=jwt.replace("Bnarer ","");
        System.out.println("jwt去掉头后:"+jwt);

        JWTDatasModel jwtDatasModel = JWTAnalysis(jwt);
        JWTCreate(jwtDatasModel.getSubject(),jwtDatasModel.getJwtmodel());
        return jwt;
    }




}





//         标准注册界面 headers                       公共声明部分        payloads                                                                                        签证信息                                   私有声明部分
//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJleHAiOjE2ODQ0MTk5OTMsImlzcyI6Ik15Q29tcGFueSIsIm5hbWUiOiLlvKDkuIkiLCJwYXNzd29yZCI6InF3ZTEyMzUifQ.o5pHTRsTkE4GVt-YqYflijkiJjcrSUSe5Tya-0UtFrR1EQYQffMdis53Hycxv54fawJWwoRqLUBHxiJRfM_yMQ