package com.zlz.blog.server.article.service;

import com.zlz.blog.common.entity.article.BlogArticle;
import com.zlz.blog.common.response.ResultSet;

import javax.servlet.http.HttpServletRequest;

/**
 * 文章操作的service接口
 *
 * @author zhulinzhong
 * @version 1.0 CreateTime:2019/12/20 17:34
 */
public interface ArticleService {

    /**
     * 新增文章
     *
     * @param article article
     * @param request request
     * @return ResultSet
     */
    ResultSet insertArticle(BlogArticle article, HttpServletRequest request);

    /**
     * 修改文章
     *
     * @param article article
     * @param request request
     * @return ResultSet
     */
    ResultSet updateArticle(BlogArticle article, HttpServletRequest request);

    /**
     * 修改文章
     *
     * @param article article
     * @param request request
     * @return ResultSet
     */
    ResultSet updateArticleTitle(BlogArticle article, HttpServletRequest request);

    /**
     * 查看文章信息
     *
     * @param id      id
     * @param request 请求信息
     * @return ResultSet
     */
    ResultSet queryArticle(Long id, HttpServletRequest request);

    /**
     * 查看所有文章信息
     *
     * @param blogArticle 分页信息
     * @param request     请求信息
     * @return ResultSet
     */
    ResultSet selectList(BlogArticle blogArticle, HttpServletRequest request);

    /**
     * 删除文章(逻辑删除)
     *
     * @param id      id
     * @param request request
     * @return ResultSet
     */
    ResultSet deleteArticle(Long id, HttpServletRequest request);

    /**
     * 撤回已经删除的文章
     *
     * @param id
     * @param request
     * @return
     */
    ResultSet revokeDeletedArticle(Long id, HttpServletRequest request);

    /**
     * 查询所有文章
     * @param param
     * @param request
     * @return
     */
    ResultSet searchAll(String param, HttpServletRequest request);
}
