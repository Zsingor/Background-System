package com.example.projectbackend.aop.authorize;


import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.projectbackend.entity.Result;
import com.example.projectbackend.service.UserService;
import com.example.projectbackend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

@Component
@Aspect
@Order(1)
public class AuthorizeAspect {

    @Autowired
    private UserService userService;

//    @Autowired
//    private HttpServletResponse response;

    @Around("@annotation(preAuthorize)")
    public Object doAround(ProceedingJoinPoint joinPoint,PreAuthorize preAuthorize) throws Throwable {

        //判断是否为登录请求，若是则继续执行
        String methodName = joinPoint.getSignature().getName();
        if(Objects.equals(methodName, "userlogin"))
        {
            return joinPoint.proceed();
        }

        //获取请求与响应
        ServletRequestAttributes attributes=(ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        HttpServletRequest request=attributes.getRequest();
        HttpServletResponse response=attributes.getResponse();
        assert response != null;

        String token=request.getHeader("token");
        DecodedJWT verify= JwtUtils.parseJWT(token);
        String userid=verify.getClaim("user_id").toString().replace("\"", "");
        List<String> authorityList=userService.queryUserAuthority(userid);

        String pemission=preAuthorize.value();

        boolean flag=authorityList.contains(pemission);

        if(!flag)
        {
            response.setCharacterEncoding("UTF-8");
            Result error = Result.error(5002,"用户无该操作权限");
            //手动转换json对象
            String result = JSONObject.toJSONString(error);
            response.getWriter().write(result);
            return null;
        }
        return joinPoint.proceed();
    }
}
