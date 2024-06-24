package com.example.springtest.service.impl;

import com.example.springtest.entity.Conversation;
import com.example.springtest.entity.User;
import com.example.springtest.mapper.ConversationsMapper;
import com.example.springtest.service.ConversationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationsServiceImpl implements ConversationsService {
    @Autowired
    private ConversationsMapper conversationsMapper;

    @Override
    public List<User> getContacts(String userId) {
        return conversationsMapper.getContactUser(userId);
    }

    @Override
    public Integer getUnreadCount(String userId) {
        return conversationsMapper.getUnreadCount(userId);
    }

    @Override
    public void updateUnreadCount(Conversation conversation) {
        conversationsMapper.updateUnreadCount(conversation);
    }

    @Override
    public void addUnreadCount(String userId,String contactId) {
        conversationsMapper.addUnreadCount(userId,contactId);
    }
}
