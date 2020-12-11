package com.zlz.blog.server.article.service;

import com.zlz.blog.common.response.ResultSet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-06-24 17:10
 * @description
 */
public interface ArticleTypeService {

    /**
     * 创建一个文章类型
     *
     * @param pId
     * @param name
     * @param request
     * @return
     */
    ResultSet createType(Long pId, String name, Integer type, HttpServletRequest request);

    /**
     * 查询文章类型
     *
     * @param request
     * @param operate
     * @return
     */
    ResultSet queryBlogType(HttpServletRequest request, Integer operate, Integer type);

    /**
     * 修改分类状态
     *
     * @param id
     * @param state
     * @param request
     * @return
     */
    ResultSet updateState(Long id, Integer state, HttpServletRequest request);

    /**
     * 删除文章分类
     *
     * @param id
     * @param request
     * @return
     */
    ResultSet deleteState(Long id, HttpServletRequest request);

    /**
     * 修改分类名称
     *
     * @param id
     * @param request
     * @return
     */
    ResultSet updateTypeName(Long id, String name, HttpServletRequest request);
}
