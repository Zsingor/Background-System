package com.example.springtest;

import com.example.springtest.entity.User;
import com.example.springtest.mapper.UserMapper;
import com.example.springtest.service.RoutesService;
import com.example.springtest.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        User user=new User();
        //List<String> arr= Arrays.asList("1", "2", "3", "4", "5");
        user.setName("user11");
        //user.setRoutesIdFromList(arr);
//        List<String> abs=user.getRoutesIdAsList();
//        abs.add("3");
//        user.setRoutesIdFromList(abs);
        //userMapper.userroutesupdate(user);
        //System.out.println(user.getRoutesIdAsList());

        routesService.routesquery(user);
    }

}
