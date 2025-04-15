package com.example.projectbackend.service;

import com.example.projectbackend.entity.Message;

import java.util.List;

public interface MessageService {
    void addMessage(Message message);

    List<Message> getMessage(String senderId, String receiverId);
}
