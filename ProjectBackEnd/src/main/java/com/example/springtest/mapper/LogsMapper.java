package com.example.springtest.mapper;

import com.example.springtest.entity.Logs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogsMapper {
    //添加日志
    @Insert("insert into test.logs(id,userid,username,module,operate,details,ip,operatedate)"+
            "values(#{id},#{userid},#{username},#{module},#{operate},#{details},#{ip},#{operatedate})")
    void logsadd(Logs logs);

    //日志查询
    List<Logs> logsquery(Logs logs);

    //删除日志
    void logsdelete(List<String> logslist);
}
