package com.example.projectbackend.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FilesService {
    String upload(MultipartFile file) throws IOException;

    List<String> mutiFileUpload(MultipartFile[] files) throws IOException;

    void downloads(String fileName, HttpServletResponse response) throws IOException;

    void deleteFile(String fileName);

    void deleteChunkFile(String fileName);

    JSONObject uploadEditor(MultipartFile file) throws IOException;

    void upLoadChunk(MultipartFile chunk,String filename) throws IOException;

    String mergeChunk(String filename, JSONArray hashList) throws IOException;

    void downloadChunk(String rangeHeader,HttpServletResponse response,String filename) throws IOException;
}
