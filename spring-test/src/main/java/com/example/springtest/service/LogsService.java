package com.example.springtest.service;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Logs;

import java.util.List;

public interface LogsService {
    JSONObject logsquery(String json);

    int logsadd(Logs logs);

    int logsdelete(Logs logs);
}
