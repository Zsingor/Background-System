package com.example.springtest.service;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.User;

import java.util.List;

public interface UserService {

    JSONObject userlogin(User user);

    int userCheckpwd(User user);

    List<User> queryMessageUser();

    User userQueryMsssage(User user);

    int useradd(User user);

    JSONObject userquery(String json);

    List<User> applicationQuery();

    void userdelete(List<String> userlist);

    void useragree(List<String> userlist);

    void userReject(List<String> userlist);

    void userupdate(User user);

    void userUpdateSelf(User user);

    void userUpdatePwd(User user) throws Exception;

    void userAssignRole(User user);

    List<String> queryUserRoles(String userid);

    List<String> queryUserAuthority(String userid);
}
