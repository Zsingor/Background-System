package com.example.projectbackend.service.impl;

import com.example.projectbackend.entity.Conversation;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.mapper.ConversationsMapper;
import com.example.projectbackend.service.ConversationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
