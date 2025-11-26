package com.example.spring_utioy_v1.utio.config;

import com.example.spring_utioy_v1.utio.Code.Config;
import com.example.spring_utioy_v1.utio.UtioClass.JwtUtio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



/**
 * 依赖注入
 */
@Configuration
public class InjectBeanConfig  implements WebMvcConfigurer {


    //允许所有请求跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 所有路径
                .allowedOriginPatterns("*")  // 允许所有来源（支持 allowCredentials）
                .allowedMethods("*")  // 允许所有方法 GET, POST, PUT, DELETE 等
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true)  // 允许携带 Cookie
                .maxAge(3600);
    }


    @Bean("jwt")
    public Boolean jwt(@Value("${jwt.secret:}")String secret, @Value("${jwt.issuer:}")String issuer){
        JwtUtio.setJWTKey(secret,issuer);
        return true;
    }


    @Override //图片放置
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("文件存储默认位置:" + Config.currentPath);
        registry.addResourceHandler("/**")  //匹配路径
                .addResourceLocations("file:" + Config.currentPath);
    }

}
