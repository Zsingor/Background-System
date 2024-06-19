package com.example.springtest.mapper;

import com.example.springtest.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Insert("insert into test.message (sender_id,receiver_id, content,create_time) values (#{senderId},#{receiverId}, #{content},#{createTime})")
    void addMessage(Message message);

    @Select("SELECT * FROM test.message " +
            "WHERE (sender_id = #{senderId} AND receiver_id = #{receiverId}) " +
            "OR (sender_id = #{receiverId} AND receiver_id = #{senderId}) " +
            "ORDER BY create_time ")
    List<Message> getMessage(String senderId, String receiverId);
}
