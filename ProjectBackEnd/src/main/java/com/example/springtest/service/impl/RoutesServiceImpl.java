package com.example.springtest.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Roles;
import com.example.springtest.entity.Routes;
import com.example.springtest.mapper.RolesRoutesMapper;
import com.example.springtest.mapper.RoutesMapper;
import com.example.springtest.service.RoutesService;
import com.example.springtest.utils.QueryResult;
import com.example.springtest.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoutesServiceImpl implements RoutesService {
    @Autowired
    private RoutesMapper routesMapper;
    @Autowired
    private RolesRoutesMapper rolesRoutesMapper;

    @Override
    public List<Routes> routesquery(Roles roles) {
        List<String> routesIds=rolesRoutesMapper.queryRoleRoutes(roles);
        if(routesIds.isEmpty()){
            return Collections.emptyList();
        }
        List<Routes> routesList=routesMapper.routesquery(routesIds);

        return Routeprocess(routesList);
    }

    @Override
    public JSONObject routesall(String json) {
        JSONObject jsonObject= JSON.parseObject(json);
        Routes routes=jsonObject.getObject("queryForm",Routes.class);
        int currentPage=jsonObject.getInteger("currentPage");
        int pageSize=jsonObject.getInteger("pageSize");
        List<Routes> routesList=routesMapper.routesAllquery(routes);
        List<Routes> data=Routeprocess(routesList);
        return QueryResult.getResult(data,currentPage,pageSize);
    }

    @Override
    public List<Routes> routesParentquery() {
        List<Routes> routesList=routesMapper.routesParentquery();
        return Routeprocess(routesList);
    }

    @Override
    public int routesadd(Routes routes) {
        Routes newr=new Routes();
        List<Routes> routesall = routesMapper.routesAllquery(newr);
        //判断菜单的唯一标识和url是否已存在
        String name = routes.getName();
        String parentId=routes.getParentid();
        String path=routes.getPath();
        String type=routes.getType();
        for(Routes temp : routesall)
        {
            if (Objects.equals(temp.getName(), name)) {
                return 2;
            }
            else if(Objects.equals(temp.getParentid(), parentId) &&
                    Objects.equals(temp.getPath(), path) &&
                    Objects.equals(temp.getType(), type))
            {
                return 3;
            }
        }
        //新增菜单
        Date createtime=new Date(System.currentTimeMillis());
        routes.setCreateTime(createtime);
        routes.setId(UUIDUtils.getUUID());
        routesMapper.routesadd(routes);
        return 1;
    }

    @Override
    public void routesupdate(Routes routes) {
        routesMapper.routesupdate(routes);
    }

    @Override
    public void routesdelete(List<String> menulist) {
        rolesRoutesMapper.deleteRoutesRoles(menulist);
        routesMapper.routesdelete(menulist);
    }

    private List<Routes> Routeprocess(List<Routes> routesList){
        // 分类一级路由和二级路由
        List<Routes> parentRoutes = routesList.stream()
                .filter(route -> route.getLevel() == 1)
                .collect(Collectors.toList());

        List<Routes> childRoutes = routesList.stream()
                .filter(route -> route.getLevel() == 2)
                .toList();

        // 确保每一条子路由都有父路由
        for (Routes childRoute : childRoutes) {
            // 检查childRoute的parentId是否在parentRoutes的id中存在
            if (parentRoutes.stream()
                    .anyMatch(parent -> parent.getId().equals(childRoute.getParentid()))) {
                // 如果找到了匹配的父路由，则继续
                continue;
            }
            Routes parent=routesMapper.routesqueryById(childRoute.getParentid());
            parentRoutes.add(parent);
        }

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
