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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${ip:localhost}")
    String ip;

    @Value("${server.port}")
    String port;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "resource";
    private static final String IMAGES_PATH = ROOT_PATH + File.separator + "images";
    private static final String VIDEOS_PATH = ROOT_PATH + File.separator + "videos";
    private static final String FILES_PATH = ROOT_PATH + File.separator + "files";
    //大文件的存储位置
    private static final String CHUNK_PATH = ROOT_PATH + File.separator + "chunks";

    //上传文件
    @Override
    public String upload(MultipartFile file) throws IOException {
        String orginalFilename = file.getOriginalFilename();//文件名称
        String mainName = FileUtil.mainName(orginalFilename);//文件的主名称
        String extName = FileUtil.extName(orginalFilename);//文件的扩展名
        String FILE_PATH = "";
        FILE_PATH = getPath(extName);

        if (!FileUtil.exist(FILE_PATH)) {
            FileUtil.mkdir(FILE_PATH);
        }
        if (FileUtil.exist(FILE_PATH + File.separator + orginalFilename)) {   //如果文件名已存在则随机创建新的文件名
            orginalFilename = System.currentTimeMillis() + "_" + mainName + "." + extName;
        }
        File saveFile = new File(FILE_PATH + File.separator + orginalFilename);
        file.transferTo(saveFile);//存储文件到本地磁盘
        return orginalFilename;
    }

    @Override
    public List<String> mutiFileUpload(MultipartFile[] files) throws IOException {
        List<String> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            String orginalFilename = file.getOriginalFilename();//文件名称
            String mainName = FileUtil.mainName(orginalFilename);//文件的主名称
            String extName = FileUtil.extName(orginalFilename);//文件的扩展名
            String FILE_PATH = "";
            FILE_PATH = getPath(extName);

            if (!FileUtil.exist(FILE_PATH)) {
                FileUtil.mkdir(FILE_PATH);
            }
            if (FileUtil.exist(FILE_PATH + File.separator + orginalFilename)) {   //如果文件名已存在则随机创建新的文件名
                orginalFilename = System.currentTimeMillis() + "_" + mainName + "." + extName;
            }
            File saveFile = new File(FILE_PATH + File.separator + orginalFilename);
            file.transferTo(saveFile);//存储文件到本地磁盘
            fileList.add(orginalFilename);
        }
        return fileList;
    }

    // 加载文件
    @Override
    public void downloads(String fileName, HttpServletResponse response) throws IOException {
        String extName = FileUtil.extName(fileName);//文件的扩展名
        String FILE_PATH = "";
        FILE_PATH = getPath(extName);

        String filePath = FILE_PATH + File.separator + fileName;
        if (!FileUtil.exist(filePath)) {
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void deleteFile(String fileName) {
        String extName = FileUtil.extName(fileName);//文件的扩展名
        String FILE_PATH = "";
        FILE_PATH = getPath(extName);

        File dest = new File(FILE_PATH + File.separator + fileName);
        if (dest.exists()) {
            // 如果文件已经被上传
            dest.delete();
        }
    }

    @Override
    public void deleteChunkFile(String fileName) {
        File dest = new File(CHUNK_PATH + File.separator + fileName);
        if (dest.exists()) {
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
            res.put("errno", 1);
            return res;
        }
        String orginalFilename = file.getOriginalFilename();//文件名称
        String mainName = FileUtil.mainName(orginalFilename);//文件的主名称
        String extName = FileUtil.extName(orginalFilename);//文件的扩展名
        String FILE_PATH = "";
        String resPath = "";

        //判断文件是图片还是视频还是其他文件
        if (isImage(extName)) {
            FILE_PATH = IMAGES_PATH;
            resPath = "http://" + ip + ":" + port + "/resource/images/" + orginalFilename;
        } else if (isVideo(extName)) {
            FILE_PATH = VIDEOS_PATH;
            resPath = "http://" + ip + ":" + port + "/resource/videos/" + orginalFilename;
        } else {
            FILE_PATH = FILES_PATH;
            resPath = "http://" + ip + ":" + port + "/resource/files/" + orginalFilename;
        }

        if (!FileUtil.exist(FILE_PATH)) {
            FileUtil.mkdir(FILE_PATH);
        }
        if (FileUtil.exist(FILE_PATH + File.separator + orginalFilename)) {   //如果文件名已存在则随机创建新的文件名
            orginalFilename = System.currentTimeMillis() + "_" + mainName + "." + extName;
        }
        File saveFile = new File(FILE_PATH + File.separator + orginalFilename);
        file.transferTo(saveFile);//存储文件到本地磁盘

        filePathList.put("url", resPath);
        filePathList.put("alt", orginalFilename);

        res.put("errno", 0);
        res.put("data", filePathList);
        return res;
    }

    //接收上传文件分片
    @Override
    public void upLoadChunk(MultipartFile chunk, String filename) throws IOException {
        // 用于存储文件分片的文件夹
        if (!FileUtil.exist(CHUNK_PATH)) {
            FileUtil.mkdir(CHUNK_PATH);
        }
        // 文件分片的路径
        String filePath = CHUNK_PATH + File.separator + filename;
        File saveFile = new File(filePath);
        // 写入文件中
        FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
        fileOutputStream.write(chunk.getBytes());
        fileOutputStream.close();
        chunk.transferTo(saveFile);
        System.out.println(filename);
    }

    @Override
    public String mergeChunk(String hash, String filename) throws IOException {
        // 文件分片所在的文件夹
        File chunkFileFolder = new File(CHUNK_PATH);
        //如果文件名已存在则随机创建新的文件名
        if (FileUtil.exist(CHUNK_PATH + File.separator + filename)) {
            filename = System.currentTimeMillis() + "_" + filename;
        }
        // 合并后的文件的路径
        File mergeFile = new File(CHUNK_PATH + File.separator + filename);
        // 得到文件分片所在的文件夹下的所有文件
        File[] chunks = chunkFileFolder.listFiles();
        assert chunks != null;
        // 按照hash值过滤出对应的文件分片
        // 排序
        File[] files = Arrays.stream(chunks)
                .filter(file -> file.getName().startsWith(hash))
                // 分片文件命名为"hash值_id.文件后缀名"
                // 按照id值排序
                .sorted(Comparator.comparing(o -> Integer.valueOf(o.getName().split("\\.")[0].split("_")[1])))
                .toArray(File[]::new);

        // 合并文件
        RandomAccessFile randomAccessFileWriter = new RandomAccessFile(mergeFile, "rw");
        byte[] bytes = new byte[1024];
        for (File chunk : files) {
            RandomAccessFile randomAccessFileReader = new RandomAccessFile(chunk, "r");
            int len;
            while ((len = randomAccessFileReader.read(bytes)) != -1) {
                randomAccessFileWriter.write(bytes, 0, len);
            }
            randomAccessFileReader.close();
            chunk.delete();
        }
        randomAccessFileWriter.close();
        return filename;
    }

    //根据文件后缀名获取文件存储路径
    String getPath(String extName)
    {
        if (isImage(extName)) {
            return IMAGES_PATH;
        } else if (isVideo(extName)) {
            return VIDEOS_PATH;
        } else {
            return FILES_PATH;
        }
    }

    //判断格式是否为图片
    Boolean isImage(String extName) {
        if (extName.equalsIgnoreCase("jpg") || extName.equalsIgnoreCase("png") || extName.equalsIgnoreCase("gif") || extName.equalsIgnoreCase("jpeg")) {
            return true;
        } else {
            return false;
        }
    }

    //判断格式是否为视频
    Boolean isVideo(String extName) {
        if (extName.equalsIgnoreCase("mp4") || extName.equalsIgnoreCase("mav")) {
            return true;
        } else {
            return false;
        }
    }
}
