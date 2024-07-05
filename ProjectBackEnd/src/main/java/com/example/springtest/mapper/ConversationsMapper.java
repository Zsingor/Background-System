package com.example.springtest.mapper;

import com.example.springtest.entity.Conversation;
import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ConversationsMapper {

    //查找对应用户的联系人
    List<User> getContactUser(String userId);

    @Select("select sum(unread_count) from sys_data.conversations where user_id=#{userid}")
    //查找对应用户的未读信息
    Integer getUnreadCount(String userid);

    //更新会话
    @Update("UPDATE sys_data.conversations SET unread_count=0"+
            " WHERE user_id = #{userId} and contact_id=#{contactId}")
    void updateUnreadCount(Conversation conversation);

    //未读数加一
    @Update("UPDATE sys_data.conversations SET unread_count=unread_count+1"+
            " WHERE user_id = #{userId} and contact_id=#{contactId}")
    void addUnreadCount(String userId,String contactId);

    //更新会话
    @Update("UPDATE sys_data.conversations SET message_id=#{messageId},update_time=#{updateTime},unread_count=#{unreadCount}"+
            " WHERE user_id = #{userId} and contact_id=#{contactId}")
    int update(Conversation conversation);

    //添加会话
    @Insert("insert into sys_data.conversations (user_id,contact_id,unread_count,message_id,update_time) values(#{userId},#{contactId},#{unreadCount},#{messageId},#{updateTime})")
    void add(Conversation conversation);
}
