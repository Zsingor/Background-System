package com.example.springtest.service;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.User;

import java.util.List;

public interface UserService {

    JSONObject userlogin(User user);

    int useradd(User user);

    JSONObject userquery(String json);

    int userdelete(List<String> userlist);

    int userupdate(User user);
}
