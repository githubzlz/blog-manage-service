package com.zlz.blog.server.article.controller;

import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.article.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-07-07 11:17
 * @description 文章标签
 */
@RequestMapping("/article/tags")
@RestController
public class ArticleTagController {

    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 查询标签分类
     *
     * @return
     */
    @GetMapping("querytagtypelist")
    public ResultSet getTagTypeList() {
        return articleTagService.getTagTypeList();
    }

    /**
     * 根据类查询标签
     *
     * @param typeId
     * @return
     */
    @GetMapping("querytaglist/{typeId}")
    public ResultSet getTagList(@PathVariable("typeId") Long typeId, HttpServletRequest request) {
        return articleTagService.getTagList(typeId, request);
    }
}
