package com.example.springtest.service;

import com.example.springtest.entity.Message;

import java.util.List;

public interface MessageService {
    void addMessage(Message message);

    List<Message> getMessage(String senderId, String receiverId);
}
