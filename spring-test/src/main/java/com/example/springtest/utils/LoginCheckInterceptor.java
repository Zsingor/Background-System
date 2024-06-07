package com.example.springtest.utils;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springtest.entity.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override //目标资源运行前执行，返回true放行，返回false不放行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        //中文编码
        response.setCharacterEncoding("UTF-8");

        //处理预检请求
        if(request.getMethod().equals("OPTIONS"))
        {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        //获取请求中的令牌
        String token = request.getHeader("token");
        //判断令牌是否存在,如果不存在则返回错误信息
        if (!StringUtils.hasLength(token))
        {
            Result error = Result.error(5001,"用户未登录");
            //手动转换json对象
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }
        //尝试解析jwt令牌
        try
        {
            JwtUtils.parseJWT(token);
        }
        catch (Exception e) {
            //jwt解析失败，返回错误信息
            Result error = Result.error(5001,"登录过期");
            //手动转换json对象
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }
        //放行该请求
        return true;
    }

    @Override   //目标资源运行后执行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    @Override   //视图渲染完毕后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterHandle");
    }
}
