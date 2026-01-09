package com.example.UtioyV1.utio.UtioClass;

import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.UtioY;
import com.example.UtioyV1.utio.model.JWTDatasModel;
import com.example.UtioyV1.utio.model.JWTModel;
import com.example.UtioyV1.utio.service.TokenException;
import io.jsonwebtoken.*;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class JwtUtioOauth2Utio {
//
//    @Resource
//    private PublicKey publicKey;
//
//
//    public JWTDatasModel jwtOauth(String jwt) {
//        // 去除 "Bearer " 前缀
//        jwt = jwt.replaceAll("Bearer ", "").trim();
//
//        // 验证JWT token
//        try {
//            // 对于 RS256 算法使用 PublicKey，应使用 setSigningKey()
//            Claims body = Jwts.parserBuilder()
//                    .setSigningKey(publicKey)  // 对于 RS256，使用 setSigningKey() 设置公钥进行验证
//                    .build()
//                    .parseClaimsJws(jwt).getBody();
//
//            JWTDatasModel jwtDatasModel = new JWTDatasModel();
//            jwtDatasModel.setSubject(claims.getSubject());  //标识用户的唯一标识id
//            jwtDatasModel.setExpiration(claims.getExpiration()); //标识过期时间
//            jwtDatasModel.setIssuer(claims.getIssuer());  //标识颁布者
//            jwtDatasModel.setJwtmodel(UtioY.JSON_ObjectType(claims.get("jwtmodel",String.class), JWTModel.class));  //获取jwt参数里面放入的值
//            return jwtDatasModel;
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
//

}
