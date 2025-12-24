package com.example.UtioyV1.utio.config;

import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.UtioY;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器路径配置
 */
@Component
@Data
@ConfigurationProperties(prefix = "interceptor")
public class FilterTool {

    /**
     * 拦截的路径
     */
    private List<String> includePaths = new ArrayList<>();

    /**
     * 排除的路径
     */
    private List<String> excludePaths = new ArrayList<>();


    // 2. Spring路径匹配器（支持通配符：/**、*、?）
    private final static AntPathMatcher pathMatcher = new AntPathMatcher();


    /**
     * 工具方法：判断请求路径是否匹配指定的路径列表（支持通配符）
     */
    public  boolean isPathMatch(String requestURI) {




//        判断是否是被排除的路径
        for (String path : excludePaths) {
            if (pathMatcher.match(path, requestURI)) {
                return true;
            }
        }


//        判断是否是被拦截的路径  如果是就返回false 表示拦截
        for (String path : includePaths) {
            if (pathMatcher.match(path, requestURI)) {
                return false;
            }
        }


        return true;
    }

    /**
     * 发送JSON格式的错误响应
     */
    public  void send(HttpServletResponse response, Result result) {
        Log.debug(UtioY.JSON(result),"封禁");
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK.value());
            PrintWriter writer = response.getWriter();
            writer.write(UtioY.JSON(result));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}