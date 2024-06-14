package com.example.springtest.mapper;

import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //添加用户信息
    @Insert("insert into test.user(id,name,password,email,roleid,description,status)"+
            "values(#{id},#{name},#{password},#{email},#{roleid},#{description},#{status})")
    void useradd(User user);

    //查询用户信息
    List<User> userquery(User user);


    //根据对应的主键查询用户
    @Select("select * from test.user where name=#{name}")
    User userPrimaryquery(User user);

    //删除用户信息
    void userdelete(List<String> userlist);

    //更新用户信息
    @Update("update test.user set name=#{name},password=#{password},email=#{email},roleid=#{roleid},"+
            "description=#{description},status=#{status} where id=#{id}")
    void userupdate(User user);

    //更新用户的角色
    @Update("update test.user set roleid=null where roleid=#{roleid}")
    void userUpdateRole(List<String> rolelist);
}
