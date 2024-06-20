package com.example.springtest.service.impl;

import com.example.springtest.entity.Conversation;
import com.example.springtest.entity.Message;
import com.example.springtest.mapper.ConversationsMapper;
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
