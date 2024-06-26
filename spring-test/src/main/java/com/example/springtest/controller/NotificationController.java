package com.example.springtest.controller;

import com.example.springtest.aop.authorize.PreAuthorize;
import com.example.springtest.aop.logs.AutoLog;
import com.example.springtest.config.websocket.WebSocketUtil;
import com.example.springtest.entity.Notification;
import com.example.springtest.entity.Result;
import com.example.springtest.service.NotificationService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WebSocketUtil webSocketUtil;

    @PostMapping("/query")
    public Result queryNotification() {
        try {
            List<Notification> data=notificationService.queryNotifications();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取数据失败");
        }
    }

    @AutoLog(module = "通知管理",operate = "删除通知")
    @PreAuthorize("/notification/delete")
    @DeleteMapping("/delete/{id}")
    public Result deleteNotification(@PathVariable("id")int id) {
        try {
            notificationService.deleteNotification(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除通知失败");
        }
    }

    //发送全体通知
    @AutoLog(module = "通知管理",operate = "发送通知")
    @PreAuthorize("/notification/send")
    @PostMapping( "/send")
    public Result sendMessageAll(@RequestBody Notification notification) {
        try {
            notificationService.addNotification(notification);
            webSocketUtil.sendNotification(notification);
            return Result.success();
        }
        catch (IOException e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
