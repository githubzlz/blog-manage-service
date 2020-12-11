package com.zlz.blog.server.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlz.blog.common.entity.article.BlogTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/16 10:43
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<BlogTag> {

    int insertList(@Param("blogTags") List<BlogTag> blogTags);
}
