package com.example.projectbackend.service;

import com.alibaba.fastjson.JSONObject;
import com.example.projectbackend.entity.Roles;
import com.example.projectbackend.entity.Routes;

import java.util.List;

public interface RoutesService {

    List<Routes> routesquery(Roles roles);

    JSONObject routesall(String json);

    List<Routes> routesParentquery();

    int routesadd(Routes routes);

    void routesupdate(Routes routes);

    void routesdelete(List<String> menulist);
}
