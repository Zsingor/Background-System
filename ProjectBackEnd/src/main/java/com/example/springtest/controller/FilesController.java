package com.example.springtest.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Result;
import com.example.springtest.service.FilesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files")
public class FilesController {
    @Autowired
    private FilesService filesService;

    //文件上传
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        try {
            System.out.println(file.getOriginalFilename());
            return Result.success(filesService.upload(file));
        } catch (Exception e) {
            return Result.error("上传文件失败");
        }
    }

    //多文件上传
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

    //删除文件
    @PostMapping("/deletefile/{fileName}")
    public Result deleteFile(@PathVariable String fileName) {
        try {
            filesService.deleteFile(fileName);
            return Result.success();
        } catch (Exception error) {
            return Result.error("删除文件失败");
        }
    }

    //接收文件分片
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

    //合并文件分片
    @PostMapping("/merge")
    public Result MergeChunk(@RequestBody String json) {
        try {
            JSONObject jsonObject= JSON.parseObject(json);
            String filename=jsonObject.getString("filename");
            JSONArray hashList=jsonObject.getJSONArray("chunkList");

            System.out.println(filename);
            System.out.println(hashList);
            String data=filesService.mergeChunk(filename,hashList);
            return Result.success(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("合并失败");
        }
    }

    //删除合并后的文件
    @PostMapping("/delChunkFile/{fileName}")
    public Result deleteChunkFile(@PathVariable String fileName) {
        try {
            filesService.deleteChunkFile(fileName);
            return Result.success();
        } catch (Exception error) {
            return Result.error("删除文件失败");
        }
    }

    @GetMapping("/downloadChunk")
    public void downloadChunk(HttpServletRequest request, HttpServletResponse response,@RequestParam("filename") String filename) throws IOException {
        filesService.downloadChunk(request.getHeader("Range"),response,filename);
    }
}
