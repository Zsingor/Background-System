package com.example.springtest.aop.logs;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springtest.entity.Logs;
import com.example.springtest.entity.Result;
import com.example.springtest.service.LogsService;
import com.example.springtest.utils.JwtUtils;
import com.example.springtest.utils.UUIDUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@Aspect
//处理AOP的处理器
public class LogAspect {
    @Autowired
    private LogsService logsService;

    @Around("@annotation(autoLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, AutoLog autoLog)throws Throwable{
        try {
            //执行具体的接口
            Result result=(Result) joinPoint.proceed();

            String className=joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String params = Arrays.toString(joinPoint.getArgs());

            String details="[类名]:"+className+"  [方法]:"+methodName+" [参数]:"+params;

            String id= UUIDUtils.getUUID();

            //操作模块
            String module=autoLog.module();

            //操作内容
            String operate=autoLog.operate();

            //操作时间
            Date operatetime=new Date(System.currentTimeMillis());

            HttpServletRequest request= ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            //操作ip
            String ip=getIp(request);

            //操作姓名
            String token="";
            if(!Objects.equals(methodName, "userlogin"))
            {
                token = request.getHeader("token");
            }
            else
            {
                JSONObject data=(JSONObject) result.getData();
                token=data.getString("token");
            }
            DecodedJWT verify=JwtUtils.parseJWT(token);
            String userid=verify.getClaim("user_id").toString().replace("\"", "");
            String username=verify.getClaim("user_name").toString().replace("\"", "");

            Logs logs=new Logs(id,userid,username,module,operate,details,ip,operatetime,null,null);
            logsService.logsadd(logs);

            return result;
        }
        catch (Exception e)
        {
            return Result.error(e.toString());
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
        /*
          获取目标类的所有方法，找到当前要执行的方法
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
