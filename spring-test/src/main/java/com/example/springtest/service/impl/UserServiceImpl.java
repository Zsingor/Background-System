package com.example.springtest.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springtest.aop.logs.AutoLog;
import com.example.springtest.entity.Roles;
import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;
import com.example.springtest.mapper.*;
import com.example.springtest.service.UserService;
import com.example.springtest.utils.JwtUtils;
import com.example.springtest.utils.QueryResult;
import com.example.springtest.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoutesMapper routesMapper;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private RolesRoutesMapper rolesRoutesMapper;
    @Autowired
    private UserRolesMapper userRolesMapper;


    @Override
    public JSONObject userlogin(User user) {
        User user1 = userMapper.userPrimaryquery(user);
        JSONObject response = new JSONObject();
        if(user1==null)
        {
            response.put("token", "0");
        }
        else if(!user1.getPassword().equals(user.getPassword()))
        {
            response.put("token", "1");
        }
        else
        {
            Roles roles=rolesMapper.rolesqueryPrimary(user1.getRoleid());

            List<String> routesIds=rolesRoutesMapper.queryRoleRoutes(roles);
            List<Routes> menuList=new ArrayList<Routes>();
            if(!routesIds.isEmpty())
            {
                List<Routes> routesList=routesMapper.routesquery(routesIds);
                menuList = RoutesServiceImpl.Routeprocess(routesList);
            }

            Map<String, Object> claims=new HashMap<>();
            claims.put("user_id",user1.getId());
            claims.put("user_name",user1.getName());
            claims.put("user_role",roles.getName());
            claims.put("role_id",roles.getId());
            String token= JwtUtils.generateJWT(claims);

            response.put("menuList", menuList);
            response.put("user_name", user.getName());
            response.put("role_id", roles.getId());
            response.put("token", token);
        }
        return response;
    }

    // 用户注册
    @Override
    public int useradd(User user) {
        try {
            user.setId(UUIDUtils.getUUID());
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
    public JSONObject userquery(String json) {
        //根据json解析出具体的值
        JSONObject jsonObject= JSON.parseObject(json);
        User user=jsonObject.getObject("queryForm",User.class);
        int currentPage=jsonObject.getInteger("currentPage");
        int pageSize=jsonObject.getInteger("pageSize");

        List<User> data=userMapper.userquery(user);

        return QueryResult.getResult(data,currentPage,pageSize);
    }

    // 用户删除
    @Override
    public int userdelete(List<String> userlist) {
        try {
            userMapper.userdelete(userlist);
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

    @Override
    public int userAssignRole(User user) {
        try {
            List<String> roleList=user.getRolesid();
            if(Objects.equals(user.getId(), "1"))
            {
                List<String> whitelist=new ArrayList<>(List.of("1"));
                Set<String> resultSet = new HashSet<>(roleList);
                resultSet.addAll(whitelist);
                List<String> resultList = new ArrayList<>(resultSet);
                user.setRolesid(resultList);
                userRolesMapper.deleteUserRoles(user);
                userRolesMapper.addUserRoles(user.getId(),resultList);
            }
            else {
                userRolesMapper.deleteUserRoles(user);
                userRolesMapper.addUserRoles(user.getId(),roleList);
            }
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }
}
