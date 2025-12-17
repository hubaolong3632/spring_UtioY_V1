package com.example.UtioyV1.utio.Filter;

import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Code.ResultCode;
import com.example.UtioyV1.utio.UtioY;
import com.example.UtioyV1.utio.model.JWTDatasModel;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

@Component

public class JWTFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("请求前执行...");

        String jwt = request.getHeader("Authorization");
        if(jwt==null|| jwt.isEmpty()||jwt.equals("null")){
            send(response, Result.failure(ResultCode.TONKEN_NO,"请在请求头里面带上Authorization"));
            return false;
        }
        try{
            jwt= jwt.split(" ")[1];
        }catch (Exception e){
            System.out.println(jwt);
            e.printStackTrace();
        }

        JWTDatasModel jwtDatasModel = UtioY.JWT_getMessage(jwt);//认证jwt
        if(jwtDatasModel==null){
            send(response,Result.failure(ResultCode.TONKEN_NO,"Authorization授权认证失败请检查jwt格式/或已过期"));
            return false;
        }
//        String jurisdiction = jwtDatasModel.getJwtmodel().getJurisdiction();
//        System.out.println("权限："+jurisdiction);
        System.out.println("\n========访问人权限:"+jwtDatasModel.getJwtmodel().getJurisdiction()+"   "+"访问人id:"+jwtDatasModel.getJwtmodel().getId()+"======");
        System.out.println(jwtDatasModel);
        request.setAttribute("CLAIMS_JWS_ATTRIBUTE",jwtDatasModel); //认证成功往下传递

        return true;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("请求后执行(没有任何异常执行)...");
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        Thread.sleep(5000);

        System.out.println("全部执行完成后执行（一定会执行无论有没有异常）...");
    }

    public void send(ServletResponse response, Result result){
        try{
            HttpServletResponse httpreSponse = (HttpServletResponse) response;

            httpreSponse.setContentType("application/json;charset=UTF-8");
            httpreSponse.setStatus(HttpStatus.OK.value());
            // 获取响应的输出流写入器。
            PrintWriter writer = response.getWriter();
            writer.write(UtioY.JSON(result));
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}