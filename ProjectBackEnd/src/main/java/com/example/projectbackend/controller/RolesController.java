package com.example.projectbackend.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.projectbackend.aop.authorize.PreAuthorize;
import com.example.projectbackend.aop.logs.AutoLog;
import com.example.projectbackend.entity.Result;
import com.example.projectbackend.entity.Roles;
import com.example.projectbackend.service.RolesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesService rolesService;


    //添加角色
    @AutoLog(module = "角色管理",operate = "添加角色")
    @PostMapping("/add")
    @PreAuthorize("/roles/add")
    public Result rolesadd(@RequestBody Roles roles)
    {
        rolesService.rolesadd(roles);
        return Result.success();
    }


    //查询所有角色
    @PostMapping("/queryAll")
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
    @PostMapping("/query")
    public Result rolesquery(@RequestBody String json)
    {
        try {
            JSONObject data=rolesService.rolesquery(json);
            return Result.success(data);
        }
        catch (Exception error){
            return Result.error("获取数据失败:"+error);
        }
    }


    //删除角色
    @AutoLog(module = "角色管理",operate = "删除角色")
    @PostMapping("/delete")
    @PreAuthorize("/roles/delete")
    public Result rolesdelete(@RequestBody List<String> rolelist)
    {
        List<String> whitelist=new ArrayList<>(List.of("1"));
        rolelist.removeAll(whitelist);
        if(rolelist.isEmpty())
        {
            return Result.error("无法删除超级管理员");
        }
        rolesService.rolesdelete(rolelist);
        return Result.success();
    }


    //更新角色信息
    @AutoLog(module = "角色管理",operate = "更新角色信息")
    @PostMapping("/update")
    @PreAuthorize("/roles/update")
    public Result rolesupdate(@RequestBody Roles roles)
    {
        rolesService.rolesupdate(roles);
        return Result.success();
    }

    //角色路由添加
    @AutoLog(module = "角色管理",operate = "更新角色路由")
    @PostMapping("/assignRoute")
    @PreAuthorize("/roles/assignRoute")
    public Result rolesAssignRoute(@RequestBody Roles roles)
    {
        rolesService.rolesAssignRoute(roles);
        return Result.success();
    }

    //角色权限添加
    @AutoLog(module = "角色管理",operate = "更新角色权限")
    @PostMapping("/assignAuthority")
    @PreAuthorize("/roles/assignAuthority")
    public Result rolesAssignAuthority(@RequestBody Roles roles)
    {
        rolesService.rolesAssignAuthority(roles);
        return Result.success();
    }
}
