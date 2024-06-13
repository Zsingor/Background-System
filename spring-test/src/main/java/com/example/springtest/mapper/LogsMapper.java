package com.example.springtest.mapper;

import com.example.springtest.entity.Logs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogsMapper {
    @Insert("insert into test.logs(id,userid,username,roleid,module,operate,details,ip,operatedate)"+
            "values(#{id},#{userid},#{username},#{roleid},#{module},#{operate},#{details},#{ip},#{operatedate})")
    void logsadd(Logs logs);

    @Delete("delete from test.logs where id=#{id}")
    void logsdelete(Logs logs);

    List<Logs> logsquery(Logs logs);
}
