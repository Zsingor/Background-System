package com.example.projectbackend.mapper;

import com.example.projectbackend.entity.Logs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogsMapper {
    //添加日志
    @Insert("insert into sys_data.logs(id,userid,username,module,operate,details,ip,err_flag,err_msg,operatedate)"+
            "values(#{id},#{userid},#{username},#{module},#{operate},#{details},#{ip},#{errFlag},#{errMsg},#{operatedate})")
    void logsadd(Logs logs);

    //日志查询
    List<Logs> logsquery(Logs logs);

    //删除日志
    void logsdelete(List<String> logslist);
}
