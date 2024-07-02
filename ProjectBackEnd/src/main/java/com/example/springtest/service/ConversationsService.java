package com.example.springtest.service;

import com.example.springtest.entity.Conversation;
import com.example.springtest.entity.User;

import java.util.List;

public interface ConversationsService {
    List<User> getContacts(String userId);

    Integer getUnreadCount(String userId);

    void updateUnreadCount(Conversation conversation);

    void addUnreadCount(String userId,String contactId);
}
