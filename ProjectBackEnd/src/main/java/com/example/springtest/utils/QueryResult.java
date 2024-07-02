package com.example.springtest.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class QueryResult {

    public static <T> JSONObject getResult(List<T> data,int currentPage,int pageSize)
    {
        List<T> res;
        int rowSum=data.size();
        int start = (currentPage - 1) * pageSize;
        int end = currentPage * pageSize;
        if(end<=rowSum)
        {
            res=data.subList(start,end);
        }
        else {
            res=data.subList(start,rowSum);
        }
        JSONObject response = new JSONObject();
        response.put("rowSum", rowSum);
        response.put("resultList", res);
        return response;
    }
}
