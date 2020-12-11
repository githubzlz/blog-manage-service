package com.zlz.blog.server.article.service;

import com.zlz.blog.common.entity.article.BlogContent;
import com.zlz.blog.common.response.ResultSet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-05-25 15:59
 * @description 文章正文的操作
 */
public interface ArticleContentService {

    /**
     * 插入文章正文
     *
     * @param content content
     * @param request request
     * @return ResultSet
     */
    ResultSet insertBody(BlogContent content, HttpServletRequest request);

    /**
     * 查询文章正文
     *
     * @param blogId  blogId
     * @param request request
     * @return ResultSet
     */
    ResultSet queryBody(Long blogId, HttpServletRequest request);

    /**
     * 修改文章正文
     *
     * @param blogContent BlogContent
     * @param request     request
     * @return ResultSet
     */
    ResultSet updateBody(BlogContent blogContent, HttpServletRequest request);
}
