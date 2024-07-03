package com.example.springtest.controller;

import cn.hutool.core.io.FileUtil;
import com.example.springtest.entity.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/files")
public class FilesController {
    @Value("${ip:localhost}")
    String ip;

    @Value("${server.port}")
    String port;

    private static final String ROOT_PATH=System.getProperty("user.dir")+ File.separator+"resource";

    @PostMapping( "/upload")
    public Result upload(MultipartFile file) throws IOException {
        String orginalFilename=file.getOriginalFilename();//文件名称
        String mainName= FileUtil.mainName(orginalFilename);//文件的主名称
        String extName=FileUtil.extName(orginalFilename);//文件的扩展名
        if(!FileUtil.exist(ROOT_PATH))
        {
            FileUtil.mkdir(ROOT_PATH);
        }
        if (FileUtil.exist(ROOT_PATH+File.separator+orginalFilename))
        {   //如果文件名已存在则随机创建新的文件名
            orginalFilename=System.currentTimeMillis()+"_"+mainName+"."+extName;
        }
        File saveFile=new File(ROOT_PATH+File.separator+orginalFilename);
        file.transferTo(saveFile);//存储文件到本地磁盘
        return Result.success(orginalFilename);
    }

    @GetMapping("/download/{fileName}")
    public void downloads(@PathVariable String fileName, HttpServletResponse response) throws IOException
    {
        String filePath=ROOT_PATH+ File.separator+fileName;
        if(!FileUtil.exist(filePath))
        {
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream=response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping("deletefile/{fileName}")
    public void deleteFile(@PathVariable String fileName){
        File dest = new File(ROOT_PATH +File.separator+ fileName);
        if(dest.exists())
        {
            // 如果文件已经被上传
            dest.delete();
        }
    }
}
