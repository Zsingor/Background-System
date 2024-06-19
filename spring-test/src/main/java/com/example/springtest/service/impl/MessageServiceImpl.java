package com.example.springtest.service.impl;

import com.example.springtest.entity.Message;
import com.example.springtest.mapper.MessageMapper;
import com.example.springtest.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void addMessage(Message message) {
        Date createtime=new Date(System.currentTimeMillis());
        message.setCreateTime(createtime);
        messageMapper.addMessage(message);
    }

    @Override
    public List<Message> getMessage(String senderId, String receiverId) {
        return messageMapper.getMessage(senderId,receiverId);
    }
}
