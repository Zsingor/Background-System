package com.example.springtest.mapper;

import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //添加用户信息
    @Insert("insert into test.user(name,sex,address,createdate,startdate,enddate,roleid)"+
            "values(#{name},#{sex},#{address},#{createdate},#{startdate},#{enddate},#{roleid})")
    void useradd(User user);

    //查询用户信息
    List<User> userquery(User user);


    //根据对应的主键查询用户
    @Select("select * from test.user where name=#{name}")
    User userPrimaryquery(User user);

    //删除用户信息
    @Delete("delete from test.user where name=#{name}")
    void userdelete(User user);

    //更新用户信息
    @Update("update test.user set name=#{name},sex=#{sex},address=#{address},createdate=#{createdate},"+
            "startdate=#{startdate},enddate=#{enddate},roleid=#{roleid} where name=#{name}")
    void userupdate(User user);
}
