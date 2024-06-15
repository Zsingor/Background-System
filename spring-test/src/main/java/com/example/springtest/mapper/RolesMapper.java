package com.example.springtest.mapper;

import com.example.springtest.entity.Roles;
import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RolesMapper {

    //角色添加
    @Insert("insert into test.roles(id,name,description)"+
            "values(#{id},#{name},#{description})")
    void rolesadd(Roles roles);


    //查找全部角色
    @Select("select * from test.roles")
    List<Roles> rolesqueryAll();

    //角色查询
    List<Roles> rolesquery(Roles roles);


    //批量删除角色
    void rolesdelete(List<String> rolelist);

    //更新角色信息
    @Update("update test.roles set name=#{name},description=#{description} where id=#{id}")
    void rolesupdate(Roles roles);
}
