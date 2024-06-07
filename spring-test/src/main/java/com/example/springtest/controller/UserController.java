package com.example.springtest.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Form;
import com.example.springtest.entity.Result;
import com.example.springtest.entity.User;
import com.example.springtest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //用户登录
    @PostMapping("/user/login")
    public Result userlogin(@RequestBody User user)
    {
        try {
            JSONObject data=userService.userlogin(user);
            return Result.success(data);
        }catch (Exception e){
            return Result.error("查询失败");
        }
    }

    //用户添加
    @PostMapping("/user/add")
    public Result useradd(@RequestBody User user)
    {
        int flag=userService.useradd(user);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("添加失败");
        }
    }

    //用户查询
    @PostMapping("/user/query")
    public Result userquery(@RequestBody Form form)
    {
        try {
            JSONObject data=userService.userquery(form);
            return Result.success(data);
        }
        catch (Exception error){
            return Result.error("获取数据失败:"+error);
        }
    }

    //用户删除
    @PostMapping("/user/delete")
    public Result userdelete(@RequestBody User user)
    {
        int flag=userService.userdelete(user);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("删除失败");
        }
    }

    //用户修改
    @PostMapping("/user/update")
    public Result update(@RequestBody User user)
    {
        int flag=userService.userupdate(user);
        if(flag==1)
        {
            return Result.success();
        }
        else
        {
            return Result.error("修改失败");
        }
    }

    //用户路由添加
    @PostMapping("/user/addroute")
    public Result useraddroute(@RequestBody User user)
    {
        int flag=userService.useraddroute(user);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("添加失败");
        }
    }
}
