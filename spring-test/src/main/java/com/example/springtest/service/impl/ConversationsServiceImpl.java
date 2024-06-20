package com.example.springtest.service.impl;

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
}
