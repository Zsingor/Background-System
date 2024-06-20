package com.example.springtest.service;

import com.example.springtest.entity.User;

import java.util.List;

public interface ConversationsService {
    List<User> getContacts(String userId);
}
