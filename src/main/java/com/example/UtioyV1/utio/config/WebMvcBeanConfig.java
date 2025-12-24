package com.example.UtioyV1.utio.config;

import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.Filter.JWTFilter;
import com.example.UtioyV1.utio.Filter.PermissionInterceptor;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.UtioClass.JwtUtio;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.*;

import static com.example.UtioyV1.utio.Log.initializeLogThread;


/**
 * 依赖注入
 */
@Configuration
@EnableScheduling //自动注入yml
@EnableAsync // 开启异步支持
@MapperScan("com.example.*.utio.mapper")
@MapperScan("com.example.*.mapper")
public class WebMvcBeanConfig implements WebMvcConfigurer, CommandLineRunner {



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
    @Bean("text1")
    public Boolean text(Config interc){
//        Log.info("输入"+UtioY.JSON(interc));
//        Log.info("输入"+UtioY.JSON(interc));
//        Log.error("输入"+UtioY.JSON(interc));
//        Log.debug("输入"+UtioY.JSON(interc));
//        Log.warn("输出保存");
//        Log.severe("错误");
//        Log.dao_save("保存到数据库");
//        Log.info("错误","ss");
//        Log.info("错误","ss");


//        for(int i=0;i<10000;i++){
//            Log.dao_save("t1","你好","13");
//        }
        return true;
    }


    //配置图片放置
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("文件存储默认位置:" + Config.currentPath);
        registry.addResourceHandler("/**")  //匹配路径
                .addResourceLocations("file:" + Config.currentPath);
    }


//    @Resource
//    private JWTFilter myInterceptor;
//
//    @Resource
//    private InterceptorConfig interceptorConfig;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration interceptor = registry.addInterceptor(myInterceptor);
//
//        // 添加拦截路径
//        if (interceptorConfig.getIncludePaths() != null && !interceptorConfig.getIncludePaths().isEmpty()) {
//            interceptor.addPathPatterns(interceptorConfig.getIncludePaths());
//            Log.debug("拦截路径: " + interceptorConfig.getIncludePaths());
//        }
//
//        // 添加排除路径
//        if (interceptorConfig.getExcludePaths() != null && !interceptorConfig.getExcludePaths().isEmpty()) {
//            interceptor.excludePathPatterns(interceptorConfig.getExcludePaths());
//            Log.debug("排除路径: " + interceptorConfig.getExcludePaths());
//        }
//    }

    @Resource
    private PermissionInterceptor permissionInterceptor;

    @Resource
    private FilterTool filterTool;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册权限拦截器 - 使用和过滤器一样的路径配置
        var interceptorRegistration = registry.addInterceptor(permissionInterceptor);
        
        // 添加拦截路径（从 FilterTool 读取）
        if (filterTool.getIncludePaths() != null && !filterTool.getIncludePaths().isEmpty()) {
            interceptorRegistration.addPathPatterns(filterTool.getIncludePaths());
            Log.debug("权限拦截器拦截路径: " + filterTool.getIncludePaths());
        } else {
            // 如果没有配置拦截路径，默认拦截所有路径
            interceptorRegistration.addPathPatterns("/**");
        }
        
        // 添加排除路径（从 FilterTool 读取）
        if (filterTool.getExcludePaths() != null && !filterTool.getExcludePaths().isEmpty()) {
            interceptorRegistration.excludePathPatterns(filterTool.getExcludePaths());
            Log.debug("权限拦截器排除路径: " + filterTool.getExcludePaths());
        }
        
        Log.debug("权限拦截器已注册，路径配置与过滤器一致");
    }

    @Autowired
    private Environment environment;

    @Resource
    private InitConfig init;


    // 应用启动完成后执行的逻辑
    @Override
    @Async //异步导入
    public void run(String... args) throws Exception {

        // 获取激活的配置文件
        String[] activeProfiles = environment.getActiveProfiles();
        String activeProfilesStr = activeProfiles.length > 0
                ? String.join(",", activeProfiles)
                : "默认配置";
        String port = environment.getProperty("server.port", "8080");

        System.out.println("配置:" + "\u001B[1;31m" + activeProfilesStr + "\u001B[0m" + "   端口:" + "\u001B[1;31m" + port + "\u001B[0m");
        Log.info("配置:" +  activeProfilesStr  + "   端口:"  + port );



        init.initConfig(); //初始化配置文件


        //                Text11 text11 = new Text11();
//                Class<?> clazz = text11.getClass();
//                Field[] declaredFields = Text11.class.getDeclaredFields();
//                for (Field s1 : declaredFields){
//
//                    Field f1 = clazz.getDeclaredField(s1.getName());
//                    f1.set(text11,getString(row, s1.getName()) ); // 公有字段也可加setAccessible，不影响
////                    System.out.println("对象："+s1.getName());
//                }
//
//                System.out.println("t1:  "+text11);
//
//
//                if(1==1){
//                    return;
//                }


//      启动日志
        initializeLogThread();


    }


}