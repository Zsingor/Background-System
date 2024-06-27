package com.example.springtest.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Logs;
import com.example.springtest.entity.Roles;
import com.example.springtest.mapper.LogsMapper;
import com.example.springtest.service.LogsService;
import com.example.springtest.utils.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogsServiceImpl implements LogsService {

    @Autowired
    private LogsMapper logsMapper;


    @Override
    public JSONObject logsquery(String json) {
        JSONObject jsonObject= JSON.parseObject(json);
        Logs logs=jsonObject.getObject("queryForm",Logs.class);
        int currentPage=jsonObject.getInteger("currentPage");
        int pageSize=jsonObject.getInteger("pageSize");
        List<Logs> data=logsMapper.logsquery(logs);
        return QueryResult.getResult(data,currentPage,pageSize);
    }

    @Override
    public int logsadd(Logs logs) {
        try {
            logsMapper.logsadd(logs);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }

    @Override
    public int logsdelete(List<String> logslist) {
        try {
            logsMapper.logsdelete(logslist);
            return 1;
        }
        catch (Exception error)
        {
            return 0;
        }
    }
}
