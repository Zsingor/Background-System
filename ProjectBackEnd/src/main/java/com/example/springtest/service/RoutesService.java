package com.example.springtest.service;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Roles;
import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;

import java.util.List;

public interface RoutesService {

    List<Routes> routesquery(Roles roles);

    List<Routes> routesall(Routes routes);

    List<Routes> routesParentquery();

    int routesadd(Routes routes);

    void routesupdate(Routes routes);

    void routesdelete(List<String> menulist);
}
