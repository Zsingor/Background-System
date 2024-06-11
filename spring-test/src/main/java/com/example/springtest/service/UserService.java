package com.example.springtest.service;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.User;

public interface UserService {

    JSONObject userlogin(User user);

    int useradd(User user);

    JSONObject userquery(User user);

    int userdelete(User user);

    int userupdate(User user);
}
