package com.zlz.blog.server.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlz.blog.common.entity.article.BlogContent;
import com.zlz.blog.common.entity.common.LoginUser;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.common.util.TokenUtil;
import com.zlz.blog.server.article.mapper.ArticleContentMapper;
import com.zlz.blog.server.article.service.ArticleContentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-05-25 16:00
 * @description
 */
@Service
public class ArticleContentServiceImpl implements ArticleContentService {

    @Resource
    private ArticleContentMapper contentMapper;

    @Override
    public ResultSet insertBody(BlogContent content, HttpServletRequest request) {

        //数据检查
        if (null == content || null == content.getBlogId()) {
            return ResultSet.inputError();
        }
        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (StringUtils.isEmpty(loginUser.getUsername())) {
            return ResultSet.error("未找到登录用户");
        }

        //补全数据
        content.setCreatedTime(new Date());
        content.setLastModifiedTime(new Date());
        content.setCreator(loginUser.getUsername());
        content.setLastModifier(loginUser.getUsername());

        //持久层数据处理并返回结果
        return SqlResultUtil.isOneRow(contentMapper.insert(content));
    }

    @Override
    public ResultSet queryBody(Long blogId, HttpServletRequest request) {

        //数据检查
        if (StringUtils.isEmpty(blogId)) {
            return ResultSet.inputError();
        }
        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (StringUtils.isEmpty(loginUser.getUsername())) {
            return ResultSet.outError("错误,获取登陆用户失败");
        }

        //查询数据
        QueryWrapper<BlogContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id", blogId);
        BlogContent blogContent = contentMapper.selectOne(queryWrapper);

        if (null == blogContent) {
            return ResultSet.outError("没有该查询到该文章的记录");
        }
        return ResultSet.success(blogContent);
    }

    @Override
    public ResultSet updateBody(BlogContent blogContent, HttpServletRequest request) {

        //数据检查
        if (null == blogContent.getBlogId()) {
            return ResultSet.inputError();
        }
        //获取登陆人
        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (StringUtils.isEmpty(loginUser.getUsername())) {
            return ResultSet.error();
        }

        // >> 10 = * 2的10次方
        if (null != blogContent.getContentHtml()) {
            double htSize = blogContent.getContentHtml().getBytes().length >> 10;
            blogContent.setHtmlSize(htSize + "KB");
        }
        if (null != blogContent.getContentMd()) {
            double mdSize = blogContent.getContentMd().getBytes().length >> 10;
            blogContent.setMdSize(mdSize + "KB");
        }

        //补全数据
        blogContent.setLastModifiedTime(new Date());
        blogContent.setLastModifier(loginUser.getUsername());

        //设置查询条件
        QueryWrapper<BlogContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id", blogContent.getBlogId());
        return SqlResultUtil.isOneRow(contentMapper.update(blogContent, queryWrapper));
    }
}
