package com.example.springtest.service;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;

import java.util.List;

public interface RoutesService {

    List<Routes> routesall();

    List<Routes> routesParentquery();

    int routesadd(Routes routes);

    int routesupdate(Routes routes);

    int routesdelete(List<String> menulist);
}
