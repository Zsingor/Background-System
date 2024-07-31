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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
@Transactional
public class RolesServiceImpl implements RolesService {
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private RolesRoutesMapper rolesRoutesMapper;
    @Autowired
    private UserRolesMapper userRolesMapper;

    @Override
    public void rolesadd(Roles roles) {
        roles.setId(UUIDUtils.getUUID());
        Date createtime=new Date(System.currentTimeMillis());
        roles.setCreateTime(createtime);
        rolesMapper.rolesadd(roles);
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
    public void rolesdelete(List<String> rolelist) {
        userRolesMapper.deleteRolesUsers(rolelist);
        rolesRoutesMapper.deleteRolesRoutes(rolelist);
        rolesMapper.rolesdelete(rolelist);
    }

    @Override
    public void rolesupdate(Roles roles) {
        rolesMapper.rolesupdate(roles);
    }

    @Override
    public void rolesAssignRoute(Roles roles) {
        List<String> routeList=roles.getRoutesid();
        if(Objects.equals(roles.getId(), "1"))
        {
            List<String> whitelist=new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
            Set<String> resultSet = new HashSet<>(routeList);
            resultSet.addAll(whitelist);
            List<String> resultList = new ArrayList<>(resultSet);
            rolesRoutesMapper.deleteRoleRoutes(roles);
            rolesRoutesMapper.addRoleRoutes(roles.getId(),resultList,"1");
        }
        else {
            rolesRoutesMapper.deleteRoleRoutes(roles);
            rolesRoutesMapper.addRoleRoutes(roles.getId(),routeList,"1");
        }
    }

    @Override
    public void rolesAssignAuthority(Roles roles) {
        List<String> routeList=roles.getRoutesid();
        if(Objects.equals(roles.getId(), "1"))
        {
            List<String> whitelist=new ArrayList<>(Arrays.asList("6","7"));
            Set<String> resultSet = new HashSet<>(routeList);
            resultSet.addAll(whitelist);
            List<String> resultList = new ArrayList<>(resultSet);
            rolesRoutesMapper.deleteAuthority(roles);
            rolesRoutesMapper.addRoleRoutes(roles.getId(),resultList,"2");
        }
        else {
            rolesRoutesMapper.deleteAuthority(roles);
            rolesRoutesMapper.addRoleRoutes(roles.getId(),routeList,"2");
        }
    }
}
