package com.example.springtest.mapper;

import com.example.springtest.entity.Roles;
import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RolesMapper {
    @Insert("insert into test.roles(id,name,description)"+
            "values(#{id},#{name},#{description})")
    void rolesadd(Roles roles);

    @Select("select * from test.roles")
    List<Roles> rolesqueryAll();

    @Select("select * from test.roles where id=#{id}")
    Roles rolesqueryPrimary(Integer id);

    List<Roles> rolesquery(Roles roles);

    @Delete("delete from test.roles where id=#{id}")
    void rolesdelete(Roles roles);

    @Update("update test.roles set name=#{name},description=#{description} where id=#{id}")
    void rolesupdate(Roles roles);
}