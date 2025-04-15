package com.example.projectbackend.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.projectbackend.aop.authorize.PreAuthorize;
import com.example.projectbackend.aop.logs.AutoLog;
import com.example.projectbackend.config.websocket.WebSocketUtil;
import com.example.projectbackend.entity.Notification;
import com.example.projectbackend.entity.Result;
import com.example.projectbackend.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WebSocketUtil webSocketUtil;

    @PostMapping("/query")
    public Result queryNotification(@RequestBody String json) {
        try {
            JSONObject data=notificationService.queryNotifications(json);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取数据失败");
        }
    }

    @AutoLog(module = "通知管理",operate = "删除通知")
    @PreAuthorize("/notification/delete")
    @DeleteMapping("/delete/{id}")
    public Result deleteNotification(@PathVariable("id")int id) {
        notificationService.deleteNotification(id);
        return Result.success();
    }

    //发送全体通知
    @AutoLog(module = "通知管理",operate = "发送通知")
    @PreAuthorize("/notification/send")
    @PostMapping( "/send")
    public Result sendMessageAll(@RequestBody Notification notification) throws IOException {
        notificationService.addNotification(notification);
        webSocketUtil.sendNotification(notification);
        return Result.success();
    }
}
