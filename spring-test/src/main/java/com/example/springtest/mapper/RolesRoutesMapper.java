package com.example.springtest.mapper;

import com.example.springtest.entity.Roles;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolesRoutesMapper {
    //查询角色拥有的所有路由(为角色分配角色或权限时使用)
    @Select("select routeid from test.roles_routes where roleid=#{id}")
    List<String> queryRoleRoutes(Roles roles);

    //删除某个角色所有对应的路由信息
    @Delete("delete from test.roles_routes where roleid=#{id} and routetype='1'")
    void deleteRoleRoutes(Roles roles);

    //删除某个角色所有对应的权限信息
    @Delete("delete from test.roles_routes where roleid=#{id} and routetype='2'")
    void deleteAuthority(Roles roles);

    //根据路由删除对应的信息
    void deleteRoutesRoles(List<String> menulist);

    //根据角色删除对应的信息
    void deleteRolesRoutes(List<String> roleslist);

    //为角色分配路由
    void addRoleRoutes(String roleid, List<String> routeids,String routetype);

    //查找多个角色拥有的所有路由/权限
    List<String> queryRolesRoutes(List<String> roleList,String routetype);
}
