package com.example.springtest.controller;


import com.example.springtest.config.websocket.WebSocketUtil;
import com.example.springtest.entity.Message;
import com.example.springtest.entity.Result;
import com.example.springtest.entity.User;
import com.example.springtest.entity.WebSocketMsg;
import com.example.springtest.service.MessageService;
import com.example.springtest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/webSocket")
public class WebSocketController {
    @Autowired
    private WebSocketUtil webSocketUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    //获取在线用户信息
    @GetMapping(value = "/getUser")
    public Result getUser() {
        Map<String, WebSocketMsg> users = webSocketUtil.getUsers();
        Set<String> ids = users.keySet();
        List<String> idList=new ArrayList<>(ids);
        return Result.success(idList);
    }

    //获取在线用户信息
    @GetMapping(value = "/getAllUser")
    public Result getAllUser() {
        try {
            List<User> data=userService.queryMessageUser();
            return Result.success(data);
        }catch (Exception e){
            return Result.error("获取用户数据失败");
        }
    }

    //发送全体消息
    @PostMapping( "/sendMessageAll")
    public Result sendMessageAll(@RequestBody Message message) {
        try {
            webSocketUtil.sendMessageAll(message.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success("消息推送成功：" + message.getContent());
    }

    //发送消息给某人
    @PostMapping( "/sendMessageTo")
    public Result sendMessageTo(@RequestBody Message message) {
        System.out.println(message);
        try {
            webSocketUtil.sendMessageTo(message.getReceiverId(),message.getContent());
            messageService.addMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}
