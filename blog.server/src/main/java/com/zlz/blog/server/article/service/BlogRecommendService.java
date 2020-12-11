package com.zlz.blog.server.article.service;

import com.zlz.blog.common.entity.article.BlogArticle;
import com.zlz.blog.common.entity.article.BlogRecommend;
import com.zlz.blog.common.response.ResultSet;

import java.util.List;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-08-11 14:44
 * @description
 */
public interface BlogRecommendService {

    /**
     * 热门推荐
     *
     * @param num
     * @return
     */
    ResultSet<List<BlogRecommend>> getHotBlog(Long num);

    /**
     * 获取侧边栏推荐
     *
     * @param num
     * @return
     */
    ResultSet<List<BlogRecommend>> getSideRecommend(Long num);

    /**
     * 获取主页推荐
     *
     * @return
     */
    ResultSet<BlogRecommend> getHomePageRecommend();

    /**
     * 推荐文章
     * @param blogRecommend
     * @return
     */
    ResultSet recommend(BlogRecommend blogRecommend);

    /**
     * 取消推荐
     * @param id
     * @return
     */
    ResultSet cancelRecommend(Long id);

    /**
     * 批量推荐文章
     * @param blogArticles
     * @return
     */
    ResultSet recommendList(List<BlogArticle> blogArticles);
}
