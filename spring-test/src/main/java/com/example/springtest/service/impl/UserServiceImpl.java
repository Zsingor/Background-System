package com.example.springtest.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Roles;
import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;
import com.example.springtest.mapper.RolesMapper;
import com.example.springtest.mapper.RoutesMapper;
import com.example.springtest.mapper.UserMapper;
import com.example.springtest.service.UserService;
import com.example.springtest.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoutesMapper routesMapper;
    @Autowired
    private RolesMapper rolesMapper;


    @Override
    public JSONObject userlogin(User user) {
        User user1 = userMapper.userPrimaryquery(user);

        Roles roles=rolesMapper.rolesqueryPrimary(user1.getRoleid());
        List<Routes> routesList=routesMapper.routesquery(roles.getRoutesIdAsList());

        List<Routes> menuList = RoutesServiceImpl.Routeprocess(routesList);

        Map<String, Object> claims=new HashMap<>();
        claims.put("user_name",user.getName());
        String token= JwtUtils.generateJWT(claims);

        JSONObject response = new JSONObject();
        response.put("menuList", menuList);
        response.put("user_name", user.getName());
        response.put("token", token);
        return response;
    }

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
    public JSONObject userquery(User user) {
        List<User> data=userMapper.userquery(user);
        List<User> res;
        int rowSum=data.size();
        int start = (user.getCurrentpage() - 1) * user.getPagesize();
        int end = user.getCurrentpage() * user.getPagesize();
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
}
