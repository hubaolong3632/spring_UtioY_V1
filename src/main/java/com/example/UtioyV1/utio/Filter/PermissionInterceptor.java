package com.example.UtioyV1.utio.Filter;

import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.Code.Result;
import com.example.UtioyV1.utio.Code.ResultCode;
import com.example.UtioyV1.utio.Code.Role;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.config.FilterTool;
import com.example.UtioyV1.utio.model.JWTDatasModel;
import com.example.UtioyV1.utio.model.JWTModel;
import com.example.UtioyV1.utio.model.UserRole;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

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

//v1sa
        // 获取注解中的权限要求

//        获取需要的那几个权限
        String[] orJurisdiction = userRole.value();
//        获取一定需要的权限
        String[] andJurisdiction = userRole.and();



        if(orJurisdiction!=null){ //如果拥有其中一个权限

            for (String or_key : orJurisdiction) {
                if (Role.role_is(or_key)) { //判断是否有此权限
                    return true;
                }
            }
            return false;


            filterTool.send(response, Result.failure(ResultCode.TONKEN_NO, "角色不匹配，需要: " + requiredName));
            return false;
        }

        // 1. 校验角色名称（name）
        if (requiredName != null && !requiredName.isEmpty()) {
            String userName = jwtModel.getName();
            if (!requiredName.equals(userName)) {
                filterTool.send(response, Result.failure(ResultCode.TONKEN_NO, "角色不匹配，需要: " + requiredName));
                return false;
            }
        }

        // 2. 校验权限代码（code）- 如果需要的话
        if (requiredCode != null && !requiredCode.isEmpty()) {
            // 这里可以根据实际需求添加 code 校验逻辑
            // 例如：检查 jwtModel 中是否有对应的 code
        }

        // 3. 校验权限列表（permissions）
        if (requiredPermissions != null && requiredPermissions.length > 0) {
            // 获取用户拥有的权限列表（根据用户的 name 从 Role.role_list 中获取）
            String userName = jwtModel.getName();
            List<String> userPermissions = Role.role_list.get(userName);

            if (userPermissions == null || userPermissions.isEmpty()) {
                filterTool.send(response, Result.failure(ResultCode.TONKEN_NO, "用户没有权限"));
                return false;
            }

            // 检查用户是否拥有所有要求的权限（优化：先检查直接匹配，再检查完整key）
            for (String permission : requiredPermissions) {
                boolean hasPermission = false;

                // 方式1：直接匹配（权限名称完全一致）- O(n)，但通常很快
                if (userPermissions.contains(permission)) {
                    hasPermission = true;
                } else {
                    // 方式2：构建完整权限 key 进行匹配（格式：name:permission）
                    String fullPermissionKey = userName + ":" + permission;
                    if (Role.jurisdiction_map.containsKey(fullPermissionKey)) {
                        // 检查用户权限列表中是否包含该权限的完整名称
                        for (String userPerm : userPermissions) {
                            if (userPerm.equals(permission) || userPerm.endsWith(":" + permission)) {
                                hasPermission = true;
                                break;
                            }
                        }
                    }
                }

                if (!hasPermission) {
                    filterTool.send(response, Result.failure(ResultCode.TONKEN_NO,
                        "权限不足，缺少权限: " + permission + "，需要权限: " + Arrays.toString(requiredPermissions)));
                    return false;
                }
            }

        }

        // 权限校验通过
        return true;
    }
}

