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
import java.io.FileOutputStream;
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

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        try {
            System.out.println(file.getOriginalFilename());
            return Result.success(filesService.upload(file));
        } catch (Exception e) {
            return Result.error("上传文件失败");
        }
    }

    @PostMapping("/mutiFileUpload")
    public Result mutiFileUpload(MultipartFile[] files) {
        try {
            System.out.println(Arrays.toString(files));
            return Result.success(filesService.mutiFileUpload(files));
        } catch (Exception e) {
            return Result.error("上传文件失败");
        }
    }

    /**
     * 富文本编辑器上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadEditor")
    public JSONObject uploaduploadEditor(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            return filesService.uploadEditor(file);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("errno", 1);
            return res;
        }
    }


    //下载文件
    @GetMapping("/download/{fileName}")
    public void downloads(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        filesService.downloads(fileName, response);
    }

    @PostMapping("/deletefile/{fileName}")
    public Result deleteFile(@PathVariable String fileName) {
        try {
            filesService.deleteFile(fileName);
            return Result.success();
        } catch (Exception error) {
            return Result.error("删除文件失败");
        }
    }

    @PostMapping("/chunk")
    public Result upLoadChunk(@RequestParam("chunk") MultipartFile chunk,
                              @RequestParam("filename") String filename)
    {
        try {
            filesService.upLoadChunk(chunk,filename);
            return Result.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("上传失败");
        }
    }

    @GetMapping("/merge")
    public Result MergeChunk(@RequestParam("hash") String hash,
                                      @RequestParam("filename") String filename) {
        try {
            System.out.println(hash);
            String data=filesService.mergeChunk(hash,filename);
            return Result.success(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("合并失败");
        }
    }

    @PostMapping("/delChunkFile/{fileName}")
    public Result deleteChunkFile(@PathVariable String fileName) {
        try {
            filesService.deleteChunkFile(fileName);
            return Result.success();
        } catch (Exception error) {
            return Result.error("删除文件失败");
        }
    }
}
