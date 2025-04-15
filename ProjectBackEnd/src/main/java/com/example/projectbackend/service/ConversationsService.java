package com.example.projectbackend.service;

import com.example.projectbackend.entity.Conversation;
import com.example.projectbackend.entity.User;

import java.util.List;

public interface ConversationsService {
    List<User> getContacts(String userId);

    Integer getUnreadCount(String userId);

    void updateUnreadCount(Conversation conversation);

    void addUnreadCount(String userId,String contactId);
}
