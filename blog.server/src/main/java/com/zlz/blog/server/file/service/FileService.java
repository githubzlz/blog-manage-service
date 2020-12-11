package com.zlz.blog.server.file.service;

import com.zlz.blog.common.response.ResultSet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/13 13:59
 */
public interface FileService {

    /**
     * 上传图片
     *
     * @param file 文件
     * @return 上传文件返回消息封装的对象
     * @throws IOException io异常
     */
    ResultSet uploadImage(MultipartFile file) throws IOException, NoSuchAlgorithmException;

    /**
     * 下载文件
     *
     * @param path     路径
     * @param response 响应
     */
    void downloadFile(String path, HttpServletResponse response);

    /**
     * 上传md获取文档
     *
     * @param response response
     * @param request  request
     */
    ResultSet getMdString(HttpServletResponse response, HttpServletRequest request);

    /**
     * 下载md文件
     *
     * @param id       id
     * @param response response
     * @param request  request
     */
    void downloadMd(Long id, HttpServletResponse response, HttpServletRequest request);

    /**
     * 下载html文件
     *
     * @param id       id
     * @param response response
     * @param request  request
     */
    void downloadHtml5(Long id, HttpServletResponse response, HttpServletRequest request);
}
