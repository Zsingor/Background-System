package com.example.springtest.mapper;

import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //添加用户信息
    @Insert("insert into test.user(id,name,password,email,description,status)"+
            "values(#{id},#{name},#{password},#{email},#{description},#{status})")
    void useradd(User user);

    //查询用户信息
    List<User> userquery(User user);

    //返回消息的用户信息
    @Select("select id,name,description from test.user")
    List<User> queryMessageUser();

    //根据对应的字段查询用户
    @Select("select * from test.user where name=#{name}")
    User userNamequery(User user);

    //根据对应的主键查询用户
    @Select("select * from test.user where id=#{id}")
    User userPrimaryquery(User user);

    //删除用户信息
    void userdelete(List<String> userlist);

    //更新用户信息
    @Update("update test.user set name=#{name},password=#{password},email=#{email},"+
            "description=#{description},status=#{status} where id=#{id}")
    void userupdate(User user);
}
