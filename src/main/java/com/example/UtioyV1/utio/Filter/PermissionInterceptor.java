package com.example.UtioyV1.utio.Filter;

import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Code.ResultCode;
import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.UtioY;
import com.example.UtioyV1.utio.config.FilterTool;
import com.example.UtioyV1.utio.model.UserRole;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限拦截器 - 检查方法上的 @UserRole 注解并进行权限校验
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Resource
    private FilterTool filterTool;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只处理 Controller 方法（快速失败，避免不必要的处理）
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 先检查方法上的 @UserRole 注解（方法注解优先级更高）
        UserRole userRole = handlerMethod.getMethodAnnotation(UserRole.class);

        // 如果方法上没有，再检查类上的注解
        if (userRole == null) {
            userRole = handlerMethod.getBeanType().getAnnotation(UserRole.class);
        }

        // 如果方法上和类上都没有 @UserRole 注解，直接放行（大部分请求会走这里，性能最优）
        if (userRole == null) {
            return true;
        }


    // 获取需要的那几个权限
        String[] orJurisdiction = userRole.value();
    // 获取一定需要的权限
        String[] andJurisdiction = userRole.and();


        String jurisdiction = UtioY.JWT_getUser(request).getRole()+":";
//        System.out.println("用户权限"+jurisdiction);

        if(orJurisdiction!=null){ //如果拥有其中一个权限
            boolean bol=false;
            for (String or_key : orJurisdiction) {
                System.out.println(or_key);
                if (Role.role_is(jurisdiction+or_key)) { //判断是否有此权限
                    bol=true; //有一个满足就可以了
                    break;
                }
            }
            if(bol==false){//如果无权限
                filterTool.send(response, Result.failure(ResultCode.ROLE_NO, "角色没有对应权限: "+String.join(",", orJurisdiction)));
                return false;
            }
        }



        if(andJurisdiction!=null){ //如果拥有其中一个权限
            for (String and_key : andJurisdiction) {
                if (!Role.role_is(jurisdiction+and_key)) { //判断是否有此权限
                    filterTool.send(response, Result.failure(ResultCode.ROLE_NO, "角色没有: "+and_key+"权限"));
                   return false;
                }
            }

        }




        // 权限校验通过
        return true;
    }
}

