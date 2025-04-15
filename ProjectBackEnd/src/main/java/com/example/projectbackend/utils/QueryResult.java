package com.example.projectbackend.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class QueryResult {

    public static <T> JSONObject getResult(List<T> data,int currentPage,int pageSize)
    {
        JSONObject response = new JSONObject();
        List<T> res;
        int rowSum=data.size();
        if(pageSize==-1)
        {
            response.put("rowSum", rowSum);
            response.put("resultList", data);
            return response;
        }
        int start = (currentPage - 1) * pageSize;
        int end = currentPage * pageSize;
        if(end<=rowSum)
        {
            res=data.subList(start,end);
        }
        else {
            res=data.subList(start,rowSum);
        }
        response.put("rowSum", rowSum);
        response.put("resultList", res);
        return response;
    }
}
