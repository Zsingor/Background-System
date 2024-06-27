package com.example.springtest.service;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Notification;

import java.util.List;

public interface NotificationService {
    void addNotification(Notification notification);

    JSONObject queryNotifications(String json);

    void deleteNotification(Integer id);
}
