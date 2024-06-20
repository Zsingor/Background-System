package com.example.springtest.mapper;

import com.example.springtest.entity.Conversation;
import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ConversationsMapper {

    List<User> getContactUser(String userId);

    @Update("UPDATE test.conversations SET message_id=#{messageId},update_time=#{updateTime},unread_count=#{unreadCount}"+
            " WHERE user_id = #{userId} and contact_id=#{contactId}")
    int update(Conversation conversation);

    @Insert("insert into test.conversations (user_id,contact_id,unread_count,message_id,update_time) values(#{userId},#{contactId},#{unreadCount},#{messageId},#{updateTime})")
    void add(Conversation conversation);
}
