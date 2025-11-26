package com.example.spring_utioy_v1.utio.config;

import com.example.spring_utioy_v1.utio.UtioClass.JwtUtio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 依赖注入
 */
@Configuration
public class InjectBeanConfig  implements WebMvcConfigurer {
    public  static   String currentPath = System.getProperty("user.dir")+"/file";


    @Bean("jwt")
    public Boolean jwt(@Value("${jwt.secret:}")String secret, @Value("${jwt.issuer:}")String issuer){
        JwtUtio.setJWTKey(secret,issuer);
        return true;
    }


    @Override //图片放置
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("文件存储默认位置:"+currentPath);
        registry.addResourceHandler("/**")  //匹配路径
                .addResourceLocations("file:"+currentPath);
    }

}
