package com.zlz.blog.server.article.service;

import com.zlz.blog.common.entity.article.BlogTag;
import com.zlz.blog.common.response.ResultSet;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-05-25 16:07
 * @description 标签
 */
public interface ArticleTagService {

    /**
     * 插入标签
     *
     * @param blogTag 标签实体
     * @param request request
     * @return request
     */
    ResultSet insertTag(BlogTag blogTag, HttpServletRequest request);

    /**
     * 批量插入标签
     *
     * @param blogTags 标签实体
     * @param request  request
     * @return request
     */
    ResultSet insertTagList(List<BlogTag> blogTags, HttpServletRequest request);

    /**
     * 查询标签的类型
     *
     * @return
     */
    ResultSet getTagTypeList();

    /**
     * 根据标签类型查询标签
     *
     * @param typeId
     * @param request
     * @return
     */
    ResultSet getTagList(Long typeId, HttpServletRequest request);
}
