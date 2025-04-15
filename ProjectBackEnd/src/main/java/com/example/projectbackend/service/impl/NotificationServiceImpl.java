package com.example.projectbackend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.projectbackend.entity.Notification;
import com.example.projectbackend.mapper.NotificationMapper;
import com.example.projectbackend.service.NotificationService;
import com.example.projectbackend.utils.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
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
    public JSONObject queryNotifications(String json) {
        JSONObject jsonObject= JSON.parseObject(json);
        Notification notification=jsonObject.getObject("queryForm", Notification.class);
        int currentPage=jsonObject.getInteger("currentPage");
        int pageSize=jsonObject.getInteger("pageSize");
        List<Notification> data=notificationMapper.queryNotification(notification);
        return QueryResult.getResult(data,currentPage,pageSize);
    }

    @Override
    public void deleteNotification(Integer id) {
        notificationMapper.deleteNotification(id);
    }
}
