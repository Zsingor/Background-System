package com.example.springtest.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;
import com.example.springtest.mapper.*;
import com.example.springtest.service.EmailService;
import com.example.springtest.service.UserService;
import com.example.springtest.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoutesMapper routesMapper;
    @Autowired
    private RolesRoutesMapper rolesRoutesMapper;
    @Autowired
    private UserRolesMapper userRolesMapper;
    @Autowired
    private EmailService emailService;


    //用户登录
    @Override
    public JSONObject userlogin(User user) {
        User user1 = userMapper.userNamequery(user);
        JSONObject response = new JSONObject();
        //如果当前用户不存在或者密码错误
        if(user1==null)
        {
            response.put("token", "0");
        }
        else if(!user1.getPassword().equals(user.getPassword()))
        {
            response.put("token", "1");
        }
        else
        {
            List<Routes> menuList=new ArrayList<Routes>();
            //拿到用户的所有角色信息
            List<String> roleLists=userRolesMapper.queryUserRoles(user1.getId());
            if(!roleLists.isEmpty())
            {
                //拿到用户拥有的所有路由
                List<String> routesIds=rolesRoutesMapper.queryRolesRoutes(roleLists,"1");
                //列表去重
                routesIds=routesIds.stream().distinct().collect(Collectors.toList());
                if(!routesIds.isEmpty())
                {
                    List<Routes> routesList=routesMapper.routesquery(routesIds);
                    menuList = Routeprocess(routesList);
                }
            }


            Map<String, Object> claims=new HashMap<>();
            claims.put("user_id",user1.getId());
            claims.put("user_name",user1.getName());
            String token= JwtUtils.generateJWT(claims);

            response.put("menuList", menuList);
            response.put("user_name", user1.getName());
            response.put("user_id", user1.getId());
            response.put("token", token);
        }
        return response;
    }

    @Override
    public int userCheckpwd(User user) {
        User user1 = userMapper.userNamequery(user);
        //如果当前用户不存在或者密码错误
        if(user1==null)
        {
            return 0;
        }
        else if(!user1.getPassword().equals(user.getPassword()))
        {
            return 0;
        }
        return 1;
    }

    @Override
    public List<User> queryMessageUser() {
        return userMapper.queryMessageUser();
    }


    @Override
    public User userQueryMsssage(User user) {
        return userMapper.userPrimaryquery(user.getId());
    }

    // 用户注册
    @Override
    public int useradd(User user) {
        try {
            User user1 = userMapper.userNamequery(user);
            if(user1!=null)
            {
                return 2;
            }
            else
            {
                user.setId(UUIDUtils.getUUID());
                Date createtime=new Date(System.currentTimeMillis());
                user.setCreateTime(createtime);
                userMapper.useradd(user);
                return 1;
            }
        }
        catch (Exception error)
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
    }

    // 用户查询
    @Override
    public JSONObject userquery(String json) {
        //根据json解析出具体的值
        JSONObject jsonObject= JSON.parseObject(json);
        User user=jsonObject.getObject("queryForm",User.class);
        int currentPage=jsonObject.getInteger("currentPage");
        int pageSize=jsonObject.getInteger("pageSize");
        if(user.getRolesid()!=null)
        {
            user.setRolesidSize(user.getRolesid().size());
        }
        System.out.println("你好");
        List<User> data=userMapper.userquery(user);
        //列表去重
        //data=data.stream().distinct().collect(Collectors.toList());
        return QueryResult.getResult(data,currentPage,pageSize);
    }

    @Override
    public List<User> applicationQuery() {
        return userMapper.applicationQuery();
    }

    // 用户删除
    @Override
    public void userdelete(List<String> userlist) {
        userRolesMapper.deleteUsersRoles(userlist);
        userMapper.userdelete(userlist);
    }

    //同意用户申请
    @Override
    public void useragree(List<String> userlist) {
        userMapper.useragree(userlist);
        for (String userId:userlist)
        {
            User data=userMapper.userPrimaryquery(userId);
            String emailTo=data.getEmail();
            if(!Objects.equals(emailTo, ""))
            {
                String subject="申请结果通知";
                String text="";
                String date= TimeConvert.dateToTime(data.getCreateTime());
                text=String.format("您于 %s 申请的账号 %s 已通过申请！",date,data.getName());
                emailService.sendEmail(emailTo,subject,text);
            }
        }
    }

    //拒绝用户申请
    @Override
    public void userReject(List<String> userlist) {
        for (String userId:userlist)
        {
            User data=userMapper.userPrimaryquery(userId);
            String emailTo=data.getEmail();
            if(!Objects.equals(emailTo, ""))
            {
                String subject="申请结果通知";
                String text="";
                String date= TimeConvert.dateToTime(data.getCreateTime());
                text=String.format("非常抱歉，您于 %s 申请的账号 %s 未通过申请！",date,data.getName());
                emailService.sendEmail(emailTo,subject,text);
            }
        }
        userRolesMapper.deleteUsersRoles(userlist);
        userMapper.userdelete(userlist);
    }


    // 用户更新
    @Override
    public void userupdate(User user) {
        userMapper.userupdate(user);
        userAssignRole(user);
//        try {
//            userMapper.userupdate(user);
//            userAssignRole(user);
//            return 1;
//        }
//        catch (Exception error)
//        {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return 0;
//        }
    }

    @Override
    public int userUpdateSelf(User user) {
        try {
            userMapper.userUpdateSelf(user);
            return 1;
        }
        catch (Exception error)
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
    }

    @Override
    public int userUpdatePwd(User user) {
        try {
            String pas= RsaUtils.decrypt(user.getPassword());
            user.setPassword(pas);
            userMapper.userUpdatePwd(user);
            return 1;
        }
        catch (Exception error)
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
    }

    //给用户分配角色
    @Override
    public int userAssignRole(User user) {
        List<String> roleList=user.getRolesid();
        if(Objects.equals(user.getId(), "1"))
        {
            List<String> whitelist=new ArrayList<>(List.of("1"));
            Set<String> resultSet = new HashSet<>(roleList);
            resultSet.addAll(whitelist);
            List<String> resultList = new ArrayList<>(resultSet);
            user.setRolesid(resultList);
            userRolesMapper.deleteUserRoles(user);
            userRolesMapper.addUserRoles(user.getId(),resultList);
        }
        else {
            userRolesMapper.deleteUserRoles(user);
            userRolesMapper.addUserRoles(user.getId(),roleList);
        }
        return 1;
    }

    //查询用户的角色
    @Override
    public List<String> queryUserRoles(String userid) {
        return userRolesMapper.queryUserRoles(userid);
    }

    //获得用户拥有的权限
    @Override
    public List<String> queryUserAuthority(String userid) {
        //拿到用户的所有角色信息
        List<String> roleLists=userRolesMapper.queryUserRoles(userid);
        //拿到用户拥有的所有权限
        List<String> routesIds=rolesRoutesMapper.queryRolesRoutes(roleLists,"2");
        //列表去重
        routesIds=routesIds.stream().distinct().collect(Collectors.toList());

        List<Routes> authorityList=routesMapper.queryAuthority(routesIds);

        List<String> result=authorityProcess(authorityList);
        return result;
    }

    //处理路由格式
    private List<Routes> Routeprocess(List<Routes> routesList){
        // 分类一级路由和二级路由
        List<Routes> parentRoutes = routesList.stream()
                .filter(route -> route.getLevel() == 1)
                .collect(Collectors.toList());

        List<Routes> childRoutes = routesList.stream()
                .filter(route -> route.getLevel() == 2)
                .toList();

        // 构建一级路由的 children
        for (Routes route : parentRoutes) {
            List<Routes> children = childRoutes.stream()
                    .filter(r -> Objects.equals(r.getParentid(), route.getId()))
                    .collect(Collectors.toList());
            route.setChildren(children);
        }

        return parentRoutes;
    }

    private List<String> authorityProcess(List<Routes> routesList){
        // 分类一级权限和二级权限
        List<Routes> parentAuthority = routesList.stream()
                .filter(route -> route.getLevel() == 1)
                .toList();

        List<Routes> childAuthority = routesList.stream()
                .filter(route -> route.getLevel() == 2)
                .toList();

        // 将 parentAuthority 转换为 Map<id, path>
        Map<String, String> parentMap = parentAuthority.stream()
                .collect(Collectors.toMap(Routes::getId, Routes::getPath));

        // 创建 resultList 并拼接路径
        List<String> resultList = childAuthority.stream()
                .map(child -> {
                    String parentPath = parentMap.get(child.getParentid());
                    return parentPath + child.getPath();
                })
                .collect(Collectors.toList());

        return resultList;
    }
}
