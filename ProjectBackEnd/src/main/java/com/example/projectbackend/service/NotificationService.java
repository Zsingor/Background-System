package com.example.projectbackend.service;

import com.alibaba.fastjson.JSONObject;
import com.example.projectbackend.entity.Notification;

public interface NotificationService {
    void addNotification(Notification notification);

    JSONObject queryNotifications(String json);

    void deleteNotification(Integer id);
}
