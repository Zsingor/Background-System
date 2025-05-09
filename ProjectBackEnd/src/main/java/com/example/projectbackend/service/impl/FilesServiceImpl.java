package com.example.projectbackend.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.projectbackend.service.FilesService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${ip:localhost}")
    String ip;

    @Value("${server.port}")
    String port;

    private final static int BUFFER_SIZE = 1024 * 1024 * 20;
    //分片大小
    private final static int CHUNK_SIZE = 1024 * 1024 * 2;

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
    }

    //合并分片文件
    @Override
    public String mergeChunk(String filename, JSONArray hashlist) throws IOException {
        // 文件分片所在的文件夹
        File chunkFileFolder = new File(CHUNK_PATH);
        //如果文件名已存在则随机创建新的文件名
        if (FileUtil.exist(CHUNK_PATH + File.separator + filename)) {
            filename = System.currentTimeMillis() + "_" + filename;
        }
        // 合并后的文件的路径
        File mergeFile = new File(CHUNK_PATH + File.separator + filename);

        List<String> hashList = JSONObject.parseArray(hashlist.toJSONString(), String.class);

        // 得到文件分片所在的文件夹下的所有文件
        File[] chunks = chunkFileFolder.listFiles();
        assert chunks != null;
        // 按照hash值过滤出对应的文件分片
        File[] files = filterFilesByHashList(hashList, chunks);
        // 排序
//        File[] files = Arrays.stream(chunks)
//                .filter(file -> file.getName().startsWith(hash))
//                // 分片文件命名为"hash值_id.文件后缀名"
//                // 按照id值排序
//                .sorted(Comparator.comparing(o -> Integer.valueOf(o.getName().split("\\.")[0].split("_")[1])))
//                .toArray(File[]::new);

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

    @Override
    public void downloadChunk(String rangeHeader, HttpServletResponse response, String fileName) throws IOException {
        String filePath = FILES_PATH + File.separator + fileName;
//        String filePath = "D:/zhaiyan/files/aaaa.csv";
        File file = new File(filePath);
        long fileSize = file.length();

        // 设置文件的基本信息
        response.setContentType("application/octect-stream;charset=UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Accept-Ranges", "bytes");

        if (rangeHeader == null) {
            // 下载全部文件
            response.setHeader("Content-Length", fileSize + "-" + CHUNK_SIZE);
            InputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[(int) fileSize];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                out.flush();
            }
            in.close();
            out.close();
        } else {
            // Download partial content
            long start = 0;
            long end;
            String[] range = rangeHeader.split("=")[1].split("-");
            if (range.length == 1) {
                start = Long.parseLong(range[0]);
                end = fileSize - 1;
            } else {
                start = Long.parseLong(range[0]);
                end = Long.parseLong(range[1]);
            }
            long contentLength = end - start + 1;
            // 返回头里存放每次读取的开始和结束字节
            response.setHeader("Content-Length", String.valueOf(contentLength));
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);
            InputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
            // 跳到第start字节
            in.skip(start);
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            long bytesWritten = 0;
            while ((bytesRead = in.read(buffer)) != -1) {
                if (bytesWritten + bytesRead > contentLength) {
                    out.write(buffer, 0, (int) (contentLength - bytesWritten));
                    break;
                } else {
                    out.write(buffer, 0, bytesRead);
                    bytesWritten += bytesRead;
                }
            }
            in.close();
            out.close();
        }
    }

    //根据文件后缀名获取文件存储路径
    String getPath(String extName) {
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

    //根据文件名列表筛选出顺序的文件列表
    public static File[] filterFilesByHashList(List<String> hashList, File[] chunks) {
        List<File> filteredFiles = new ArrayList<>();
        for (String hash : hashList) {
            try {
                for (File chunk : chunks) {
                    if (chunk.getName().equals(hash)) {
                        filteredFiles.add(chunk);
                        break;
                    }
                }
            } catch (Exception e) {
                // 处理解析JSON时可能发生的异常，这里简单打印堆栈跟踪
                e.printStackTrace();
            }
        }

        return filteredFiles.toArray(new File[0]);
    }
}
