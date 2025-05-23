package com.example.projectbackend.mapper;

import com.example.projectbackend.entity.Roles;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RolesMapper {

    //角色添加
    @Insert("insert into sys_data.roles(id,name,description,create_time,creator_id)"+
            "values(#{id},#{name},#{description},#{createTime},#{creatorId})")
    void rolesadd(Roles roles);


    //查找全部角色
    @Select("select * from sys_data.roles")
    List<Roles> rolesqueryAll();

    //角色查询
    List<Roles> rolesquery(Roles roles);


    //批量删除角色
    void rolesdelete(List<String> rolelist);

    //更新角色信息
    @Update("update sys_data.roles set name=#{name},description=#{description} where id=#{id}")
    void rolesupdate(Roles roles);
}
