package com.example.springtest.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Logs;
import com.example.springtest.entity.Result;
import com.example.springtest.service.LogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class LogsController {
    @Autowired
    private LogsService logsService;

    @PostMapping("/logs/query")
    public Result logsquery(@RequestBody String json)
    {
        try {
            JSONObject data=logsService.logsquery(json);
            return Result.success(data);
        }
        catch (Exception error){
            return Result.error("获取数据失败:"+error);
        }
    }

    @PostMapping("/logs/delete")
    public Result logsdelete(@RequestBody List<String> logslist)
    {
        int flag=logsService.logsdelete(logslist);
        if (flag==1)
        {
            return Result.success();
        }
        else {
            return Result.error("删除失败");
        }
    }
}
