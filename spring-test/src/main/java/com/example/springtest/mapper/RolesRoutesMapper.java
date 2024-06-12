package com.example.springtest.mapper;

import com.example.springtest.entity.Roles;
import com.example.springtest.entity.Routes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolesRoutesMapper {
    @Select("select routeid from test.roles_routes where roleid=#{id}")
    List<String> queryRoleRoutes(Roles roles);

    @Delete("delete from test.roles_routes where roleid=#{id}")
    void deleteRoleRoutes(Roles roles);

    void deleteRouteRoles(List<String> menulist);

    void addRoleRoutes(Integer roleid, List<String> routeids);
}
