package com.example.springtest.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.aop.authorize.PreAuthorize;
import com.example.springtest.aop.logs.AutoLog;
import com.example.springtest.entity.Result;
import com.example.springtest.entity.Roles;
import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;
import com.example.springtest.service.RoutesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/routes")
public class RoutesController {

    @Autowired
    private RoutesService routesService;

    //路由查询
    @PostMapping("/query")
    public Result routesquery(@RequestBody Roles roles)
    {
        try {
            List<Routes> data=routesService.routesquery(roles);
            return Result.success(data);
        }catch (Exception e){
            return Result.error("查询失败");
        }
    }

    //查询全部路由
    @PostMapping("/queryAll")
    public Result routesqueryAll(@RequestBody String json)
    {
        try {
            JSONObject data=routesService.routesall(json);
            return Result.success(data);
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //查询所有路由的父菜单
    @PostMapping("/parents")
    public Result routesparents()
    {
        try {
            List<Routes> data=routesService.routesParentquery();
            return Result.success(data);
        }catch (Exception e){
            return Result.error("查询失败");
        }
    }

    //添加路由
    @AutoLog(module = "菜单管理",operate = "添加菜单")
    @PostMapping("/add")
    @PreAuthorize("/routes/add")
    public Result routesadd(@RequestBody Routes routes)
    {
        int flag=routesService.routesadd(routes);
        if (flag==1)
        {
            return Result.success();
        }
        else if(flag==2)
        {
            System.out.println("错误");
            return Result.error("该标识已存在");
        }
        else {
            return Result.error("添加失败");
        }
    }


    //更新路由信息
    @AutoLog(module = "菜单管理",operate = "更新菜单信息")
    @PostMapping("/update")
    @PreAuthorize("/routes/update")
    public Result routesupdate(@RequestBody Routes routes)
    {
        routesService.routesupdate(routes);
        return Result.success();
    }


    //删除路由
    @AutoLog(module = "菜单管理",operate = "删除菜单")
    @PostMapping("/delete")
    @PreAuthorize("/routes/delete")
    public Result routesdelete(@RequestBody List<String> menulist)
    {
        List<String> whitelist=new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
        menulist.removeAll(whitelist);
        if(menulist.isEmpty())
        {
            return Result.error("无法删除初始路由");
        }
        routesService.routesdelete(menulist);
        return Result.success();
    }

    @GetMapping("/get/{name}")
    public Result gettest(@PathVariable(value = "name") String name)
    {
        System.out.println(name);
        return Result.success();
    }
}
