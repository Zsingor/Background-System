package com.example.springtest.service;

import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FilesService {
    String upload(MultipartFile file) throws IOException;

    void downloads(String fileName, HttpServletResponse response) throws IOException;

    void deleteFile(String fileName);

    JSONObject uploadEditor(MultipartFile file) throws IOException;
}
