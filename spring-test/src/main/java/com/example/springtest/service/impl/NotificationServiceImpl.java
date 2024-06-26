package com.example.springtest.service.impl;

import com.example.springtest.entity.Notification;
import com.example.springtest.mapper.NotificationMapper;
import com.example.springtest.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;


    @Override
    public void addNotification(Notification notification) {
        Date createtime=new Date(System.currentTimeMillis());
        notification.setCreateTime(createtime);
        notificationMapper.addNotification(notification);
    }

    @Override
    public List<Notification> queryNotifications() {
        return notificationMapper.queryNotification();
    }

    @Override
    public void deleteNotification(Integer id) {
        notificationMapper.deleteNotification(id);
    }
}
