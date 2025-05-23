package com.example.projectbackend.mapper;

import com.example.projectbackend.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //添加用户信息
    @Insert("insert into sys_data.user(id,name,password,email,description,status,create_time,type)"+
            "values(#{id},#{name},#{password},#{email},#{description},#{status},#{createTime},#{type})")
    void useradd(User user);

    //查询用户信息
    List<User> userquery(User user);

    @Select("select id,name,email,description,status,create_time from sys_data.user where type=0 order by create_time desc ")
    List<User> applicationQuery();

    //返回消息的用户信息
    @Select("select id,name,description from sys_data.user")
    List<User> queryMessageUser();

    //根据对应的字段查询用户
    @Select("select * from sys_data.user where name=#{name}")
    User userNamequery(User user);

    //根据对应的主键查询用户
    @Select("select id,name,email,description,status,create_time from sys_data.user where id=#{id}")
    User userPrimaryquery(String id);

    //删除用户信息
    void userdelete(List<String> userlist);

    //同意用户申请
    void useragree(List<String> userlist);

    //更新用户信息
    @Update("update sys_data.user set name=#{name},email=#{email},"+
            "description=#{description},status=#{status} where id=#{id}")
    void userupdate(User user);

    //更新用户个人信息
    @Update("update sys_data.user set name=#{name},email=#{email},"+
            "description=#{description} where id=#{id}")
    void userUpdateSelf(User user);

    //更新用户个人密码
    @Update("update sys_data.user set password=#{password} where id=#{id}")
    void userUpdatePwd(User user);
}
