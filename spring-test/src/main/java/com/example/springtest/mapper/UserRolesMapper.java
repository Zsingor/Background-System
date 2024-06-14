package com.example.springtest.mapper;

import com.example.springtest.entity.Roles;
import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRolesMapper {
    @Delete("delete from test.user_roles where userid=#{id}")
    void deleteUserRoles(User user);

    void addUserRoles(String userid, List<String> roleids);
}
