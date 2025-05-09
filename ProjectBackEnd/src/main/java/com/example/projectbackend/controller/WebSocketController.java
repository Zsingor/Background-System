package com.example.projectbackend.controller;


import com.example.projectbackend.config.websocket.WebSocketUtil;
import com.example.projectbackend.entity.*;
import com.example.projectbackend.service.ConversationsService;
import com.example.projectbackend.service.MessageService;
import com.example.projectbackend.service.NotificationService;
import com.example.projectbackend.service.UserService;
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
    private NotificationService notificationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConversationsService conversationsService;

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

    @GetMapping("/getConversations")
    public Result getConversations(@RequestParam String userId)
    {
        try {
            List<User> data=conversationsService.getContacts(userId);
            return Result.success(data);
        }catch (Exception e){
            return Result.error("获取用户数据失败");
        }
    }

    //发送消息给某人
    @PostMapping( "/sendMessageTo")
    public Result sendMessageTo(@RequestBody Message message) {
        System.out.println(message);
        try {
            //新增消息信息
            messageService.addMessage(message);
            //增加未读数
            conversationsService.addUnreadCount(message.getReceiverId(),message.getSenderId());
            //发送消息
            webSocketUtil.sendMessageTo(message.getReceiverId(),message);
            return Result.success();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
