package com.example.projectbackend.mapper;

import com.example.projectbackend.entity.Notification;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("insert into sys_data.notification(sender_id,title, content, create_time) VALUES (#{senderId},#{title},#{content},#{createTime})")
    void addNotification(Notification notification);

    List<Notification> queryNotification(Notification notification);

    @Delete("delete from sys_data.notification where id = #{id}")
    void deleteNotification(Integer id);
}
