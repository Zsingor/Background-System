package com.example.springtest.mapper;

import com.example.springtest.entity.Form;
import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //添加用户信息
    @Insert("insert into test.user(name,sex,address,createdate,startdate,enddate)"+
            "values(#{name},#{sex},#{address},#{createdate},#{startdate},#{enddate})")
    void useradd(User user);

    //查询用户信息
    List<User> userquery(User user);


    //查询用户所拥有的路由
    @Select("select * from test.user where name=#{name}")
    User userRoutesquery(User user);

    //删除用户信息
    @Delete("delete from test.user where name=#{name}")
    void userdelete(User user);

    //更新用户信息
    @Update("update test.user set name=#{name},sex=#{sex},address=#{address},createdate=#{createdate},"+
            "startdate=#{startdate},enddate=#{enddate} where name=#{name}")
    void userupdate(User user);

    //更新用户路由信息
    @Update("update test.user set routesid=#{routesid} where name=#{name}")
    void userroutesupdate(User user);
}
