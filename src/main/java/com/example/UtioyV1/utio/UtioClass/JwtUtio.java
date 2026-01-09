package com.example.UtioyV1.utio.UtioClass;


import com.example.UtioyV1.utio.UtioY;
import com.example.UtioyV1.utio.model.JWTDatasModel;
import com.example.UtioyV1.utio.model.JWTModel;
import com.example.UtioyV1.utio.service.TokenException;
import io.jsonwebtoken.*;
import jakarta.annotation.Resource;

import java.security.PublicKey;
import java.util.Date;
public class JwtUtio {

//    private static final long EXPIRATION_TIME = 31536000000; // 一年
//    private static final long  EXPIRATION_TIME = 2592000000; // 一个月
    public static final long EXPIRATION_TIME = 604800000; // 一周
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
    public static String JWTCreate(Date expiration,JWTModel jwtmodel) { //提交面向的对象  和需要保存的数据

        // 创建 JWT
        String jwt = Jwts.builder()
                .setSubject(UtioY.Random_string20())  //用户临时id 用于做用户数量限制等功能
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
    public static String JWTUpdate(Date expiration,String jwt) {

        jwt=jwt.replace("Bnarer ","");
        System.out.println("jwt去掉头后:"+jwt);

        JWTDatasModel jwtDatasModel = JWTAnalysis(jwt);
        JWTCreate(expiration,jwtDatasModel.getJwtmodel());
        return jwt;
    }



//
////    新版jwt解析
//@Resource
//public PublicKey publicKey;
//
//    @Override
//    public Jws<Claims> jwtOauth(String jwt) {
//        // 去除 "Bearer " 前缀
//        jwt = jwt.replaceAll("Bearer ", "").trim();
//
//        // 验证JWT token
//        try {
//            // 使用新的 API：parserBuilder() 替代已弃用的 parser()
//            // 注意：在 jjwt 0.11.5 中，setSigningKey() 在 parserBuilder() 中仍然可用
//            // verifyWith() 方法在 0.12.0+ 版本才引入，且主要用于对称密钥
//            // 对于 RS256 算法使用 PublicKey，应使用 setSigningKey()
//            Jws<Claims> jws = Jwts.parserBuilder()
//                    .setSigningKey(publicKey)  // 对于 RS256，使用 setSigningKey() 设置公钥进行验证
//                    .build()
//                    .parseClaimsJws(jwt);
//            return jws;
//
//        } catch (ExpiredJwtException e) {
//            // 令牌已过期
//            throw new TokenException("令牌已过期，请重新登录");
//        } catch (UnsupportedJwtException e) {
//            // 不支持的 JWT 格式
//            throw new TokenException("不支持的令牌格式");
//        } catch (MalformedJwtException e) {
//            // 令牌格式错误
//            throw new TokenException("令牌格式不正确");
//        } catch (io.jsonwebtoken.security.SignatureException e) {
//            // 签名验证失败（如秘钥不匹配）
//            throw new TokenException("令牌签名无效");
//        } catch (IllegalArgumentException e) {
//            // 参数错误（如 JWT 字符串为空）
//            throw new TokenException("令牌参数错误");
//        } catch (Exception e) {
//            // 其他未知错误
//            throw new TokenException("认证失败，请检查令牌");
//        }
//    }

}





//         标准注册界面 headers                       公共声明部分        payloads                                                                                        签证信息                                   私有声明部分
//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJleHAiOjE2ODQ0MTk5OTMsImlzcyI6Ik15Q29tcGFueSIsIm5hbWUiOiLlvKDkuIkiLCJwYXNzd29yZCI6InF3ZTEyMzUifQ.o5pHTRsTkE4GVt-YqYflijkiJjcrSUSe5Tya-0UtFrR1EQYQffMdis53Hycxv54fawJWwoRqLUBHxiJRfM_yMQ