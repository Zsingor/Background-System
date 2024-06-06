package com.example.springtest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;
import com.example.springtest.mapper.RoutesMapper;
import com.example.springtest.mapper.UserMapper;
import com.example.springtest.service.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RoutesServiceImpl implements RoutesService {
    @Autowired
    private RoutesMapper routesMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject routesquery(User user) {
        User user1 = userMapper.userRoutesquery(user);

        List<Routes> routesList=routesMapper.routesquery(user1.getRoutesIdAsList());

        List<Routes> menuList = Routeprocess(routesList);
        JSONObject response = new JSONObject();
        response.put("menuList", menuList);
        response.put("user_name", user.getName());
        return response;
    }

    @Override
    public List<Routes> routesall() {
        List<Routes> routesList=routesMapper.routesAllquery();
        return Routeprocess(routesList);
    }

    @Override
    public List<Routes> routesParentquery() {
        List<Routes> routesList=routesMapper.routesParentquery();
        return Routeprocess(routesList);
    }

    @Override
    public int routesadd(Routes routes) {
        try {
            List<Routes> routesall = routesMapper.routesAllquery();
            String name = routes.getName();
            if (routesall.stream().anyMatch(route -> Objects.equals(route.getName(), name))) {
                return 2;
            }
            String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            Random random = new Random();
            StringBuffer sb;
            //为了确保生成的id为唯一值
            while (true) {
                sb = new StringBuffer();
                for (int i = 0; i < 8; i++) {
                    int number = random.nextInt(62);
                    sb.append(str.charAt(number));
                }
                StringBuffer finalSb = sb;
                if (routesall.stream().noneMatch(route -> Objects.equals(route.getId(), finalSb.toString()))) {
                    break;
                }
            }
            routes.setId(sb.toString());
            routesMapper.routesadd(routes);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    @Override
    public int routesupdate(Routes routes) {
        try {
            routesMapper.routesupdate(routes);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    @Override
    public int routesdelete(List<String> menulist) {
        try {
            routesMapper.routesdelete(menulist);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    public List<Routes> Routeprocess(List<Routes> routesList){
        // 分类一级路由和二级路由
        List<Routes> parentRoutes = routesList.stream()
                .filter(route -> route.getLevel() == 1)
                .collect(Collectors.toList());

        List<Routes> childRoutes = routesList.stream()
                .filter(route -> route.getLevel() == 2)
                .collect(Collectors.toList());

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
