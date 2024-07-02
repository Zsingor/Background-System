package com.example.springtest.mapper;

import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRolesMapper {
    //删除某个用户所有的角色信息
    @Delete("delete from test.user_roles where userid=#{id}")
    void deleteUserRoles(User user);

    //为用户分配角色
    void addUserRoles(String userid, List<String> roleids);

    //查询用户拥有的所有角色
    @Select("select roleid from test.user_roles where userid=#{id}")
    List<String> queryUserRoles(String id);

    //根据角色删除所有的信息
    void deleteRolesUsers(List<String> rolesid);

    //根据用户删除所有的信息
    void deleteUsersRoles(List<String> usersid);
}
