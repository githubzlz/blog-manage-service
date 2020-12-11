package com.zlz.blog.server.article.controller;

import com.zlz.blog.common.entity.article.BlogArticle;
import com.zlz.blog.common.entity.article.BlogRecommend;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.article.service.BlogRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-08-11 14:43
 * @description 推荐列表
 */
@RestController
@RequestMapping("/blog/recommend")
public class BlogRecommendController {

    @Autowired
    private BlogRecommendService blogRecommendService;

    /**
     * 热门推荐
     *
     * @return
     */
    @GetMapping("/hot")
    public ResultSet<List<BlogRecommend>> getHotBlog(@PathVariable(required = false) Long num) {
        return blogRecommendService.getHotBlog(num);
    }

    /**
     * 主页推荐
     *
     * @return
     */
    @GetMapping("homepage")
    public ResultSet<BlogRecommend> getHomePageRecommend() {
        return blogRecommendService.getHomePageRecommend();
    }

    /**
     * 侧栏推荐
     *
     * @param num
     * @return
     */
    @GetMapping("/side")
    public ResultSet<List<BlogRecommend>> sideRecommend(@RequestParam(required = false) Long num) {
        return blogRecommendService.getSideRecommend(num);
    }

    /**
     * 推荐文章
     * @param blogRecommend
     * @return
     */
    @PostMapping("/recommend")
    public ResultSet recommend(@RequestBody BlogRecommend blogRecommend){
        return blogRecommendService.recommend(blogRecommend);
    }

    /**
     * 取消推荐
     * @param id
     * @return
     */
    @GetMapping("/cancel/{id}")
    public ResultSet cancelRecommend(@PathVariable("id") Long id){
        return blogRecommendService.cancelRecommend(id);
    }

    /**
     * 批量推荐文章
     * @param blogArticles
     * @return
     */
    @PostMapping("list")
    public ResultSet recommendList(@RequestBody List<BlogArticle> blogArticles){
        return blogRecommendService.recommendList(blogArticles);
    }
}
