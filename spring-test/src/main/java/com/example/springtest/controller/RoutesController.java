package com.example.springtest.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Result;
import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;
import com.example.springtest.service.RoutesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
public class RoutesController {

    @Autowired
    private RoutesService routesService;

    @PostMapping("/routes/query")
    public Result routesquery(@RequestBody User user)
    {
        try {
            List<Routes> data=routesService.routesquery(user);
            return Result.success(data);
        }catch (Exception e){
            return Result.error("查询失败");
        }
    }

    @PostMapping("/routes/all")
    public Result routesall()
    {
        try {
            List<Routes> data=routesService.routesall();
            return Result.success(data);
        }catch (Exception e){
            return Result.error("查询失败");
        }
    }

    @PostMapping("/routes/parents")
    public Result routesparents()
    {
        try {
            List<Routes> data=routesService.routesParentquery();
            return Result.success(data);
        }catch (Exception e){
            return Result.error("查询失败");
        }
    }

    @PostMapping("/routes/add")
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

    @PostMapping("/routes/update")
    public Result routesupdate(@RequestBody Routes routes)
    {
        int flag=routesService.routesupdate(routes);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("修改失败");
        }
    }

    @PostMapping("/routes/delete")
    public Result routesdelete(@RequestBody List<String> menulist)
    {
        int flag=routesService.routesdelete(menulist);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("删除失败");
        }
    }

    @GetMapping("/routes/get/{name}")
    public Result gettest(@PathVariable(value = "name") String name)
    {
        System.out.println(name);
        return Result.success();
    }
}
