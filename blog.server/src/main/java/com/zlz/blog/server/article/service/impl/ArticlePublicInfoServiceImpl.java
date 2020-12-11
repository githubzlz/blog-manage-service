package com.zlz.blog.server.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlz.blog.common.entity.article.BlogPublicInfo;
import com.zlz.blog.common.entity.common.LoginUser;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.common.util.TokenUtil;
import com.zlz.blog.server.article.mapper.ArticlePublicInfoMapper;
import com.zlz.blog.server.article.service.ArticlePublicInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-05-26 11:59
 * @description 文章信息相关
 */
@Service
public class ArticlePublicInfoServiceImpl implements ArticlePublicInfoService {

    @Resource
    private ArticlePublicInfoMapper publicInfoMapper;

    @Override
    public ResultSet insertPublicInfo(BlogPublicInfo blogPublicInfo, HttpServletRequest request) {

        //数据检查
        if (null == blogPublicInfo || null == blogPublicInfo.getBlogId()) {
            return ResultSet.inputError();
        }
        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (StringUtils.isEmpty(loginUser.getUsername())) {
            return ResultSet.error("未找到登录用户");
        }

        //补全数据
        blogPublicInfo.setLastModifiedTime(new Date());
        blogPublicInfo.setCreatedTime(new Date());
        blogPublicInfo.setLastModifier(loginUser.getUsername());
        blogPublicInfo.setCreator(loginUser.getUsername());

        return SqlResultUtil.isOneRow(publicInfoMapper.insert(blogPublicInfo));
    }

    @Override
    public ResultSet queryPublicInfo(Long blogId) {

        //设置查询条件
        QueryWrapper<BlogPublicInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id", blogId)
                .last("limit 1");
        BlogPublicInfo blogPublicInfo = publicInfoMapper.selectOne(queryWrapper);

        //检查并返回数据
        if (null == blogPublicInfo) {
            return ResultSet.outError();
        }
        return ResultSet.success(blogPublicInfo);
    }
}
