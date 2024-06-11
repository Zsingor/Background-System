package com.example.springtest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Roles;
import com.example.springtest.mapper.RolesMapper;
import com.example.springtest.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RolesServiceImpl implements RolesService {
    @Autowired
    private RolesMapper rolesMapper;

    @Override
    public int rolesadd(Roles roles) {
        try {
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
    public JSONObject rolesquery(Roles roles) {
        List<Roles> res;
        List<Roles> data=rolesMapper.rolesquery(roles);
        int rowSum=data.size();
        int start = (roles.getCurrentpage() - 1) * roles.getPagesize();
        int end = roles.getCurrentpage() * roles.getPagesize();
        if(end<=rowSum)
        {
            res=data.subList(start,end);
        }
        else {
            res=data.subList(start,rowSum);
        }
        JSONObject response = new JSONObject();
        response.put("rowSum", rowSum);
        response.put("roleslist", res);
        return response;
    }

    @Override
    public int rolesdelete(Roles roles) {
        try {
            rolesMapper.rolesdelete(roles);
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
            List<String> whitelist=new ArrayList<>(Arrays.asList("1", "2", "3", "4"));

            List<String> routeList=roles.getRoutesIdAsList();
            Set<String> resultSet = new HashSet<>(routeList);
            resultSet.addAll(whitelist);
            List<String> resultList = new ArrayList<>(resultSet);

            roles.setRoutesIdFromList(resultList);
            rolesMapper.rolesAssignRoute(roles);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }
}
