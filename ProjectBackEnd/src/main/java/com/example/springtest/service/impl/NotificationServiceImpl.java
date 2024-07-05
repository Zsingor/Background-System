package com.example.springtest.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Logs;
import com.example.springtest.entity.Notification;
import com.example.springtest.mapper.NotificationMapper;
import com.example.springtest.service.NotificationService;
import com.example.springtest.utils.QueryResult;
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
