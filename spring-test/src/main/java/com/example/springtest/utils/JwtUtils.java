package com.example.springtest.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET = "scutkclosh";

    public static String generateJWT(Map<String, Object> claims){
        Calendar instance = Calendar.getInstance();
        // 默认1天过期
        instance.add(Calendar.DATE, 1);

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        // payload
        claims.forEach((k, v) -> {
            builder.withClaim(k, (String) v);
        });

        String token = builder.withExpiresAt(instance.getTime())  //指定令牌过期时间
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }

    //解析jwt令牌
    public static DecodedJWT parseJWT(String token)
    {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }
}
