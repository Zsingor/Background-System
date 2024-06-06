package com.example.springtest.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Form;
import com.example.springtest.entity.User;
import com.example.springtest.mapper.RoutesMapper;
import com.example.springtest.mapper.UserMapper;
import com.example.springtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoutesMapper routesMapper;

    // 用户注册
    @Override
    public int useradd(User user) {
        try {
            userMapper.useradd(user);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    // 用户查询
    @Override
    public JSONObject userquery(Form form) {
        User user=form.getQueryData();
        List<User> data=userMapper.userquery(user);
        List<User> res;
        int rowSum=data.size();
        int start = (form.getCurrentpage() - 1) * form.getPagesize();
        int end = form.getCurrentpage() * form.getPagesize();
        if(end<=rowSum)
        {
            res=data.subList(start,end);
        }
        else {
            res=data.subList(start,rowSum);
        }
        JSONObject response = new JSONObject();
        response.put("rowSum", rowSum);
        response.put("userlist", res);
        return response;
    }

    // 用户删除
    @Override
    public int userdelete(User user) {
        try {
            userMapper.userdelete(user);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }


    // 用户更新
    @Override
    public int userupdate(User user) {
        try {
            userMapper.userupdate(user);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    //用户路由添加
    public int useraddroute(User user) {
        try {
            userMapper.userroutesupdate(user);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }
}
