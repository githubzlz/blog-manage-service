package com.zlz.blog.server.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.zlz.blog.common.entity.blog.BlogTag;
import com.zlz.blog.common.entity.common.LoginUser;
import com.zlz.blog.common.entity.tag.Tag;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.common.util.TokenUtil;
import com.zlz.blog.server.blog.mapper.BlogTagMapper;
import com.zlz.blog.server.blog.service.BlogTagService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author peeterZ
 * @version 2.0 CreateTime:2020-12-12 14:40
 * @description
 */
@Service
public class BlogTagServiceImpl implements BlogTagService {

    @Resource
    private BlogTagMapper blogTagMapper;

    @Override
    public ResultSet<BlogTag> insertTagList(Long blogId, List<Tag> tags, HttpServletRequest request) {

        //数据检查
        if (null == blogId || tags.isEmpty()) {
            return ResultSet.inputError();
        }
        tags.forEach(tag ->{
            tag.setId(IdWorker.getId());
        });
        //插入标签
        return SqlResultUtil.isOneRow(blogTagMapper.insertList(blogId, tags));
    }
}