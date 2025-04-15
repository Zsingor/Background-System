package com.example.projectbackend.mapper;

import com.example.projectbackend.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {

    void addMessage(Message message);

    @Select("SELECT * FROM sys_data.message " +
            "WHERE (sender_id = #{senderId} AND receiver_id = #{receiverId}) " +
            "OR (sender_id = #{receiverId} AND receiver_id = #{senderId}) " +
            "ORDER BY create_time ")
    List<Message> getMessage(String senderId, String receiverId);
}
