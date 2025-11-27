package com.example.spring_utioy_v1.utio.config;

import com.example.spring_utioy_v1.utio.UtioY;
import com.example.spring_utioy_v1.utio.model.JWTDatasModel;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 过滤器 - 解析 JWT 并在请求参数中添加 user_id
 */
@Component
@Order(1)
public class AddParamFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        CustomRequestWrapper wrappedRequest = new CustomRequestWrapper(httpRequest);
        
        // 尝试从 JWT 中获取 user_id
        String jwt = httpRequest.getHeader("Authorization");
        if (jwt != null && !jwt.isEmpty() && !jwt.equals("null")) {
            try {
                String token = jwt.split(" ")[1];
                JWTDatasModel jwtData = UtioY.JWT_getMessage(token);
                if (jwtData != null && jwtData.getJwtmodel() != null) {
                    Integer userId = jwtData.getJwtmodel().getId();
                    wrappedRequest.addParameter("user_id", String.valueOf(userId));
                }
            } catch (Exception e) {
                // JWT 解析失败，不添加参数
            }
        }
        
        chain.doFilter(wrappedRequest, response);
    }
}

