package com.example.springtest.service.impl;

import com.example.springtest.entity.Roles;
import com.example.springtest.entity.Routes;
import com.example.springtest.mapper.RolesRoutesMapper;
import com.example.springtest.mapper.RoutesMapper;
import com.example.springtest.service.RoutesService;
import com.example.springtest.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoutesServiceImpl implements RoutesService {
    @Autowired
    private RoutesMapper routesMapper;
    @Autowired
    private RolesRoutesMapper rolesRoutesMapper;

    @Override
    public List<Routes> routesquery(Roles roles) {
        List<String> routesIds=rolesRoutesMapper.queryRoleRoutes(roles);
        List<Routes> routesList=routesMapper.routesquery(routesIds);

        return Routeprocess(routesList);
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
            routes.setId(UUIDUtils.getUUID());
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
            rolesRoutesMapper.deleteRouteRoles(menulist);
            routesMapper.routesdelete(menulist);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    public static List<Routes> Routeprocess(List<Routes> routesList){
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
