package com.example.projectbackend.service;

import com.alibaba.fastjson.JSONObject;
import com.example.projectbackend.entity.Logs;

import java.util.List;

public interface LogsService {
    JSONObject logsquery(String json);

    int logsadd(Logs logs);

    int logsdelete(List<String> logslist);
}
