package com.example.springtest.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Result;
import com.example.springtest.service.FilesService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files")
public class FilesController {
    @Autowired
    private FilesService filesService;

    @PostMapping( "/upload")
    public Result upload(MultipartFile file) {
        try {
            return Result.success(filesService.upload(file));
        }
        catch (Exception e)
        {
            return Result.error("上传文件失败");
        }
    }

    /**
     * 富文本编辑器上传
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/uploadEditor")
    public JSONObject uploaduploadEditor(@RequestParam("file") MultipartFile file) throws Exception {
        System.out.println(file.getOriginalFilename());
        return filesService.uploadEditor(file);
    }


    //下载文件
    @GetMapping("/download/{fileName}")
    public void downloads(@PathVariable String fileName, HttpServletResponse response) throws IOException
    {
        filesService.downloads(fileName,response);
    }

    @PostMapping("deletefile/{fileName}")
    public void deleteFile(@PathVariable String fileName){
        filesService.deleteFile(fileName);
    }
}
