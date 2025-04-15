package com.example.projectbackend.service.impl;

import com.example.projectbackend.entity.Conversation;
import com.example.projectbackend.entity.Message;
import com.example.projectbackend.mapper.ConversationsMapper;
import com.example.projectbackend.mapper.MessageMapper;
import com.example.projectbackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ConversationsMapper conversationsMapper;

    @Override
    public void addMessage(Message message) {
        Date createtime=new Date(System.currentTimeMillis());
        message.setCreateTime(createtime);
        messageMapper.addMessage(message);

        Conversation conversation=new Conversation();
        conversation.setUserId(message.getSenderId());
        conversation.setContactId(message.getReceiverId());
        conversation.setUpdateTime(createtime);
        conversation.setMessageId(message.getId());
        conversation.setUnreadCount(0);

        Conversation conversation1=new Conversation();
        conversation1.setUserId(message.getReceiverId());
        conversation1.setContactId(message.getSenderId());
        conversation1.setUpdateTime(createtime);
        conversation1.setMessageId(message.getId());
        conversation1.setUnreadCount(0);

        int flag=conversationsMapper.update(conversation);
        int flag1=conversationsMapper.update(conversation1);

        if(flag==0)
        {
            conversationsMapper.add(conversation);
        }
        if(flag1==0)
        {
            conversationsMapper.add(conversation1);
        }
    }

    @Override
    public List<Message> getMessage(String senderId, String receiverId) {
        return messageMapper.getMessage(senderId,receiverId);
    }
}
