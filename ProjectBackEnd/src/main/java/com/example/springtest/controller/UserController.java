package com.example.springtest.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.aop.authorize.PreAuthorize;
import com.example.springtest.aop.logs.AutoLog;
import com.example.springtest.entity.Result;
import com.example.springtest.entity.Roles;
import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;
import com.example.springtest.service.UserService;
import com.example.springtest.utils.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired

    private UserService userService;

    //用户登录
    @AutoLog(module = "用户管理",operate = "用户登录")
    @PostMapping("/login")
    public Result userlogin(@RequestBody User user) throws Exception {
        String pas= RsaUtils.decrypt(user.getPassword());
        user.setPassword(pas);
        JSONObject data=userService.userlogin(user);
        if(data.get("token")=="0")
        {
            return Result.error("当前用户不存在");
        }
        else if(data.get("token")=="1")
        {
            return Result.error("用户名或密码错误");
        }
        return Result.success(data);
    }

    //密码校验
    @PostMapping("/checkpwd")
    public Result userCheckpwd(@RequestBody User user)
    {
        try {
            String pas= RsaUtils.decrypt(user.getPassword());
            user.setPassword(pas);
            int flag=userService.userCheckpwd(user);
            if(flag==1)
            {
                return Result.success();
            }
           else
            {
                return Result.error("密码错误");
            }
        }catch (Exception e){
            return Result.error("密码错误");
        }
    }

    //退出登录
    @AutoLog(module = "用户管理",operate = "退出登录")
    @PostMapping("/logout")
    public Result userlogout()
    {
        return Result.success();
    }

    //用户申请账号
    @PostMapping("/register")
    public Result userRegister(@RequestBody User user) throws Exception {
        String pas= RsaUtils.decrypt(user.getPassword());
        user.setPassword(pas);
        user.setType(0);
        user.setStatus("1");
        int flag=userService.useradd(user);
        if (flag==1)
        {
            return Result.success();
        }
        else if (flag==2)
        {
            return Result.error(2,"用户名已存在");
        }
        else {
            return Result.error("申请失败");
        }
    }

    //查询用户的基本信息，用于首页信息显示
    @PostMapping("/querymsssage")
    public Result userQueryMsssage(@RequestBody User user)
    {
        try {
            User data=userService.userQueryMsssage(user);
            return Result.success(data);
        }catch (Exception e){
            return Result.error("查询失败");
        }
    }

    //用户添加
    @PostMapping("/add")
    @PreAuthorize("/user/add")
    @AutoLog(module = "用户管理",operate = "添加用户")
    public Result useradd(@RequestBody User user) throws Exception {
        String pas= RsaUtils.decrypt(user.getPassword());
        user.setPassword(pas);
        int flag=userService.useradd(user);
        if (flag==1)
        {
            return Result.success();
        }
        else
        {
            return Result.error(2,"用户名已存在");
        }
    }

    //用户查询
    @PostMapping("/query")
    public Result userquery(@RequestBody String json)
    {
        try {
            JSONObject data=userService.userquery(json);
            return Result.success(data);
        }
        catch (Exception error){
            return Result.error("获取数据失败:"+error);
        }
    }

    //查询注册
    @PostMapping("/applicationquery")
    public Result applicationQuery()
    {
        try {
            List<User> data=userService.applicationQuery();
            return Result.success(data);
        }
        catch (Exception error){
            return Result.error("获取数据失败:"+error);
        }
    }

    //查询用户所拥有的路由
    @PostMapping("/queryRoles")
    public Result queryUserRoles(@RequestBody User user)
    {
        try {
            List<String> data=userService.queryUserRoles(user.getId());
            return Result.success(data);
        }catch (Exception e){
            return Result.error("查询失败");
        }
    }

    //用户删除
    @PostMapping("/delete")
    @PreAuthorize("/user/delete")
    @AutoLog(module = "用户管理",operate = "删除用户")
    public Result userdelete(@RequestBody List<String> userlist)
    {
        List<String> whitelist=new ArrayList<>(List.of("1"));
        userlist.removeAll(whitelist);
        if(userlist.isEmpty())
        {
            return Result.error("无法删除初始用户");
        }
        userService.userdelete(userlist);
        return Result.success();
    }

    //同意用户申请
    @PostMapping("/agree")
    @PreAuthorize("/user/agree")
    @AutoLog(module = "用户管理",operate = "同意用户申请")
    public Result useragree(@RequestBody List<String> userlist)
    {
        userService.useragree(userlist);
        return Result.success();
    }

    //拒绝用户申请
    @PostMapping("/reject")
    @PreAuthorize("/user/reject")
    @AutoLog(module = "用户管理",operate = "拒绝用户申请")
    public Result userReject(@RequestBody List<String> userlist)
    {
        userService.userReject(userlist);
        return Result.success();
    }

    //用户修改
    @PostMapping("/update")
    @PreAuthorize("/user/update")
    @AutoLog(module = "用户管理",operate = "更新用户信息")
    public Result update(@RequestBody User user)
    {
        userService.userupdate(user);
        return Result.success();
    }

    //用户修改个人信息
    @PostMapping("/updateSelf")
    @PreAuthorize("/user/updateSelf")
    @AutoLog(module = "用户管理",operate = "更新个人信息")
    public Result updateSelf(@RequestBody User user)
    {
        userService.userUpdateSelf(user);
        return Result.success();
    }

    //用户修改个人密码
    @PostMapping("/updatePwd")
    @PreAuthorize("/user/updatePwd")
    @AutoLog(module = "用户管理",operate = "修改个人密码")
    public Result updatePwd(@RequestBody User user) throws Exception {
        userService.userUpdatePwd(user);
        return Result.success();
    }

    //管理员修改用户密码
    @PostMapping("/updateUserPwd")
    @PreAuthorize("/user/updateUserPwd")
    @AutoLog(module = "用户管理",operate = "修改用户密码")
    public Result updateUserPwd(@RequestBody User user) throws Exception {
        userService.userUpdatePwd(user);
        return Result.success();
    }

    //为用户分配角色
    @PreAuthorize("/user/assignRole")
    @AutoLog(module = "用户管理",operate = "更新用户角色")
    @PostMapping("/assignRole")
    public Result userAssignRole(@RequestBody User user)
    {
        userService.userAssignRole(user);
        return Result.success();
    }
}
