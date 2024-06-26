package com.example.springtest.controller;

import com.example.springtest.entity.Notification;
import com.example.springtest.entity.Result;
import com.example.springtest.service.NotificationService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/query")
    public Result queryNotification() {
        try {
            List<Notification> data=notificationService.queryNotifications();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取数据失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteNotification(@PathVariable("id")int id) {
        try {
            notificationService.deleteNotification(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除通知失败");
        }
    }
}
