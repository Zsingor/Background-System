package com.example.springtest.service;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Roles;

import java.util.List;

public interface RolesService {
    int rolesadd(Roles roles);

    List<Roles> rolesqueryAll();

    JSONObject rolesquery(String json);

    int rolesdelete(List<String> rolelist);

    int rolesupdate(Roles roles);

    int rolesAssignRoute(Roles roles);

    int rolesAssignAuthority(Roles roles);
}