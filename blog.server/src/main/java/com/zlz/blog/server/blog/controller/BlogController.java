package com.zlz.blog.server.blog.controller;

import com.zlz.blog.common.entity.blog.Blog;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.blog.service.BlogAttachFileService;
import com.zlz.blog.server.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * 文章操作的控制器层
 *
 * @author zhulinzhong
 * @version 1.0 CreateTime:2019/12/20 17:37
 */
@RequestMapping("/blog")
@RestController
public class BlogController {

    private BlogService blogService;

    private BlogAttachFileService blogAttachFileService;

    @Autowired
    public void setBlogAttachFileService(BlogAttachFileService blogAttachFileService){
        this.blogAttachFileService = blogAttachFileService;
    }
    @Autowired
    public void setBlogService(BlogService blogService){
        this.blogService = blogService;
    }

    /**
     * 新增文章
     * @param blog
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ResultSet<Blog> insertArticle(@RequestBody Blog blog, HttpServletRequest request) {
        return blogService.insertBlog(blog, request);
    }

    /**
     * 修改文章
     * @param blog
     * @param request
     * @return
     */
    @PostMapping("/update")
    public ResultSet<Blog> updateArticle(@RequestBody Blog blog, HttpServletRequest request) {
        return blogService.updateBlog(blog, request);
    }

    /**
     * 修改文章（仅标题摘要）
     * @param blog
     * @param request
     * @return
     */
    @PostMapping("/titleorsummary")
    public ResultSet<Blog> updateArticleTitle(@RequestBody Blog blog, HttpServletRequest request) {
        return blogService.updateBlogTitle(blog, request);
    }

    /**
     * 分页查询,模糊查询
     * @param blog
     * @param request
     * @return
     */
    @PostMapping("/list")
    public ResultSet<Blog> selectAll(@RequestBody Blog blog, HttpServletRequest request) {
        return blogService.selectList(blog, request);
    }

    /**
     * 查询一篇文章
     *
     * @param id      id
     * @param request request
     * @return return
     */
    @GetMapping("/queryarticle/{id}")
    public ResultSet<Blog> selectArticle(@PathVariable("id") Long id, HttpServletRequest request) {
        return blogService.queryBlogById(id, request);
    }

    /**
     * 逻辑删除文章
     *
     * @param id      id
     * @param request request
     * @return ResultSet<Blog>
     */
    @GetMapping("/remove/{id}")
    public ResultSet<Blog> deleteArticle(@PathVariable("id") Long id, HttpServletRequest request) {
        return blogService.deleteBlog(id, request);
    }

    /**
     * 恢复已经删除的文章
     *
     * @param id      id
     * @param request request
     * @return ResultSet<Blog>
     */
    @GetMapping("/remove/revoke/{id}")
    public ResultSet<Blog> revokeDeletedArticle(@PathVariable("id") Long id, HttpServletRequest request) {
        return blogService.revokeDeletedBlog(id, request);
    }

//    /**
//     * 查询所有未推荐的文章
//     * @param param
//     * @param request
//     * @return
//     */
//    @GetMapping("/search/all")
//    public ResultSet<Blog> searchAll(@RequestParam(required = false) String param, HttpServletRequest request) {
//        return blogService.searchAll(param, request);
//    }
    /**
     * 上传图片，返回图片地址到前端
     *
     * @param file 文件
     * @param request  request
     * @throws IOException IOException
     */
    @PostMapping("/image/upload")
    public ResultSet upload(MultipartFile file, HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        return blogAttachFileService.uploadImage(file);
    }

    /**
     * 下载文件
     *
     * @param path     文件路径
     * @param response response
     */
    @PostMapping("/download")
    public void download(String path, HttpServletResponse response) {
        blogAttachFileService.downloadFile(path, response);
    }
}