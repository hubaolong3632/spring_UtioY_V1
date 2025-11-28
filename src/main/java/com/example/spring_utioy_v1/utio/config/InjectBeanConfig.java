package com.example.spring_utioy_v1.utio.config;

import com.example.spring_utioy_v1.utio.Code.Config;
import com.example.spring_utioy_v1.utio.Log;
import com.example.spring_utioy_v1.utio.UtioClass.JwtUtio;
import com.example.spring_utioy_v1.utio.UtioY;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;


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

    //配置jwt注入
    @Bean("jwt")
    public Boolean jwt(@Value("${jwt.secret:}")String secret, @Value("${jwt.issuer:}")String issuer){
        JwtUtio.setJWTKey(secret,issuer);
        return true;
    }



    //其他文件注入
    @Bean("text")
    public Boolean text(Config interc){
        Log.info("输入"+UtioY.JSON(interc));
        Log.error("输入"+UtioY.JSON(interc));
        Log.debug("输入"+UtioY.JSON(interc));
        return true;
    }


    //配置图片放置
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("文件存储默认位置:" + Config.currentPath);
        registry.addResourceHandler("/**")  //匹配路径
                .addResourceLocations("file:" + Config.currentPath);
    }


    @Resource
    private JWTFilter myInterceptor;

    @Resource
    private InterceptorConfig interceptorConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(myInterceptor);

        // 添加拦截路径
        if (interceptorConfig.getIncludePaths() != null && !interceptorConfig.getIncludePaths().isEmpty()) {
            interceptor.addPathPatterns(interceptorConfig.getIncludePaths());
            System.out.println("拦截路径: " + interceptorConfig.getIncludePaths());
        }

        // 添加排除路径
        if (interceptorConfig.getExcludePaths() != null && !interceptorConfig.getExcludePaths().isEmpty()) {
            interceptor.excludePathPatterns(interceptorConfig.getExcludePaths());
            System.out.println("排除路径: " + interceptorConfig.getExcludePaths());
        }
    }

}
