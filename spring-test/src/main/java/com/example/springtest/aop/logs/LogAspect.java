package com.example.springtest.aop.logs;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springtest.entity.Logs;
import com.example.springtest.entity.Result;
import com.example.springtest.utils.JwtUtils;
import com.example.springtest.utils.UUIDUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Component
@Aspect
//处理AOP的处理器
public class LogAspect {
    @Around("@annotation(autoLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, AutoLog autoLog)throws Throwable{
        try {
            //执行具体的接口
            Result result=(Result) joinPoint.proceed();

            String methodName = joinPoint.getSignature().getName();
            Method method = currentMethod(joinPoint,methodName);
            System.out.println("方法名称"+methodName);
            System.out.println("操作包名"+joinPoint.getTarget().getClass().getName());
            System.out.println(method);
            System.out.println("参数:"+Arrays.toString(joinPoint.getArgs()));


            //操作内容
            String operate=autoLog.module();

            //操作描述
            String identify=autoLog.description();

            //操作时间
            Date time=new Date();

            HttpServletRequest request= ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //操作ip
            String ip=getIp(request);

            //操作姓名
            String user_id="";
            String user_name="";
            DecodedJWT verify = null;
            String token = request.getHeader("token");
            if(token!=null)
            {
                verify=JwtUtils.parseJWT(token);
                user_id=verify.getClaim("user_id").toString();
                user_name=verify.getClaim("user_name").toString();
            }
            else
            {
                JSONObject data=(JSONObject) result.getData();
                token=data.getString("token");
                verify=JwtUtils.parseJWT(token);
                user_id=verify.getClaim("user_id").toString();
                user_name=verify.getClaim("user_name").toString();
            }
            String ID= UUIDUtils.getUUID();
//            Logs log=new Logs(ID,user_id,user_name,identify,operate,ip,time);
//            logService.addlog(operationLog);

            return result;
        }
        catch (Exception error)
        {
            System.out.println(error);
            return Result.error("日志信息添加失败");
        }
    }

    private String getIp(HttpServletRequest request) {
        List<String> ipHeadList = Stream.of("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "X-Real-IP").toList();
        for (String ipHead : ipHeadList) {
            if (checkIP(request.getHeader(ipHead))) {
                return request.getHeader(ipHead).split(",")[0];
            }
        }
        return "0:0:0:0:0:0:0:1".equals(request.getRemoteAddr()) ? "127.0.0.1" : request.getRemoteAddr();
    }

    private boolean checkIP(String ip) {
        return !(null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip));
    }

    /**
     * 获取当前执行的方法
     *
     * @param joinPoint  连接点
     * @param methodName 方法名称
     * @return 方法
     */
    private Method currentMethod(ProceedingJoinPoint joinPoint, String methodName) {
        /**
         * 获取目标类的所有方法，找到当前要执行的方法
         */
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }
}
