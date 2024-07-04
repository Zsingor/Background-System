package com.example.springtest;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springtest.entity.Message;
import com.example.springtest.entity.User;
import com.example.springtest.mapper.MessageMapper;
import com.example.springtest.mapper.RoutesMapper;
import com.example.springtest.mapper.UserMapper;
import com.example.springtest.service.RoutesService;
import com.example.springtest.service.UserService;
import com.example.springtest.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.*;

@SpringBootTest
class SpringTestApplicationTests {

    @Test
    void contextLoads() {

    }

}
