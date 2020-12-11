package com.zlz.blog.server.article.service;

import com.zlz.blog.common.entity.article.BlogPublicInfo;
import com.zlz.blog.common.response.ResultSet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-05-26 11:58
 * @description 文章信息相关
 */
public interface ArticlePublicInfoService {

    /**
     * 插入文章相关信息
     *
     * @param request        request
     * @param blogPublicInfo blogPublicInfo
     * @return ResultSet
     */
    ResultSet insertPublicInfo(BlogPublicInfo blogPublicInfo, HttpServletRequest request);

    /**
     * 查询文章信息
     *
     * @param blogId blogId
     * @return ResultSet
     */
    ResultSet queryPublicInfo(Long blogId);
}
