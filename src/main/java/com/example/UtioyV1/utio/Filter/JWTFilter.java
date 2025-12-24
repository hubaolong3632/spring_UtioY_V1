package com.example.UtioyV1.utio.Filter;

import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Code.ResultCode;
import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.UtioY;
import com.example.UtioyV1.utio.config.CustomRequestWrapper;
import com.example.UtioyV1.utio.config.FilterTool;
import com.example.UtioyV1.utio.model.JWTDatasModel;
import com.example.UtioyV1.utio.service.TokenException;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * JWT过滤器（替代原Interceptor）
 * 规则：拦截interceptor.includePaths配置的路径，放行excludePaths配置的路径
 */
@Component
@Order(2) //执行校验逻辑
public class JWTFilter implements Filter {

    // 1. 注入路径配置类（读取application.yml中的interceptor.include/exclude-paths）

    @Resource
    private FilterTool filterTool;



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI(); // 获取当前请求路径

        Log.debug("请求路径:" + requestURI);

        boolean pathMatch = filterTool.isPathMatch(requestURI);
        if(pathMatch){ //为true表示这个不需要拦截
//            wrappedRequest.addParameter("user_id", "222");
            filterChain.doFilter(new CustomRequestWrapper(request), response); //执行完成过滤就结束程序
            return;
        }


        // ========== 仅拦截指定路径 → 执行JWT校验逻辑 ==========
        String jwt = request.getHeader("Authorization");
        if (jwt == null || jwt.isEmpty() || jwt.equals("null")) {
            filterTool.send(response, Result.failure(ResultCode.TONKEN_NO, "请在请求头里面带上Authorization"));
            return; // 校验失败，终止请求
        }

        // 解析Bearer Token（处理无空格/格式错误场景）
        String token = null;
        try {
            String[] jwtParts = jwt.split(" ");
            if (jwtParts.length != 2 || !"Bearer".equalsIgnoreCase(jwtParts[0])) {
                throw new IllegalArgumentException("JWT格式错误，需符合 Bearer + Token 规范");
            }
            token = jwtParts[1];


        // 校验JWT有效性
        JWTDatasModel jwtDatasModel = UtioY.JWT_getMessage(token);

        // ========== JWT校验成功 → 传递数据 + 添加参数 ==========
        // 1. 将JWT信息存入request属性，供后续过滤器/控制器使用
        request.setAttribute(Config.CLAIMS_JWS_ATTRIBUTE, jwtDatasModel);


        // 2. 打印认证信息
        Log.debug("\n========访问人权限:" + jwtDatasModel.getJwtmodel().getRole() + "   访问人id:" + jwtDatasModel.getJwtmodel().getId() +"  用户ip:"+request.getParameter("user_ip")+"  路径:"+requestURI+ "======\n");
//        Log.debug("JWT解析结果：" + jwtDatasModel);


        // 3. 包装请求，添加user_id参数（Filter中必须传递包装后的request才生效）
        CustomRequestWrapper wrappedRequest = new CustomRequestWrapper(request);
        wrappedRequest.addParameter(Role.user_id, jwtDatasModel.getJwtmodel().getId()); // 测试用固定值，可替换为真实ID：jwtDatasModel.getJwtmodel().getId().toString()
        wrappedRequest.addParameter(Role.user_role, jwtDatasModel.getJwtmodel().getRole()); // 测试用固定值，可替换为真实ID：jwtDatasModel.getJwtmodel().getId().toString()


        // 4. 传递包装后的请求到后续过滤器/控制器
        filterChain.doFilter(wrappedRequest, response);



        }
        catch (TokenException e) { //如果是密钥错误
            filterTool.send(response, Result.failure(ResultCode.TONKEN_NO, e.getMessage()));
        }
        catch (Exception e) {
            System.out.println("JWT格式解析失败：" + jwt);
            e.printStackTrace();
            filterTool.send(response, Result.failure(ResultCode.TONKEN_NO, "Authorization格式错误（正确格式：Bearer {token}）"));
        }
    }


    // Filter默认空实现方法（可保留）
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Log.debug("JWTFilter 初始化完成");

        Log.info("配置L:"+Config.FILE_URL);
    }

    @Override
    public void destroy() {
        System.out.println("JWTFilter 程序关闭销毁");
    }
}