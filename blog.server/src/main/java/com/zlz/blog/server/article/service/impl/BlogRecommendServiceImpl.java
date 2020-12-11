package com.zlz.blog.server.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.zlz.blog.common.entity.article.BlogArticle;
import com.zlz.blog.common.entity.article.BlogRecommend;
import com.zlz.blog.common.enums.article.RecommendTypeEnum;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.server.article.mapper.ArticleMapper;
import com.zlz.blog.server.article.mapper.BlogRecommendMapper;
import com.zlz.blog.server.article.service.BlogRecommendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-08-11 14:44
 * @description
 */
@Service
public class BlogRecommendServiceImpl implements BlogRecommendService {

    @Resource
    private BlogRecommendMapper blogRecommendMapper;
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public ResultSet<List<BlogRecommend>> getHotBlog(Long num) {
        return ResultSet.success("查询成功", articleMapper.selectListOrderByHot(num));
    }

    @Override
    public ResultSet<List<BlogRecommend>> getSideRecommend(Long num) {
        List<BlogArticle> blogArticles = articleMapper.selectBlogRecommend(0);
        return ResultSet.success(blogArticles);
    }

    @Override
    public ResultSet<BlogRecommend> getHomePageRecommend() {
        List<BlogArticle> blogArticles = articleMapper.selectBlogRecommend(1);
        return ResultSet.success(blogArticles);
    }

    @Override
    public ResultSet recommend(BlogRecommend blogRecommend){

        if(null == blogRecommend || null == blogRecommend.getBlogId() || null == blogRecommend.getRecommendType()){
            return ResultSet.error();
        }
        blogRecommend.setCreatedTime(new Date());
        blogRecommend.setLastModifiedTime(new Date());
        blogRecommend.setCreator("zlz");
        blogRecommend.setLastModifier("zlz");
        int insert = blogRecommendMapper.insert(blogRecommend);
        return SqlResultUtil.isOneRow(insert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultSet cancelRecommend(Long id){
        int de = blogRecommendMapper.deleteById(id);
        return SqlResultUtil.isOneRow(de);
    }

    @Override
    public ResultSet recommendList(List<BlogArticle> blogArticles) {

        //数据检查
         if(null == blogArticles || blogArticles.isEmpty()){
             return ResultSet.inputError();
         }

         //构造推荐数据
         List<BlogRecommend> recommends = new ArrayList<>();
        for (BlogArticle blog : blogArticles) {
            BlogRecommend blogRecommend = blog.getBlogRecommend();
            if(null == blog.getId()){
                return ResultSet.inputError();
            }
            if(null == blogRecommend){
                return ResultSet.inputError();
            }
            blogRecommend.setBlogId(blog.getId());
            blogRecommend.setCreator("默认");
            blogRecommend.setLastModifier("默认");
            blogRecommend.setCreatedTime(new Date());
            blogRecommend.setLastModifiedTime(new Date());
            blogRecommend.setId(IdWorker.getId());
            recommends.add(blogRecommend);
        }

        //插入数据
        Integer integer = blogRecommendMapper.insertList(recommends);
        if(integer == recommends.size()){
            return ResultSet.success();
        }
        return ResultSet.error();
    }
}
