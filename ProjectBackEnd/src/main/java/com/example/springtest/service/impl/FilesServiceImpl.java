package com.example.springtest.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.springtest.entity.Result;
import com.example.springtest.service.FilesService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${ip:localhost}")
    String ip;

    @Value("${server.port}")
    String port;

    private static final String IMAGE_PATH=System.getProperty("user.dir")+ File.separator+"resource"+ File.separator+"images";
    private static final String VIDEO_PATH=System.getProperty("user.dir")+ File.separator+"resource"+ File.separator+"videos";

    //上传文件
    @Override
    public String upload(MultipartFile file) throws IOException {
        String orginalFilename=file.getOriginalFilename();//文件名称
        String mainName= FileUtil.mainName(orginalFilename);//文件的主名称
        String extName=FileUtil.extName(orginalFilename);//文件的扩展名
        String FILE_PATH="";
        //判断文件是图片还是视频
        if (isImage(extName))
        {
            FILE_PATH=IMAGE_PATH;
        }
        else if (isVideo(extName))
        {
            FILE_PATH=VIDEO_PATH;
        }

        if(!FileUtil.exist(FILE_PATH))
        {
            FileUtil.mkdir(FILE_PATH);
        }
        if (FileUtil.exist(FILE_PATH+File.separator+orginalFilename))
        {   //如果文件名已存在则随机创建新的文件名
            orginalFilename=System.currentTimeMillis()+"_"+mainName+"."+extName;
        }
        File saveFile=new File(FILE_PATH+File.separator+orginalFilename);
        file.transferTo(saveFile);//存储文件到本地磁盘
        return orginalFilename;
    }

    // 加载文件
    @Override
    public void downloads(String fileName, HttpServletResponse response) throws IOException {
        String extName=FileUtil.extName(fileName);//文件的扩展名
        String FILE_PATH="";
        //判断文件是图片还是视频
        if (isImage(extName))
        {
            FILE_PATH=IMAGE_PATH;
        }
        else if (isVideo(extName))
        {
            FILE_PATH=VIDEO_PATH;
        }

        String filePath=FILE_PATH+ File.separator+fileName;
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

    @Override
    public void deleteFile(String fileName) {
        String extName=FileUtil.extName(fileName);//文件的扩展名
        String FILE_PATH="";
        //判断文件是图片还是视频
        if (isImage(extName))
        {
            FILE_PATH=IMAGE_PATH;
        }
        else if (isVideo(extName))
        {
            FILE_PATH=VIDEO_PATH;
        }

        File dest = new File(FILE_PATH +File.separator+ fileName);
        if(dest.exists())
        {
            // 如果文件已经被上传
            dest.delete();
        }
    }

    //上传富文本的文件
    @Override
    public JSONObject uploadEditor(MultipartFile file) throws IOException {
        JSONObject filePathList = new JSONObject();
        JSONObject res = new JSONObject();
        if (file == null) {
            res.put("errno",1);
            return res;
        }
        String orginalFilename=file.getOriginalFilename();//文件名称
        String mainName= FileUtil.mainName(orginalFilename);//文件的主名称
        String extName=FileUtil.extName(orginalFilename);//文件的扩展名
        String FILE_PATH="";
        String resPath="";

        //判断文件是图片还是视频
        if (isImage(extName))
        {
            FILE_PATH=IMAGE_PATH;
            resPath="http://"+ip+":"+port+"/resource/images/"+orginalFilename;
        }
        else if (isVideo(extName))
        {
            FILE_PATH=VIDEO_PATH;
            resPath="http://"+ip+":"+port+"/resource/videos/"+orginalFilename;
        }

        if(!FileUtil.exist(FILE_PATH))
        {
            FileUtil.mkdir(FILE_PATH);
        }
        if (FileUtil.exist(FILE_PATH+File.separator+orginalFilename))
        {   //如果文件名已存在则随机创建新的文件名
            orginalFilename=System.currentTimeMillis()+"_"+mainName+"."+extName;
        }
        File saveFile=new File(FILE_PATH+File.separator+orginalFilename);
        file.transferTo(saveFile);//存储文件到本地磁盘

        filePathList.put("url",resPath);
        filePathList.put("alt",orginalFilename);

        res.put("errno",0);
        res.put("data",filePathList);
        return res;
    }

    Boolean isImage(String extName)
    {
        if (extName.equalsIgnoreCase("jpg")||extName.equalsIgnoreCase("png")||extName.equalsIgnoreCase("gif")||extName.equalsIgnoreCase("jpeg"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    Boolean isVideo(String extName)
    {
        if (extName.equalsIgnoreCase("mp4")||extName.equalsIgnoreCase("mav"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
