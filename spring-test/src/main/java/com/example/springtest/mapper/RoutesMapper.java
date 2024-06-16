package com.example.springtest.mapper;

import com.example.springtest.entity.Routes;
import com.example.springtest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoutesMapper {
    //查询用户拥有的路由
    List<Routes> routesquery(List<String> routesid);

    //查询所有路由信息
    List<Routes> routesAllquery(Routes routes);

    //查询所有父路由
    @Select("select * from test.routes where level=1 order by position DESC")
    List<Routes> routesParentquery();

    //添加新路由
    @Insert("insert into test.routes(id,name,title,path,level,status,icon,parentid,position,type)"+
            "values(#{id},#{name},#{title},#{path},#{level},#{status},#{icon},#{parentid},#{position},#{type})")
    void routesadd(Routes routes);

    //修改路由
    @Update("update test.routes set name=#{name},title=#{title},path=#{path},level=#{level},"+
            "status=#{status},icon=#{icon},parentid=#{parentid},position=#{position},type=#{type} where id=#{id}")
    void routesupdate(Routes routes);

    //删除路由
    void routesdelete(List<String> menulist);

    //查询用户拥有的所有权限
    List<Routes> queryAuthority(List<String> routesIds);
}
