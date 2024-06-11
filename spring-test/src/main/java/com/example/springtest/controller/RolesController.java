package com.example.springtest.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Result;
import com.example.springtest.entity.Roles;
import com.example.springtest.service.RolesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class RolesController {

    @Autowired
    private RolesService rolesService;

    @PostMapping("/roles/add")
    public Result rolesadd(@RequestBody Roles roles)
    {
        int flag=rolesService.rolesadd(roles);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("添加失败");
        }
    }

    @PostMapping("/roles/queryAll")
    public Result rolesqueryAll()
    {
        try {
            List<Roles> data=rolesService.rolesqueryAll();
            return Result.success(data);
        }catch (Exception e){
            return Result.error("查询失败");
        }
    }

    //角色查询
    @PostMapping("/roles/query")
    public Result rolesquery(@RequestBody Roles roles)
    {
        System.out.println(roles);
        try {
            JSONObject data=rolesService.rolesquery(roles);
            return Result.success(data);
        }
        catch (Exception error){
            return Result.error("获取数据失败:"+error);
        }
    }

    @PostMapping("/roles/delete")
    public Result rolesdelete(@RequestBody Roles roles)
    {
        int flag=rolesService.rolesdelete(roles);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("删除失败");
        }
    }

    @PostMapping("/roles/update")
    public Result rolesupdate(@RequestBody Roles roles)
    {
        System.out.println(roles);
        int flag=rolesService.rolesupdate(roles);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("修改失败");
        }
    }

    //角色路由添加
    @PostMapping("/roles/assignRoute")
    public Result rolesAssignRoute(@RequestBody Roles roles)
    {
        int flag=rolesService.rolesAssignRoute(roles);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("添加失败");
        }
    }
}
