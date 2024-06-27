package com.example.springtest.mapper;

import com.example.springtest.entity.Notification;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("insert into test.notification(sender_id,title, content, create_time) VALUES (#{senderId},#{title},#{content},#{createTime})")
    void addNotification(Notification notification);

    List<Notification> queryNotification(Notification notification);

    @Delete("delete from test.notification where id = #{id}")
    void deleteNotification(Integer id);
}
