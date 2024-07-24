package com.example.springtest.service;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Roles;

import java.util.List;

public interface RolesService {
    void rolesadd(Roles roles);

    List<Roles> rolesqueryAll();

    JSONObject rolesquery(String json);

    void rolesdelete(List<String> rolelist);

    void rolesupdate(Roles roles);

    void rolesAssignRoute(Roles roles);

    void rolesAssignAuthority(Roles roles);
}
