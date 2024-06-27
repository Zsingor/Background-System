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
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoutesMapper routesMapper;
    @Autowired
    private RolesRoutesMapper rolesRoutesMapper;
    @Autowired
    private UserRolesMapper userRolesMapper;


    //用户登录
    @Override
    public JSONObject userlogin(User user) {
        User user1 = userMapper.userPrimaryquery(user);
        JSONObject response = new JSONObject();
        //如果当前用户不存在或者密码错误
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
            List<Routes> menuList=new ArrayList<Routes>();
            //拿到用户的所有角色信息
            List<String> roleLists=userRolesMapper.queryUserRoles(user1.getId());
            if(!roleLists.isEmpty())
            {
                //拿到用户拥有的所有路由
                List<String> routesIds=rolesRoutesMapper.queryRolesRoutes(roleLists,"1");
                //列表去重
                routesIds=routesIds.stream().distinct().collect(Collectors.toList());
                if(!routesIds.isEmpty())
                {
                    List<Routes> routesList=routesMapper.routesquery(routesIds);
                    menuList = Routeprocess(routesList);
                }
            }


            Map<String, Object> claims=new HashMap<>();
            claims.put("user_id",user1.getId());
            claims.put("user_name",user1.getName());
            String token= JwtUtils.generateJWT(claims);

            response.put("menuList", menuList);
            response.put("user_name", user1.getName());
            response.put("user_id", user1.getId());
            response.put("token", token);
        }
        return response;
    }

    @Override
    public List<User> queryMessageUser() {
        return userMapper.queryMessageUser();
    }


    @Override
    public User userQueryMsssage(User user) {
        return userMapper.userPrimaryquery(user);
    }

    // 用户注册
    @Override
    public int useradd(User user) {
        try {
            user.setId(UUIDUtils.getUUID());
            userMapper.useradd(user);
            userAssignRole(user);
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
        //列表去重
        data=data.stream().distinct().collect(Collectors.toList());

        return QueryResult.getResult(data,currentPage,pageSize);
    }

    // 用户删除
    @Override
    public int userdelete(List<String> userlist) {
        try {
            userRolesMapper.deleteUsersRoles(userlist);
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
            userAssignRole(user);
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

    //查询用户的角色
    @Override
    public List<String> queryUserRoles(String userid) {
        return userRolesMapper.queryUserRoles(userid);
    }

    //获得用户拥有的权限
    @Override
    public List<String> queryUserAuthority(String userid) {
        //拿到用户的所有角色信息
        List<String> roleLists=userRolesMapper.queryUserRoles(userid);
        //拿到用户拥有的所有权限
        List<String> routesIds=rolesRoutesMapper.queryRolesRoutes(roleLists,"2");
        //列表去重
        routesIds=routesIds.stream().distinct().collect(Collectors.toList());

        List<Routes> authorityList=routesMapper.queryAuthority(routesIds);

        List<String> result=RoutesServiceImpl.authorityProcess(authorityList);
        return result;
    }

    public static List<Routes> Routeprocess(List<Routes> routesList){
        // 分类一级路由和二级路由
        List<Routes> parentRoutes = routesList.stream()
                .filter(route -> route.getLevel() == 1)
                .collect(Collectors.toList());

        List<Routes> childRoutes = routesList.stream()
                .filter(route -> route.getLevel() == 2)
                .toList();

        // 构建一级路由的 children
        for (Routes route : parentRoutes) {
            List<Routes> children = childRoutes.stream()
                    .filter(r -> Objects.equals(r.getParentid(), route.getId()))
                    .collect(Collectors.toList());
            route.setChildren(children);
        }

        return parentRoutes;
    }
}
