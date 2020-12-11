package com.zlz.blog.server.file.controller;

import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.file.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/13 14:00
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private final Logger log = LoggerFactory.getLogger(FileController.class);

    @Resource
    private FileService fileService;

    /**
     * 上传图片，返回图片地址到前端
     *
     * @param file 文件
     * @param request  request
     * @throws IOException IOException
     */
    @PostMapping("/image/upload")
    public ResultSet upload(MultipartFile file, HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        return fileService.uploadImage(file);
    }

    /**
     * 下载文件
     *
     * @param path     文件路径
     * @param response response
     */
    @PostMapping("/download")
    public void download(String path, HttpServletResponse response) {
        fileService.downloadFile(path, response);
    }

    /**
     * 导入md文件
     *
     * @param response response
     * @param request  request
     */
    @GetMapping("/getString")
    public ResultSet getMdString(HttpServletResponse response, HttpServletRequest request) {
        return fileService.getMdString(response, request);
    }

    /**
     * 下载md文件
     *
     * @param id       id
     * @param response response
     * @param request  request
     */
    @GetMapping("/download/markdown/{id}")
    public void downloadMd(@PathVariable("id") Long id, HttpServletResponse response, HttpServletRequest request) {
        fileService.downloadMd(id, response, request);
    }

    /**
     * 下载html文件
     *
     * @param id       id
     * @param response response
     * @param request  request
     */
    @GetMapping("/download/html5/{id}")
    public void downloadHtml5(@PathVariable("id") Long id, HttpServletResponse response, HttpServletRequest request) {
        fileService.downloadHtml5(id, response, request);
    }
}
