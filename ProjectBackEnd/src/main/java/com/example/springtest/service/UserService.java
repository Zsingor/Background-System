package com.example.springtest.service;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.User;

import java.util.List;

public interface UserService {

    JSONObject userlogin(User user);

    List<User> queryMessageUser();

    User userQueryMsssage(User user);

    int useradd(User user);

    JSONObject userquery(String json);

    List<User> applicationQuery();

    int userdelete(List<String> userlist);

    int useragree(List<String> userlist);

    int userupdate(User user);

    int userAssignRole(User user);

    List<String> queryUserRoles(String userid);

    List<String> queryUserAuthority(String userid);
}