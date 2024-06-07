package com.example.springtest;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springtest.entity.User;
import com.example.springtest.mapper.UserMapper;
import com.example.springtest.service.RoutesService;
import com.example.springtest.service.UserService;
import com.example.springtest.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class SpringTestApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoutesService routesService;

    @Test
    void contextLoads() {
        Map<String, Object> claims=new HashMap<>();
        claims.put("username","user11");
        String token= JwtUtils.generateJWT(claims);
        System.out.println("token"+token);
        try {
            // 验证令牌
            DecodedJWT verify=JwtUtils.parseJWT(token);
            System.out.println(verify.getClaim("username"));
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            System.out.println("1");
        }catch (TokenExpiredException e){
            e.printStackTrace();
            System.out.println("1");
        }catch (AlgorithmMismatchException e){
            e.printStackTrace();
            System.out.println("1");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("1");
        }
    }

}
