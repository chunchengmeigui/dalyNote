package com.neo.controller;

import com.neo.utils.BaseResponseEntity;
import com.neo.utils.JsonUtil;
import com.neo.utils.UuidUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UploadController {

    @Value("${filePath}")
    private String UPLOADED_FOLDER;


    //    单文件上传
    @PostMapping(value = "/upload")
    @ResponseBody
    public String singleFileUpload(@RequestParam("file") MultipartFile file) {
        Map<String, String> map = new HashMap<>();
        String upPath = "";
        if (file.isEmpty()) {
            map.put("data", "文件空");
            return JsonUtil.toJSon(new BaseResponseEntity(map));
        } else {
            upPath = createDir(UPLOADED_FOLDER);
        }
        try {
            byte[] bytes = file.getBytes();
            // 重新生成文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            Path path = Paths.get(upPath + UuidUtil.getUUID() + suffixName);
            Files.write(path, bytes);
            System.out.println(file.getOriginalFilename());
            map.put("data", file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonUtil.toJSon(new BaseResponseEntity(map));
    }


    //多文件上传
    @PostMapping("/many")
    @ResponseBody
    public String singleFileUpload2(@RequestParam("files") MultipartFile[] files) {
        Map<String, String> map = new HashMap<>();
        String upPath = "";
        if (files.length == 0) {
            map.put("data", "文件空");
            return JsonUtil.toJSon(new BaseResponseEntity(map));
        } else {
            upPath = createDir(UPLOADED_FOLDER);
        }
        List<String> list = new ArrayList<>();
        try {
            for (int i = 0; i < files.length; i++) {
                byte[] bytes = files[i].getBytes();
                // 重新生成文件名
                String fileName = files[i].getOriginalFilename();
                // 获取文件的后缀名
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                Path path = Paths.get(upPath + UuidUtil.getUUID() + suffixName);
                Files.write(path, bytes);
                list.add(files[i].getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonUtil.toJSon(new BaseResponseEntity(list, list.size()));
    }


    /**
     * description:创建保存文件路径
     * param:
     * return:
     * author: CDN
     * date: 2019/6/4
     */
    public String createDir(String path) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String pathDate = path + simpleDateFormat.format(new Date()) + "/";
        if (!new File(pathDate).exists()) {
            new File(pathDate).mkdirs();
        }
        return pathDate;
    }

    //文件下载相关代码

}