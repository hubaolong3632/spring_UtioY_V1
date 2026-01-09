package com.example.UtioyV1.utio.config;

import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.Filter.JWTFilter;
import com.example.UtioyV1.utio.Filter.PermissionInterceptor;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.UtioClass.ApiUtio;
import com.example.UtioyV1.utio.UtioClass.JwtUtio;
import com.example.UtioyV1.utio.UtioY;
import jakarta.annotation.PostConstruct;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.example.UtioyV1.utio.Log.initializeLogThread;


/**
 * 依赖注入
 */
@Configuration
@EnableScheduling //自动注入yml
@EnableAsync // 开启异步支持
@MapperScan("com.example.*.utio.mapper")
@MapperScan("com.example.*.mapper") //扫描依赖
public class WebMvcBeanConfig implements WebMvcConfigurer, CommandLineRunner {


        //返回公钥
//        @Async //异步调用
//        @Bean
//        public PublicKey publicKeyClass(){
//            PublicKey publicKey=null;
//
//
//            if(Config.MicroService==true){ //判断是否开启微服务调用
//
//                ApiUtio.API_get("http://127.0.0.1:1111");
//
//
//
//                    String JwtKeyConfig= Role.JwtKeyConfig; //这个是公钥
//                    byte[] publicKeyBytes = Base64.getDecoder().decode(JwtKeyConfig);
//                    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
//
//                    try{
//                        publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
//                        Role.publicKey=publicKey;
//                        Log.info("微服务公钥配置完成");
//                    }catch (Exception e){
//                        System.out.println("请配置正确的公钥");
//                        e.printStackTrace();
//                    }
//
//            }
//
//            return publicKey;
//        }


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
//    @PostConstruct   //所有Beng执行完成后执行
    public Boolean jwt(@Value("${jwt.secret:}")String secret, @Value("${jwt.issuer:}")String issuer){
        JwtUtio.setJWTKey(secret,issuer);
        return true;
    }



    //其他文件注入
    @Bean("text1")
//    @PostConstruct
    public Boolean text(Config interc){
        System.out.println("111");
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

//
//        String property = environment.getProperty("config.fileUrl");
//
//        Log.debug("x:"+property);


                Field[] declaredFields = Config.class.getDeclaredFields();
                for (Field s1 : declaredFields){

                    Class<?> type = s1.getType();
                    if(!isTargetFieldType(type)){ //如果不是基本包装类就不要
                        continue;
                    }
                    String daoValue = Config.getDaoValue(s1.getName());


                    if(daoValue==null) continue; //如果为空的话就跳过当前循环

//                    System.out.println("类别:"+type);
//                    System.out.println("对象:"+s1.getName());
//                    System.out.println(daoValue);


                    // 关键1：设置字段可访问
                    s1.setAccessible(true);

//                判断是否是静态字段
                    if (Modifier.isStatic(s1.getModifiers())) {
                        // 4. 核心：将字符串"true"转为字段对应类型的值
                        Object targetValue = convertStringToTargetType(daoValue, type);
                        // 5. 赋值（此时值的类型与字段类型完全匹配）
                        s1.set(null, targetValue);


//                        System.out.println("======赋值========");
//                        System.out.println(Config.CLAIMS_JWS_ATTRIBUTE);
//                        System.out.println(Config.FILE_URL);
//                        System.out.println(Config.currentPath);
//                        System.out.println(Config.d1);
//                        System.out.println(Config.t1);
//                        System.out.println(Config.ll);
                    }
                }


//      启动日志
        initializeLogThread();


    }



    // 核心：判断字段类型是否是目标类型（String + 基本类型 + 包装类）  如果是就返回true
    private static boolean isTargetFieldType(Class<?> fieldType) {
        return fieldType == String.class                // String 类型
                || fieldType == int.class || fieldType == Integer.class // int/Integer
                || fieldType == float.class || fieldType == Float.class // float/Float
                || fieldType == double.class || fieldType == Double.class // double/Double
                || fieldType == boolean.class || fieldType == Boolean.class; // boolean/Boolean
    }


    // 核心工具方法：将字符串转为指定类型的值
    private static Object convertStringToTargetType(String strValue, Class<?> targetType) {
        // 空值处理（可选，根据业务调整）
        if (strValue == null) {
            return null;
        }

        // 1. String 类型：直接返回原字符串
        if (targetType == String.class) {
            return strValue;
        }

        // 2. boolean/Boolean 类型：字符串转布尔值（兼容 true/TRUE/1，false/FALSE/0）
        if (targetType == boolean.class || targetType == Boolean.class) {
            return "true".equalsIgnoreCase(strValue) || "1".equals(strValue);
        }

        // 3. int/Integer 类型：字符串转整型（转换失败返回默认值0）
        if (targetType == int.class || targetType == Integer.class) {
            try {
                return Integer.parseInt(strValue);
            } catch (NumberFormatException e) {
                Log.error("字符串[" + strValue + "]转整型失败，赋值默认值0");
                return 0;
            }
        }

        // 4. float/Float 类型：字符串转浮点型（转换失败返回默认值0.0f）
        if (targetType == float.class || targetType == Float.class) {
            try {
                return Float.parseFloat(strValue);
            } catch (NumberFormatException e) {
                Log.error("字符串[" + strValue + "]转浮点型失败，赋值默认值0.0f");
                return 0.0f;
            }
        }

        // 5. double/Double 类型：字符串转双精度型（转换失败返回默认值0.0d）
        if (targetType == double.class || targetType == Double.class) {
            try {
                return Double.parseDouble(strValue);
            } catch (NumberFormatException e) {
               Log.error("字符串[" + strValue + "]转双精度型失败，赋值默认值0.0d");
                return 0.0d;
            }
        }

        // 非目标类型（实际不会走到这里，因为前面已过滤）
        return null;
    }


}


/**
 *  反射教程
 Config text11 = new Config();
 Class<?> clazz = text11.getClass();
 Field[] declaredFields = Text11.class.getDeclaredFields();
 for (Field s1 : declaredFields){

 Field f1 = clazz.getDeclaredField(s1.getName());
 f1.set(text11,getString(row, s1.getName()) ); // 公有字段也可加setAccessible，不影响
 //                    System.out.println("对象："+s1.getName());
 }
 */