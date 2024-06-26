package com.example.springtest.service;

import com.example.springtest.entity.Notification;

import java.util.List;

public interface NotificationService {
    void addNotification(Notification notification);

    List<Notification> queryNotifications();

    void deleteNotification(Integer id);
}
