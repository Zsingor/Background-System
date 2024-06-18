package com.example.springtest.controller;


import com.example.springtest.config.websocket.WebSocketUtil;
import com.example.springtest.entity.Result;
import com.example.springtest.entity.WebSocketMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/webSocket")
public class WebSocketController {
    @Autowired
    private WebSocketUtil webSocketUtil;

    //获取在线用户信息
    @GetMapping(value = "/getUser")
    public Result getUser() {
        Map<String, WebSocketMsg> users = webSocketUtil.getUsers();
        Set<String> ids = users.keySet();
        return Result.success(ids);
    }

    //发送全体消息
    @GetMapping(value = "/sendMessageAll")
    public Result sendMessageAll(@RequestParam String message) {
        try {
            webSocketUtil.sendMessageAll(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success("消息推送成功：" + message);
    }

    //发送消息给某人
    @GetMapping(value = "/sendMessageTo")
    public Result sendMessageTo(@RequestParam String username, @RequestParam String message) {
        try {
            webSocketUtil.sendMessageTo(username,message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}
