package com.example.springtest.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Roles;
import com.example.springtest.mapper.RolesMapper;
import com.example.springtest.mapper.RolesRoutesMapper;
import com.example.springtest.mapper.UserMapper;
import com.example.springtest.mapper.UserRolesMapper;
import com.example.springtest.service.RolesService;
import com.example.springtest.utils.QueryResult;
import com.example.springtest.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RolesServiceImpl implements RolesService {
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RolesRoutesMapper rolesRoutesMapper;
    @Autowired
    private UserRolesMapper userRolesMapper;

    @Override
    public int rolesadd(Roles roles) {
        try {
            roles.setId(UUIDUtils.getUUID());
            rolesMapper.rolesadd(roles);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    @Override
    public List<Roles> rolesqueryAll() {
        return rolesMapper.rolesqueryAll();
    }

    @Override
    public JSONObject rolesquery(String json) {
        JSONObject jsonObject= JSON.parseObject(json);
        Roles roles=jsonObject.getObject("queryForm",Roles.class);
        int currentPage=jsonObject.getInteger("currentPage");
        int pageSize=jsonObject.getInteger("pageSize");
        List<Roles> data=rolesMapper.rolesquery(roles);
        return QueryResult.getResult(data,currentPage,pageSize);
    }

    @Override
    public int rolesdelete(List<String> rolelist) {
        try {
            userRolesMapper.deleteRolesUsers(rolelist);
            rolesRoutesMapper.deleteRolesRoutes(rolelist);
            rolesMapper.rolesdelete(rolelist);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    @Override
    public int rolesupdate(Roles roles) {
        try {
            rolesMapper.rolesupdate(roles);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    @Override
    public int rolesAssignRoute(Roles roles) {
        try {
            List<String> routeList=roles.getRoutesid();
            if(Objects.equals(roles.getId(), "1"))
            {
                List<String> whitelist=new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
                Set<String> resultSet = new HashSet<>(routeList);
                resultSet.addAll(whitelist);
                List<String> resultList = new ArrayList<>(resultSet);
                rolesRoutesMapper.deleteRoleRoutes(roles);
                rolesRoutesMapper.addRoleRoutes(roles.getId(),resultList);
            }
            else {
                rolesRoutesMapper.deleteRoleRoutes(roles);
                rolesRoutesMapper.addRoleRoutes(roles.getId(),routeList);
            }
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }
}
