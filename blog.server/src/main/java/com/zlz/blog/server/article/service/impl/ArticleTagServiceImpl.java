package com.zlz.blog.server.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.zlz.blog.common.constants.article.TagTypeConstants;
import com.zlz.blog.common.entity.article.BlogTag;
import com.zlz.blog.common.entity.article.BlogTagClasses;
import com.zlz.blog.common.entity.common.LoginUser;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.common.util.TokenUtil;
import com.zlz.blog.server.article.mapper.ArticleTagClassesMapper;
import com.zlz.blog.server.article.mapper.ArticleTagMapper;
import com.zlz.blog.server.article.service.ArticleTagService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-05-25 16:07
 * @description 标签
 */
@Service
public class ArticleTagServiceImpl implements ArticleTagService {

    @Resource
    private ArticleTagMapper tagMapper;
    @Resource
    private ArticleTagClassesMapper articleTagClassesMapper;

    @Override
    public ResultSet insertTag(BlogTag blogTag, HttpServletRequest request) {

        //数据检查
        if (null == blogTag || StringUtils.isEmpty(blogTag.getName())) {
            return ResultSet.inputError();
        }
        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (loginUser.getUsername() == null) {
            ResultSet.error("未找到登录用户");
        }

        //数据补全
        blogTag.setLastModifiedTime(new Date());
        blogTag.setLastModifier(loginUser.getUsername());
        blogTag.setCreatedTime(new Date());
        blogTag.setCreator(loginUser.getUsername());

        //插入标签
        return SqlResultUtil.isOneRow(tagMapper.insert(blogTag));
    }

    @Override
    public ResultSet insertTagList(List<BlogTag> blogTags, HttpServletRequest request) {

        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (blogTags.isEmpty() || StringUtils.isEmpty(loginUser.getUsername())) {
            return ResultSet.inputError();
        }

        //筛选未添加的标签，添加到数据库中
        List<BlogTag> newTags = blogTags.stream().filter(tag -> tag.getId() == null).collect(Collectors.toList());

        //遍历创建标签实体
        newTags.forEach(tag -> {
            tag.setId(IdWorker.getId());
            tag.setUsedNumber(1);
            tag.setTypeId(TagTypeConstants.TAG_TYPE_USER_DEFINED);
            tag.setLastModifier(loginUser.getUsername());
            tag.setLastModifiedTime(new Date());
            tag.setCreator(loginUser.getUsername());
            tag.setCreatedTime(new Date());
        });

        //插入集合
        int i = 0;
        if (!newTags.isEmpty()) {
            i = tagMapper.insertList(newTags);
        }

        return ResultSet.success("成功插入" + i + "个标签");
    }

    @Override
    public ResultSet getTagTypeList() {

        //查询有效的标签分类
        QueryWrapper<BlogTagClasses> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", 1);
        List<BlogTagClasses> list = articleTagClassesMapper.selectList(queryWrapper);

        BlogTagClasses blogTagClasses = list.get(0);
        list.remove(0);
        list.add(blogTagClasses);

        return ResultSet.success("查询成功", list);
    }

    @Override
    public ResultSet getTagList(Long typeId, HttpServletRequest request) {

        //获取登陆人
        LoginUser loginUser = TokenUtil.getLoginUser(request);

        //设置查询条件
        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        if (TagTypeConstants.TAG_TYPE_USER_DEFINED.equals(typeId)) {
            queryWrapper.eq("creator", loginUser.getUsername());
        }

        List<BlogTag> blogTags = tagMapper.selectList(queryWrapper);
        return ResultSet.success("查询成功", blogTags);
    }

}
